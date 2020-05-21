package cn.zhuchuangsoft.footstone.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Jala {

    /**
     * 调用第三方获取Token
     *
     * @return
     * @throws IOException
     */
    @Cacheable(cacheNames = "Token")
    public String getToken() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut put = new HttpPut("http://ex-api.jalasmart.com/api/v2/Platform/Token");
            put.setConfig(BaseController.getConfig());
            put.setHeader("Content-Type", "application/json");
            JSONObject bodyJson = new JSONObject(true);
            bodyJson.put("ID", BaseController.ID);
            bodyJson.put("Secret", BaseController.SECRET);
            put.setEntity(new StringEntity(bodyJson.toJSONString()));
            HttpResponse response = httpClient.execute(put);
            String json = EntityUtils.toString(response.getEntity());
            JSONObject js = JSON.parseObject(json);
            return js.getString("Data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
