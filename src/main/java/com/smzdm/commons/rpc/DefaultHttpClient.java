package com.smzdm.commons.rpc;


/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class DefaultHttpClient extends AbstractHttpClient {
    public DefaultHttpClient() {
    }

    public DefaultHttpClient(int connectTimeout, int socketTimeout, int maxConnections) {
        super(connectTimeout, socketTimeout, maxConnections);
    }

    public DefaultHttpClient(int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections, int hostCount) {
        super(connectionRequestTimeout, connectTimeout, socketTimeout, maxConnections, hostCount);
    }
}
