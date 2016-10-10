package com.smzdm.commons.rpc;

import com.smzdm.commons.rpc.entity.HttpResult;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public interface GenericHttp {

    String doGet(String url);

    String doPost(String url);

    String doGet(String url, Map<String, String> params);

    String doPost(String url, Map<String, String> params);

    String doPostFile(String url, InputStream fileInputStream);

    <T> HttpResult<T> processResponse(String var1, Type var2);
}