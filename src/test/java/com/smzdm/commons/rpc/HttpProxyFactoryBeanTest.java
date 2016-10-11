package com.smzdm.commons.rpc;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


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

}