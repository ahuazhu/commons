package com.smzdm.commons.rpc.entity;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
import java.lang.reflect.Type;

public class HttpInvokerMethodResult {
    private Type returnType;
    private boolean isReturnHttpResult;

    public HttpInvokerMethodResult(Type returnType, boolean isReturnHttpResult) {
        this.returnType = returnType;
        this.isReturnHttpResult = isReturnHttpResult;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public boolean isReturnHttpResult() {
        return this.isReturnHttpResult;
    }
}
