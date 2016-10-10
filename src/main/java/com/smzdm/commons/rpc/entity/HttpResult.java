package com.smzdm.commons.rpc.entity;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class HttpResult<T> {
    private T data;
    private Object errorMessage;
    private boolean isSuccess = true;

    public HttpResult() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static <T> HttpResult<T> success(T data) {
        HttpResult<T> result = new HttpResult<T>();
        result.data = data;
        return result;
    }

    public static HttpResult fail(Object errorMessage) {
        HttpResult result = new HttpResult();
        result.isSuccess = false;
        result.errorMessage = errorMessage;
        return result;
    }

    public String toString() {
        return "HttpResult{data=" + this.data + ", errorMessage=" + this.errorMessage + ", isSuccess=" + this.isSuccess + '}';
    }
}
