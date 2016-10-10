package com.smzdm.commons.rpc.annotation;

/**
 * Created by zhengwenzhu on 16/10/10.
 */

import com.smzdm.commons.rpc.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpAction {
    String url() default "";

    String resultMapKey() default "";

    int retryTimes() default 0;

    HttpMethod method() default HttpMethod.GET;
}
