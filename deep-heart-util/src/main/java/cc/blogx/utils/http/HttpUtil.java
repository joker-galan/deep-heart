package cc.blogx.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String UTF_8 = "utf-8";
    private static final Integer TIMEOUT = 5000;

    public static String doPostJson(String url, String json) {
        return doPostJson(url, json, TIMEOUT);
    }

    public static String doPostJson(String url, String json, int timeOut) {
        logger.info("请求地址：" + url + " \n请求参数：" + json);
        // 创建https 访问
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        // 创建http 访问
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            //设置请求和传输超时时间 如果不设置将会一直阻塞....
            RequestConfig config = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
            httpPost.setConfig(config);
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("请求结果：" + resultString);
        return resultString;
    }
}

