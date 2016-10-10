package com.smzdm.commons.rpc.monitor;

import com.smzdm.commons.rpc.utils.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class MonitorLoader {
    private static Monitor monitor = (Monitor) ExtensionLoader.getExtension(Monitor.class);
    private static final Logger logger = LoggerFactory.getLogger(MonitorLoader.class);

    public MonitorLoader() {
    }

    public static Monitor getMonitor() {
        return monitor;
    }

    static {
        if (monitor == null) {
            monitor = new SimpleMonitor();
        }

        logger.info("monitor:" + monitor);
        monitor.init();
    }
}