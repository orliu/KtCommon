package com.tunaikita.log.bean;

import java.io.Serializable;

/**
 * Created by orliu on 17/11/30.
 */

public class HttpLog implements Serializable {

    private String _id;
    private String requestUrl;
    private String requestMethod;
    private String requestParamsJson;
    private String requestHeaderJson;
    private String responseJson;
    private Long requestTime;
    private Long useTimes;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParamsJson() {
        return requestParamsJson;
    }

    public void setRequestParamsJson(String requestParamsJson) {
        this.requestParamsJson = requestParamsJson;
    }

    public String getRequestHeaderJson() {
        return requestHeaderJson;
    }

    public void setRequestHeaderJson(String requestHeaderJson) {
        this.requestHeaderJson = requestHeaderJson;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public Long getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Long useTimes) {
        this.useTimes = useTimes;
    }

    @Override
    public String toString() {
        return "HttpLog{" +
                "_id='" + _id + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestParamsJson='" + requestParamsJson + '\'' +
                ", requestHeaderJson='" + requestHeaderJson + '\'' +
                ", responseJson='" + responseJson + '\'' +
                ", requestTime=" + requestTime +
                ", useTimes=" + useTimes +
                '}';
    }
}
