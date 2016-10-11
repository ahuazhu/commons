package com.smzdm.commons.rpc;

import com.smzdm.commons.rpc.annotation.HttpAction;
import com.smzdm.commons.rpc.entity.HttpResult;
import com.smzdm.commons.rpc.enums.HttpMethod;

/**
 * Created by zhengwenzhu on 16/10/11.
 */
public interface ChargeService {

    @HttpAction(url = "/charge/create", retryTimes = 1, method = HttpMethod.GET)
    HttpResult<Charge> charge();

    @HttpAction(url = "/charge/create", resultMapKey = "appid", retryTimes = 1, method = HttpMethod.GET)
    HttpResult<String> charge2();


}
