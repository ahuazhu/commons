package com.smzdm.commons.rpc.monitor;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class SimpleMonitor implements Monitor {
    public SimpleMonitor() {
    }

    public void init() {
    }

    public void logEvent(String type, String name, String status) {
    }

    public void logError(String message, Throwable cause) {
    }

    public MonitorTransaction newTransaction(String type, String name) {
        return new SimpleMonitorTransaction(type, name);
    }
}
