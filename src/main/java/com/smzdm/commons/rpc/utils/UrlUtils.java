package com.smzdm.commons.rpc.utils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by zhengwenzhu on 16/10/13.
 */
public class UrlUtils {

    public static String parseHost(String url) {
        try {
            return new URL(url).getHost();
        } catch (IOException e) {
            return "";
        }
    }

    public static int parsePort(String url) {
        try {
            return new URL(url).getPort();
        } catch (IOException e) {
            return -1;
        }
    }

    public static String parsePath(String url) {
        try {
            return new URL(url).getPath();
        } catch (IOException e) {
            return "";
        }
    }
}
