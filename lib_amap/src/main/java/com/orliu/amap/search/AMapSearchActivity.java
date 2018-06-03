package com.orliu.amap.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.orliu.amap.LocationInputDropDownPupup;
import com.orliu.amap.IAdapterListener;
import com.orliu.amap.R;
import com.orliu.kotlin.common.view.rv.ItemViewDelegate;
import com.orliu.kotlin.common.view.rv.RecyclerAdapter;
import com.orliu.kotlin.common.view.rv.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orliu on 19/04/2018.
 */

public class AMapSearchActivity extends AppCompatActivity
        implements LocationSource, AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener,
        Inputtips.InputtipsListener,
        View.OnClickListener {

    public static final String TAG = AMapSearchActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 0XAC;

    // navigation
    private ImageView navigationTitle;
    private EditText navigationSearch;
    private LocationInputDropDownPupup searchDropDownPopup;

    // amap
    private AMap aMap;
    private MapView mapView;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    // result container
    private RecyclerView poiResultRv;
    private RecyclerAdapter<PoiItem> poiResultAdapter;
    private LinearLayoutManager poiResultLayoutManager;

    // location
    private OnLocationChangedListener mListener;
    private Marker locationMarker;
    private LatLonPoint searchLatlonPoint;

    // geo search
    private GeocodeSearch geocodeSearch;
    private GeocodeQuery geocodeQuery;

    // poi search
    private PoiSearch poiSearch;
    private PoiSearch.Query poiSearchQuery;
    //private PoiItem firstPoiItem;
    private ArrayList<PoiItem> poiItems;
    private ArrayList<PoiItem> poiSearchResult;

    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, AMapSearchActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_location);

        initArguments();
        initNavigationBar();
        initPoiSearchResult();
        initAMap(savedInstanceState);
    }

    /**
     * params
     */
    private void initArguments() {
    }

    /**
     * navigation bar
     */
    private void initNavigationBar() {
        navigationTitle = (ImageView) findViewById(R.id.id_amap_back);
        navigationTitle.setOnClickListener(this);
        navigationSearch = (EditText) findViewById(R.id.id_amap_search);
        navigationSearch.addTextChangedListener(new SearchWatcher());
    }

    /**
     * navigation edittext watcher
     */
    private class SearchWatcher implements TextWatcher {

        private int prevInputLen = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String searchText = s.toString().trim();
            if (searchText.length() > prevInputLen) {
                doInputtipQuery(searchText);
            }
            prevInputLen = searchText.length();
        }
    }

    /**
     * InputipsQuery
     *
     * @param searchText
     */
    private void doInputtipQuery(String searchText) {
        if (TextUtils.isEmpty(searchText)) return;
        InputtipsQuery inputtipsQuery = new InputtipsQuery(searchText, "");
        Inputtips inputtips = new Inputtips(AMapSearchActivity.this, inputtipsQuery);
        inputtips.setInputtipsListener(AMapSearchActivity.this);
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {

            ArrayList<Tip> arrys = new ArrayList<>();
            arrys.addAll(list);
            showDropDownPopup(arrys);
        } else {
            Log.e(TAG, "erroCode " + rCode);
        }
    }

    /**
     * 下拉关键字结果
     */
    private void showDropDownPopup(ArrayList<Tip> tips) {
        if (searchDropDownPopup == null) {
            searchDropDownPopup = new LocationInputDropDownPupup(this);
            searchDropDownPopup.setOnItemClickListener(new DropDownItemClickListener(tips));
        }
        searchDropDownPopup.setTips(tips);
        searchDropDownPopup.show(navigationSearch);
    }

    /**
     * 下拉关键字结果，点击事件
     */
    private class DropDownItemClickListener implements IAdapterListener<Tip> {
        private List<Tip> tips;

        public DropDownItemClickListener(List<Tip> tips) {
            this.tips = tips;
        }

        @Override
        public void onClick(Tip tip, int position) {
            if (searchDropDownPopup != null) searchDropDownPopup.dismissPopup();

            searchLatlonPoint = tip.getPoint();
            if (searchLatlonPoint == null && tips.size() >= 2) {
                searchLatlonPoint = tips.get(1).getPoint();
            }
            if (searchLatlonPoint == null) return;

            // camera
            if (aMap != null) {
                LatLng latLng = new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
            }

            doPoiSearch();
        }
    }

    /**
     * 初始化
     */
    private void initAMap(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.id_amap_view);
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                geoSearch();
            }
        });

        aMap.setOnMapLoadedListener(() -> addMarkerInScreenCenter(null));
    }

    /**
     * 定位marker
     *
     * @param locationLatLng
     */
    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        locationMarker.setZIndex(1);

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        //aMap.setMyLocationStyle(myLocationStyle);
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //mListener.onLocationChanged(amapLocation);
                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                geoSearch();

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e(TAG, "AmapErr : " + errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * geo code search
     */
    private void geoSearch() {
        if (searchLatlonPoint == null) return;
        if (geocodeSearch == null) {
            geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(this);

        }
        RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * geo code search listener
     *
     * @param result
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
//                String address = result.getRegeocodeAddress().getProvince()
//                        + result.getRegeocodeAddress().getCity()
//                        + result.getRegeocodeAddress().getDistrict()
//                        + result.getRegeocodeAddress().getTownship();
//                firstPoiItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                doPoiSearch();
            }
        } else {
            Log.e(TAG, "error code is " + rCode);
        }
    }

    /**
     * geo code search listener
     *
     * @param geocodeResult
     * @param i
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * poi search
     */
    private void doPoiSearch() {
        poiSearchQuery = new PoiSearch.Query("", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        poiSearchQuery.setCityLimit(true);
        poiSearchQuery.setPageSize(20);
        poiSearchQuery.setPageNum(0);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(this, poiSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
            poiSearch.searchPOIAsyn();
        }
    }

    /**
     * poi search listener
     *
     * @param poiResult
     * @param resultCode
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(poiSearchQuery)) {
                    poiItems = poiResult.getPois();
                    updatePoiSearchResult(poiItems);
                }
            } else {
                Log.i(TAG, "poi search: no result");
                updatePoiSearchResult(null);
            }
        }
    }

    /**
     * poi search listener
     *
     * @param poiItem
     * @param i
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 更新查询结果
     *
     * @param poiItems
     */
    private void updatePoiSearchResult(List<PoiItem> poiItems) {
        hideSoftKey(navigationSearch);
        if (poiSearchResult == null)
            poiSearchResult = new ArrayList<>();
        if (poiSearchResult.size() > 0)
            poiSearchResult.clear();
        //poiSearchResult.add(firstPoiItem);
        if (poiItems != null) poiSearchResult.addAll(poiItems);
        poiResultAdapter.setData(poiSearchResult);
        if (poiResultLayoutManager != null) poiResultLayoutManager.scrollToPosition(0);
    }

    /**
     * 响应逆地理编码
     */
    public void geoAddress() {
//        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
//        showDialog();
//        searchText.setText("");
//        if (searchLatlonPoint != null){
//            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//            geocoderSearch.getFromLocationAsyn(query);
//        }
    }

    /**
     * PoiSearchResult rv
     */
    private void initPoiSearchResult() {
        poiResultRv = (RecyclerView) findViewById(R.id.id_amap_rv);
        if (poiResultLayoutManager == null)
            poiResultLayoutManager = new LinearLayoutManager(this);
        poiResultRv.setLayoutManager(poiResultLayoutManager);
        if (poiResultAdapter == null)
            poiResultAdapter = new RecyclerAdapter<>(this);
        if (poiSearchResult == null) poiSearchResult = new ArrayList<>();
        poiResultAdapter.setData(poiSearchResult);
        poiResultAdapter.addItemViewDelegate(new PoiItemDelegateView());
        poiResultRv.setAdapter(poiResultAdapter);
    }

    /**
     * PoiSearchResult List Item
     */
    private class PoiItemDelegateView implements ItemViewDelegate<PoiItem> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_poi_search_result;
        }

        @Override
        public boolean isForViewType(PoiItem item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, PoiItem poiItem, int position) {
            holder.withView(R.id.id_poi_title).setText(poiItem.getTitle());
            StringBuffer sb = new StringBuffer();
            sb.append(poiItem.getCityName()).append(poiItem.getAdName()).append(poiItem.getSnippet());
            holder.withView(R.id.id_poi_address).setText(sb.toString());

            if (position == 0) {
                holder.withView(R.id.id_poi_address_label).setVisible(View.VISIBLE);
            } else {
                holder.withView(R.id.id_poi_address_label).setVisible(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra("addressStr", sb.toString());
                setResult(RESULT_OK, data);
                finish();
            });
        }
    }

    private void hideSoftKey(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_amap_back) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

}
