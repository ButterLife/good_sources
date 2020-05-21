package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.config.RsaKeyProperties;
import cn.zhuchuangsoft.footstone.controller.ex.RequestMethodErrorException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

@Data
public class BaseController {


    public static final String SEPARATOR = "&";
    /**
     * Jala给定的ID
     */
    public static final String ID = "5dca730ecb1f5b21b4525c64";
    /**
     * Jala给定的secret
     */
    public static final String SECRET = "d7f842a684a34ef580a35807083c86ef";
    /**
     * 状态标识码
     */
    public static final Integer SUCCESS = 0;
    public static final Integer FAILED = 1;
    public static final Integer TOKEN_EXPIRED = 10001;
    public static final Integer NO_RIGHT_TO_VISIT = 10002;
    /**
     * 角色类型
     */
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String PROXY = "ROLE_PROXY";
    public static final String USER = "ROLE_USER";
    /**
     * 拼接所需字符串
     */
    private static final String NONCE = "Nonce=";
    private static final String TIME_STAMP = "TimeStamp=";
    private static final String USER_ID = "UserID=";
    private static final String TOKEN = "Token=";
    @Autowired
    private Jala jala;

    @Autowired
    private RsaKeyProperties prop;

    /**
     * md5加密
     *
     * @return
     */
    public static String getMd5(String[] data, String token) {
        Arrays.sort(data);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            stringBuilder.append(data[i]).append(SEPARATOR);
        }
        stringBuilder.append(TOKEN).append(token);
        return DigestUtils.md5Hex(stringBuilder.toString());
    }

    /**
     * 生成随机字符
     *
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取请求配置类
     *
     * @return
     */
    public static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(10 * 1000)
                .setConnectTimeout(100 * 1000)
                .build();
        return config;
    }

    /**
     * 根据请求实例来获取json返回值
     *
     * @param httpRequestBase
     * @return
     * @throws IOException
     */
    public static String getJson(HttpRequestBase httpRequestBase) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpRequestBase);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 获取请求客户端实例
     *
     * @param url           请求路径地址
     * @param authorization 签名
     * @param method        请求方式 1:get 2:post 3:put
     * @return
     * @throws IOException
     */
    public static HttpRequestBase getRequestByUrl(String url, String authorization, Integer method) throws IOException {
        HttpRequestBase httpRequestBase;
        if (method == 1) {
            httpRequestBase = new HttpGet(url);
        } else if (method == 2) {
            httpRequestBase = new HttpPost(url);
        } else if (method == 3) {
            httpRequestBase = new HttpPut(url);
        } else {
            throw new RequestMethodErrorException("不正确的请求方法参数");
        }
        httpRequestBase.setConfig(getConfig());
        httpRequestBase.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpRequestBase.setHeader("Authorization", authorization);
        return httpRequestBase;
    }

    /**
     * 获取随机UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取签名
     *
     * @param parameters 需要添加的参数
     *                   根据 参数名=参数值的方式添加到data集合中
     * @return
     */
    public String getAuthorization(List<String> parameters) {
        if (parameters == null) {
            parameters = new ArrayList<String>();
        }
        String token = jala.getToken();
        String nonce = getRandomString(6);
        String timeStamp = String.valueOf(new Date().getTime());
        parameters.add(NONCE + nonce);
        parameters.add(TIME_STAMP + timeStamp);
        parameters.add(USER_ID + ID);
        String[] datas = parameters.toArray(new String[0]);
        String signature = getMd5(datas, token);
        JSONObject js = new JSONObject(true);
        js.put("UserID", "5dca730ecb1f5b21b4525c64");
        js.put("Nonce", nonce);
        js.put("TimeStamp", timeStamp);
        js.put("Signature", signature);
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(js.toJSONString().getBytes());
    }

    /**
     * @param parameters 参数 DeviceID:Value
     * @param url        Jala api请求Url
     * @param methodType 请求的类型，1:get,2:post,3:put
     * @return 响应的Json字符串
     * @throws IOException
     */
    public String getResponseJson(Map<String, Object> parameters, String url, int methodType) throws IOException {
        List<String> list = new ArrayList<>();
        JSONObject bodyJson = new JSONObject(true);
        String result = "";
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            list.add(parameter.getKey() + "=" + parameter.getValue());
            bodyJson.put(parameter.getKey(), parameter.getValue());
        }
        String authorization = getAuthorization(list);
        HttpRequestBase request = getRequestByUrl(url, authorization, methodType);
        if (methodType == 1) {
            result = getJson(request);
        } else if (methodType == 2) {
            HttpPost post = (HttpPost) request;
            post.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            result = getJson(post);
        } else if (methodType == 3) {
            HttpPut put = (HttpPut) request;
            put.setEntity(new StringEntity(bodyJson.toJSONString(), "UTF-8"));
            result = getJson(put);
        }
        return result;
    }

}
