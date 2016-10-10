package com.smzdm.commons.rpc.utils;

import com.alibaba.fastjson.JSON;
import com.smzdm.commons.rpc.annotation.HttpAction;
import com.smzdm.commons.rpc.annotation.HttpParam;
import com.smzdm.commons.rpc.entity.HttpInvokerMethod;
import com.smzdm.commons.rpc.entity.HttpInvokerMethodResult;
import com.smzdm.commons.rpc.entity.HttpResult;
import com.smzdm.commons.rpc.enums.HttpMethod;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public final class HttpInvokerUtils {
    private static final ConcurrentMap<Method, Object> methodParamAnnotationsCached = new ConcurrentHashMap<Method, Object>();
    private static final ConcurrentMap<Method, HttpInvokerMethodResult> methodResultCached = new ConcurrentHashMap<Method, HttpInvokerMethodResult> ();

    public HttpInvokerUtils() {
    }

    public static HttpInvokerMethod createHttpInvokerMethod(Method method, Object[] arguments) {
        if(method != null && arguments != null) {
            HttpAction httpAction = (HttpAction)method.getAnnotation(HttpAction.class);
            if(httpAction == null) {
                throw new IllegalArgumentException("The method " + method.getName() + " must be annotated by " + HttpAction.class.getName());
            } else {
                Map<String, String> paramValues = new HashMap<String, String>();
                if(httpAction.method() != HttpMethod.POST_FILE) {
                    Class[] paramTypes = method.getParameterTypes();
                    Annotation[][] paramAnnotations = getParameterAnnotations(method);

                    for(int i = 0; i < paramTypes.length; ++i) {
                        HttpParam httpParam = null;
                        Annotation[] paramAnnotation = paramAnnotations[i];
                        for (Annotation annotation : paramAnnotation) {
                            if(annotation instanceof HttpParam) {
                                httpParam = (HttpParam)annotation;
                                break;
                            }
                        }

                        if(httpParam == null || "json".equalsIgnoreCase(httpParam.value())) {
                            if(paramTypes.length != 1) {
                                throw new RuntimeException("httpParam json type must be only one parameter");
                            }

                            paramValues.put("json", JSON.toJSONString(arguments[i]));
                            break;
                        }

                        if(arguments[i] instanceof String) {
                            paramValues.put(httpParam.value(), arguments[i].toString());
                        } else if(arguments[i] instanceof Number) {
                            paramValues.put(httpParam.value(), String.valueOf(arguments[i]));
                        } else if(arguments[i] instanceof Collection) {
                            paramValues.put(httpParam.value(), StringUtils.join((Collection) arguments[i], ','));
                        } else {
                            if(!(arguments[i] instanceof Map)) {
                                throw new RuntimeException("httpParam is not Collection or Map or String or Number");
                            }

                            Map var13 = (Map)arguments[i];
                            Iterator var14 = var13.keySet().iterator();

                            while(var14.hasNext()) {
                                Object var15 = var14.next();
                                paramValues.put(String.valueOf(var15), String.valueOf(var13.get(var15)));
                            }
                        }
                    }
                }

                return new HttpInvokerMethod(httpAction.url(), httpAction.method(), httpAction.retryTimes(), httpAction.resultMapKey(), paramValues, getHttpInvokerMethodResult(method));
            }
        } else {
            throw new IllegalArgumentException("method and arguments must be no null");
        }
    }

    private static HttpInvokerMethodResult generateHttpInvokerMethodResult(Method method) {
        boolean isReturnHttpResult = method.getReturnType() == HttpResult.class;
        Type returnType = method.getGenericReturnType();
        if(isReturnHttpResult && returnType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType)returnType).getActualTypeArguments();
            if(types != null && types.length == 1) {
                returnType = types[0];
            }
        }

        return new HttpInvokerMethodResult(basicTypeToPackageType(returnType), isReturnHttpResult);
    }

    private static Type basicTypeToPackageType(Type basicType) {
        return (Type)(basicType == Integer.TYPE?Integer.class:(basicType == Boolean.TYPE?Boolean.class:(basicType == Long.TYPE?Long.class:(basicType == Float.TYPE?Float.class:(basicType == Double.TYPE?Double.class:(basicType == Byte.TYPE?Byte.class:(basicType == Short.TYPE?Short.class:(basicType == Character.TYPE?Character.class:basicType))))))));
    }

    private static HttpInvokerMethodResult getHttpInvokerMethodResult(Method method) {
        HttpInvokerMethodResult httpInvokerMethodResult = (HttpInvokerMethodResult)methodResultCached.get(method);
        if(httpInvokerMethodResult == null) {
            synchronized(method) {
                httpInvokerMethodResult = (HttpInvokerMethodResult)methodResultCached.get(method);
                if(httpInvokerMethodResult == null) {
                    httpInvokerMethodResult = generateHttpInvokerMethodResult(method);
                    methodResultCached.put(method, httpInvokerMethodResult);
                }
            }
        }

        return httpInvokerMethodResult;
    }

    private static Annotation[][] getParameterAnnotations(Method method) {
        Object annotations = methodParamAnnotationsCached.get(method);
        if(annotations == null) {
            synchronized(method) {
                annotations = methodParamAnnotationsCached.get(method);
                if(annotations == null) {
                    annotations = method.getParameterAnnotations();
                    methodParamAnnotationsCached.put(method, annotations);
                }
            }
        }

        return (Annotation[][])((Annotation[][])annotations);
    }
}
