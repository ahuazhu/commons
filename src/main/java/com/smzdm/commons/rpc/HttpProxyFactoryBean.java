package com.smzdm.commons.rpc;

/**
 * Created by zhengwenzhu on 16/10/10.
 */

import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class HttpProxyFactoryBean implements FactoryBean {
    private String proxyInterfaces;
    private InvocationHandler invocationHandler;
    private Object proxy;
    private Class<?> proxyType;
    private String serviceName;

    public HttpProxyFactoryBean() {
    }

    public void init() throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        this.proxyType = ClassUtils.getClass(classLoader, this.proxyInterfaces.trim());
        DefaultHttpClient client = new DefaultHttpClient();
        client.setServiceName(serviceName);
        invocationHandler = new HttpInvocationHandler(client);
        this.proxy = Proxy.newProxyInstance(classLoader, new Class[]{this.proxyType}, this.invocationHandler);
    }

    public Object getObject() throws Exception {
        return this.proxy;
    }

    public Class<?> getObjectType() {
        return this.proxyType;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setProxyInterfaces(String proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}

