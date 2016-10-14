package com.smzdm.commons.rpc.entity;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwenzhu on 16/10/13.
 */
public class Context implements Cat.Context {

    private Map<String, String> map = new HashMap<String, String>();

    @Override
    public void addProperty(String key, String value) {
        map.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return map.get(key);
    }

    public Map<String, String> getMap() {
        return map;
    }
}
