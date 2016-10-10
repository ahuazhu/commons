package com.smzdm.commons.rpc.entity;

/**
 * Created by zhengwenzhu on 16/10/10.
 */

import com.smzdm.commons.rpc.enums.HttpMethod;

import java.util.Map;

public class HttpInvokerMethod {
    private String url;
    private HttpMethod method;
    private int retryTimes;
    private String resultMapKey;
    private Map<String, String> params;
    private HttpInvokerMethodResult httpInvokerMethodResult;

    public HttpInvokerMethod(String url, HttpMethod method, int retryTimes, String resultMapKey, Map<String, String> params, HttpInvokerMethodResult httpInvokerMethodResult) {
        this.url = url;
        this.method = method;
        this.retryTimes = retryTimes;
        this.resultMapKey = resultMapKey;
        this.params = params;
        this.httpInvokerMethodResult = httpInvokerMethodResult;
    }

    public String getUrl() {
        return this.url;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    public String getResultMapKey() {
        return this.resultMapKey;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public HttpInvokerMethodResult getHttpInvokerMethodResult() {
        return this.httpInvokerMethodResult;
    }
}

