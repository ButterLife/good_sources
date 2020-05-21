package cn.zhuchuangsoft.footstone.utils;


import cn.zhuchuangsoft.footstone.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;

public class JiGuangApiMethod {

    private static final String APP_KEY = "1130b53b806062932dba19e8";
    private static final String MASTER_SECRET = "90852b290101af8ea6f2f8d3";
    private static final String PUSH = "";
    private static final String SMS = "https://api.sms.jpush.cn/v1/codes";

    private static final String BASIC;

    static {
        BASIC = Base64.getEncoder().encodeToString((APP_KEY + ":" + MASTER_SECRET).getBytes());
    }

    /**
     * 根据手机号发送验证码
     *
     * @param mobile
     * @return 返回回调接口所需的id
     */
    public static String sendCode(String mobile) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", mobile);
        jsonObject.put("sign_id", 11553);
        jsonObject.put("temp_id", 1);
        jsonObject = JSON.parseObject(getHttp(SMS, jsonObject.toJSONString()));
        if (jsonObject.containsKey("msg_id")) {
            return jsonObject.getString("msg_id");
        } else {
            return null;
        }
    }

    /**
     * 短信验证码回调接口
     *
     * @param msgId
     * @param code
     * @return
     */
    public static boolean codeCallback(String msgId, String code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject = JSON.parseObject(getHttp("https://api.sms.jpush.cn/v1/codes/" + msgId + "/valid", jsonObject.toJSONString()));
        return jsonObject.getBoolean("is_valid");
    }

    /**
     * 获取调用极光API返回值
     *
     * @param url      路径
     * @param bodyJson 参数
     * @return 请求结果的json字符串
     */
    public static String getHttp(String url, String bodyJson) {
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(BaseController.getConfig());
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            post.setHeader("Authorization", "Basic " + BASIC);
            post.setEntity(new StringEntity(bodyJson));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
