package com.smzdm.commons.rpc;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.smzdm.commons.rpc.entity.Context;
import com.smzdm.commons.rpc.entity.HttpResult;
import com.smzdm.commons.rpc.monitor.Monitor;
import com.smzdm.commons.rpc.monitor.MonitorLoader;
import com.smzdm.commons.rpc.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
@ThreadSafe
public class AbstractHttpClient implements GenericHttp {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClient.class);
    protected static final Monitor monitor = MonitorLoader.getMonitor();
    protected static final String APPLICATION_TRANSACTION = "ApplicationTransaction";
    protected static final String DEFAULT_FILE_NAME = "attachment.png";
    protected static final ContentType imageContentType = ContentType.create("image/png");
    protected final int connectionRequestTimeout;
    protected final int connectTimeout;
    protected final int socketTimeout;
    protected final int maxConnections;
    protected final int hostCount;
    protected boolean openLogInfo;
    protected String defaultCharset;
    protected HttpClient httpClient;
    private String serviceName = "";

    public AbstractHttpClient() {
        this(1000, 5000, 200);
    }

    public AbstractHttpClient(int connectTimeout, int socketTimeout, int maxConnections) {
        this(connectTimeout, connectTimeout, socketTimeout, maxConnections, 1);
    }

    public AbstractHttpClient(int connectionRequestTimeout, int connectTimeout, int socketTimeout, int maxConnections, int hostCount) {
        this.openLogInfo = true;
        this.defaultCharset = Consts.UTF_8.name();
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.maxConnections = maxConnections;
        this.hostCount = hostCount;
        this.initHttpClient();
    }

    private void initHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(this.connectionRequestTimeout).setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(this.maxConnections);
        connectionManager.setDefaultMaxPerRoute(this.maxConnections / this.hostCount);
        this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
    }

    @Override
    public String doGet(String url) {
        return doGet(url, new HashMap<String, String>());
    }

    @Override
    public String doPost(String url) {
        return doPost(url, new HashMap<String, String>());
    }

    public String doGet(String url, Map<String, String> params) {

        url = serviceName + url;
        String method = UrlUtils.parsePath(url);

        Transaction t = Cat.newTransaction("Call", method);
        HttpGet httpGet = null;
        String responseBody = null;

        try {
            url = enrichGetParameter(url, params);
            httpGet = new HttpGet(url);
            logCatCross(httpGet, method);
            this.addAuth(httpGet, url);
            HttpResponse e = this.httpClient.execute(httpGet);
            logCall(url, e);

            if (200 != e.getStatusLine().getStatusCode()) {
                throw new RuntimeException("response code not 200, response code: " + e.getStatusLine().getStatusCode());
            }

            HttpEntity httpEntity = e.getEntity();
            responseBody = EntityUtils.toString(httpEntity, this.defaultCharset);
            EntityUtils.consume(httpEntity);
            if (StringUtils.isEmpty(responseBody)) {
                throw new RuntimeException("response is empty");
            }

            if (this.openLogInfo) {
                logger.info((new StringBuilder(300)).append("doGet url: ").append(url).append(",responseBody: ").append(responseBody).toString());
            }

            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            throw new RuntimeException("doGet error,url: " + url + ",errorMessage: " + e.getMessage(), e);
        } finally {
            t.complete();
            if (httpGet != null) {
                httpGet.abort();
            }

        }

        return responseBody;
    }

    private void logCatCross(HttpRequestBase request, String method) {

        Context context = new Context();
        Cat.logRemoteCallClient(context);
        context.addProperty("_catCallerDomain", Cat.getManager().getDomain());
        context.addProperty("_catCallerMethod", method);
        for (Map.Entry<String, String> entry : context.getMap().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public String doPost(String url, Map<String, String> params) {
        url = serviceName + url;
        String method = UrlUtils.parsePath(url);

        Transaction t = Cat.newTransaction("Call", method);
        HttpPost httpPost = null;
        String responseBody = null;
        try {
            httpPost = new HttpPost(url);
            logCatCross(httpPost, method);
            this.addAuth(httpPost, url);
            this.enrichPostParameter(httpPost, params);
            HttpResponse e = this.httpClient.execute(httpPost);

            logCall(url, e);

            if (200 != e.getStatusLine().getStatusCode()) {
                throw new RuntimeException("response code not 200, response code: " + e.getStatusLine().getStatusCode());
            }

            HttpEntity httpEntity = e.getEntity();
            responseBody = EntityUtils.toString(httpEntity, this.defaultCharset);
            EntityUtils.consume(httpEntity);
            if (StringUtils.isEmpty(responseBody)) {
                throw new RuntimeException("response is empty");
            }

            if (this.openLogInfo) {
                logger.info((new StringBuilder(500)).append("doPost url: ").append(url).append(",data: ").append(params.toString()).append(",responseBody: ").append(responseBody).toString());
            }

            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            throw new RuntimeException("doPost error,url: " + url + ",params: " + params + ",errorMessage: " + e.getMessage(), e);
        } finally {
            t.complete();
            if (httpPost != null) {
                httpPost.abort();
            }

        }

        return responseBody;
    }

    private void logCall(String url, HttpResponse e) {
        String domain = e.getFirstHeader("_catServerDomain") == null ? null : e.getFirstHeader("_catServerDomain").getValue();
        String server = e.getFirstHeader("_catServer") == null ? null : e.getFirstHeader("_catServer").getValue();
        if (domain == null) domain = serviceName;
        if (server == null) server = UrlUtils.parseHost(url);

        Cat.logEvent("Call.server", server);
//        Cat.logEvent("Call.ip", server);
        Cat.logEvent("PigeonCall.app", domain);
//        Cat.logEvent("Call.port", String.valueOf(UrlUtils.parsePort(serviceName)));
    }

    private void addAuth(HttpRequestBase request, String url) {
        //
    }

    public String doPostFile(String url, InputStream inputStream) {
        url = serviceName + url;
        String method = UrlUtils.parsePath(url);

        Transaction t = Cat.newTransaction("Call", method);

        HttpPost httpPost = null;
        String responseBody = null;

        try {
            httpPost = new HttpPost(url);
            logCatCross(httpPost, method);
            this.addAuth(httpPost, url);
            this.enrichPostFileParameter(httpPost, inputStream);
            HttpResponse e = this.httpClient.execute(httpPost);
            logCall(url, e);

            if (200 != e.getStatusLine().getStatusCode()) {
                throw new RuntimeException("response code not 200, response code: " + e.getStatusLine().getStatusCode());
            }

            HttpEntity httpEntity = e.getEntity();
            responseBody = EntityUtils.toString(httpEntity, this.defaultCharset);
            EntityUtils.consume(httpEntity);
            if (StringUtils.isEmpty(responseBody)) {
                throw new RuntimeException("response is empty");
            }

            if (this.openLogInfo) {
                logger.info((new StringBuilder(500)).append("doPostFile url: ").append(url).append(",responseBody: ").append(responseBody).toString());
            }

            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            throw new RuntimeException("doPostFile error,url: " + url + ",errorMessage: " + e.getMessage(), e);
        } finally {
            t.complete();
            if (httpPost != null) {
                httpPost.abort();
            }

        }

        return responseBody;
    }

    public <T> HttpResult<T> processResponse(String responseBody, Type type) {
        return HttpResult.success(JSON.<T>parseObject(responseBody, type));
    }

    private void enrichPostFileParameter(HttpPost httpPost, InputStream inputStream) throws UnsupportedEncodingException {
        InputStreamBody inputStreamBody = new InputStreamBody(inputStream, imageContentType, "attachment.png");
        HttpEntity httpEntity = MultipartEntityBuilder.create().addPart("image", inputStreamBody).build();
        httpPost.setEntity(httpEntity);
    }

    private void enrichPostParameter(HttpPost httpPost, Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null && params.size() != 0) {
            if (params.size() == 1 && params.get("json") != null) {
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity nameValuePairs1 = new StringEntity((String) params.get("json"), this.defaultCharset);
                nameValuePairs1.setContentType("text/json");
                nameValuePairs1.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
                httpPost.setEntity(nameValuePairs1);
            } else {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                UrlEncodedFormEntity urlEncodedFormEntity1 = new UrlEncodedFormEntity(nameValuePairs, this.defaultCharset);
                httpPost.setEntity(urlEncodedFormEntity1);
            }
        }

    }

    private String enrichGetParameter(String url, Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null && params.size() != 0) {
            StringBuilder sbUrl = new StringBuilder(url);
            if (!url.endsWith("?")) {
                sbUrl.append("?");
            }

            for (Map.Entry<String, String> entry : params.entrySet()) {

                if (url.contains("{" + entry.getKey() + "}")) {
                    url = url.replace("{" + entry.getKey() + "}", entry.getKey());
                } else {
                    sbUrl.append(URLEncoder.encode(entry.getKey(), this.defaultCharset)).append('=').append(URLEncoder.encode((String) entry.getValue(), this.defaultCharset)).append('&');
                }
            }


            sbUrl.delete(sbUrl.length() - 1, sbUrl.length());
            url = sbUrl.toString();
        }

        return url;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDefaultCharset() {
        return this.defaultCharset;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public void setOpenLogInfo(boolean openLogInfo) {
        this.openLogInfo = openLogInfo;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
