package com.smzdm.commons.rpc.aop;

import com.alibaba.fastjson.JSON;
import com.smzdm.commons.rpc.GenericHttp;
import com.smzdm.commons.rpc.entity.HttpInvokerMethod;
import com.smzdm.commons.rpc.entity.HttpResult;
import com.smzdm.commons.rpc.utils.HttpInvokerUtils;
import com.smzdm.commons.rpc.utils.HttpResultUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class HttpAdvice //implements IntroductionInterceptor
{
//    public HttpAdvice() {
//    }
//
//    public boolean implementsInterface(Class<?> clazz) {
//        return clazz.isInterface() && GenericHttp.class.isAssignableFrom(clazz);
//    }
//
//    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
//        if (!this.implementsInterface(methodInvocation.getMethod().getDeclaringClass())) {
//            return methodInvocation.proceed();
//        } else {
//            GenericHttp realWorker = (GenericHttp) methodInvocation.getThis();
//            HttpInvokerMethod invokerMethod = HttpInvokerUtils.createHttpInvokerMethod(methodInvocation.getMethod(), methodInvocation.getArguments());
//            HttpResult httpResult = HttpResultUtils.httpCall(realWorker, invokerMethod, methodInvocation.getArguments());
//            if (invokerMethod.getHttpInvokerMethodResult().isReturnHttpResult()) {
//                return httpResult;
//            } else if (!httpResult.isSuccess()) {
//                throw new RuntimeException(httpResult.getErrorMessage() instanceof String ? httpResult.getErrorMessage().toString() : JSON.toJSONString(httpResult.getErrorMessage()));
//            } else {
//                return httpResult.getData();
//            }
//        }
//    }
}