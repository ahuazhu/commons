package com.smzdm.merchant.inter;

import com.smzdm.commons.rpc.annotation.HttpAction;
import com.smzdm.commons.rpc.annotation.HttpParam;
import com.smzdm.commons.rpc.enums.HttpMethod;

import java.util.Map;

/**
 * Created by lichao on 16/10/11.
 */
public interface MerchantOrderService {
    @HttpAction(retryTimes = 3, url="/callback", method = HttpMethod.GET)
    MerchantResponse payNotifyOrder(@HttpParam Map<String,String> params);
}
