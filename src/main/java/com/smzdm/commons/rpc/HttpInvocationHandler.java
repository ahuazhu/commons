package com.smzdm.commons.rpc;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
import com.alibaba.fastjson.JSON;
import com.smzdm.commons.rpc.entity.HttpInvokerMethod;
import com.smzdm.commons.rpc.entity.HttpResult;
import com.smzdm.commons.rpc.utils.HttpInvokerUtils;
import com.smzdm.commons.rpc.utils.HttpResultUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpInvocationHandler implements InvocationHandler {
    private final GenericHttp realWorker;

    public HttpInvocationHandler() {
        this.realWorker = new DefaultHttpClient();
    }

    public HttpInvocationHandler(GenericHttp realWorker) {
        this.realWorker = realWorker;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpInvokerMethod invokerMethod = HttpInvokerUtils.createHttpInvokerMethod(method, args);
        HttpResult httpResult = HttpResultUtils.generateHttpResult(this.realWorker, invokerMethod, args);
        if(invokerMethod.getHttpInvokerMethodResult().isReturnHttpResult()) {
            return httpResult;
        } else if(!httpResult.isSuccess()) {
            throw new RuntimeException(httpResult.getErrorMessage() instanceof String?httpResult.getErrorMessage().toString(): JSON.toJSONString(httpResult.getErrorMessage()));
        } else {
            return httpResult.getData();
        }
    }
}

