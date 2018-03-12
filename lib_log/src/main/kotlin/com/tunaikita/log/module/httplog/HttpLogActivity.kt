package com.tunaikita.log.module.httplog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.orliu.kotlin.common.extension.other.parseJson
import com.orliu.kotlin.common.view.rv.ItemViewDelegate
import com.orliu.kotlin.common.view.rv.RecyclerAdapter
import com.orliu.kotlin.common.view.rv.ViewHolder
import com.tunaikita.log.R
import com.tunaikita.log.bean.HttpLog
import com.tunaikita.log.bean.Result
import com.tunaikita.log.database.Database
import kotlinx.android.synthetic.main.activity_httplog.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * HttpLog日志列表页
 * Created by orliu on 17/11/30.
 */
class HttpLogActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter<HttpLog>
    private var httplogs: ArrayList<HttpLog>? = null

    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_httplog)

        initView()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        id_httplog_rv.layoutManager = layoutManager

        adapter = RecyclerAdapter(this)
        adapter.addItemViewDelegate(HttpLogItemViewDelegate())
        adapter.setData(httplogs)
        id_httplog_rv.adapter = adapter
    }

    private fun initData() {
        httplogs = Database.getInstance().query(HttpLog::class.java)
        Collections.reverse(httplogs)
        adapter.update(httplogs)
    }

    private inner class HttpLogItemViewDelegate : ItemViewDelegate<HttpLog> {

        override fun getItemViewLayoutId() = R.layout.item_httplog

        override fun isForViewType(item: HttpLog, position: Int) = true

        override fun convert(holder: ViewHolder, item: HttpLog, position: Int) {
            item?.let {
                holder?.apply {
                    val requestTime = "${sdf.format(Date(it.requestTime))}"
                    val useTime = "duration: ${it.useTimes}ms"
                    val requestUrl = it.requestUrl

                    withView(R.id.id_httplog_request_time).setText(requestTime)
                    withView(R.id.id_httplog_use_time).setText(useTime)
                    withView(R.id.id_httplog_url).setText(requestUrl)

                    val result : Result<*> = Gson().parseJson(it.responseJson)
                    var colorRes = when (result.errcode) {
                        Result.SUCCESS -> {
                            if (it.useTimes >= 500) {
                                resources.getColor(android.R.color.holo_orange_dark)
                            } else {
                                resources.getColor(android.R.color.white)
                            }

                        }
                        else -> resources.getColor(android.R.color.holo_red_dark)
                    }
                    itemView.setBackgroundColor(colorRes)

                    onItemClickListener(View.OnClickListener {
                        startActivity<HttpLogDetailsActivity>("httpLog" to item)
                    })
                }
            }
        }

    }
}