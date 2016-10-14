package com.smzdm.commons.rpc;

import com.smzdm.merchant.inter.MerchantOrderService;
import com.smzdm.merchant.inter.MerchantResponse;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhengwenzhu on 16/10/11.
 */
public class HttpProxyFactoryBeanTest {

    @Test
    public void testInvoke() throws ClassNotFoundException {
        Resource res = new ClassPathResource("appcontext-beans.xml");
        BeanFactory factory = new XmlBeanFactory(res);

        ChargeService chargeService = (ChargeService) factory.getBean("chargeService");
        Charge charge = chargeService.charge().getData();
        String appId = chargeService.charge2().getData();
    }


    @Test
    public void payNotifyOrder() throws Exception {

        Resource res = new ClassPathResource("appcontext-beans.xml");
        BeanFactory factory = new XmlBeanFactory(res);

        MerchantOrderService merchantOrderService = (MerchantOrderService) factory.getBean(MerchantOrderService.class);

        Map<String,String> params = new HashMap<String,String>();
        params.put("param1", "param1test");
        params.put("param2", "param2test");

        MerchantResponse resp = merchantOrderService.payNotifyOrder(params);

        Assert.assertNotNull(resp);

    }

}