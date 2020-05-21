package cn.zhuchuangsoft.footstone.timing_task;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ScheduledTask {

    @CacheEvict(cacheNames = "jalaToken")
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 15)
    public void push() {
        System.out.println("清除缓存");
    }

    /**
     * 根据控制器ID获取告警信息
     * @param controllerId
     * @param messageIds
     */
    /*public void timingGetWarning(String controllerId, List<String> messageIds){
        List<String> parameter = new ArrayList<String>();
        parameter.add("ControllerID="+controllerId);
        parameter.add("Page=0");
        String authorization = getAuthorization(parameter);
        String url = "http://ex-api.jalasmart.com/api/v2/messages/"+controllerId+"/0";
        try{
            HttpRequestBase httpRequestBase = BaseController.getRequestByUrl(url,authorization,1);
            String json = BaseController.getJson(httpRequestBase);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray data = jsonObject.getJSONArray("Data");
            for (int i=0;i<data.size();i++){
                JSONObject js = data.getJSONObject(i);
                if(!messageIds.contains(js.getString("MessageID"))){
                    Message message = new Message(data.getJSONObject(i));
                    //调用推送
                    //写入数据库
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }*/


}
