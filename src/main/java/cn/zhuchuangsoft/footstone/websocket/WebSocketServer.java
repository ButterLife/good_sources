package cn.zhuchuangsoft.footstone.websocket;

import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.service.IDeviceService;
import cn.zhuchuangsoft.footstone.service.IUserAndDeviceService;
import cn.zhuchuangsoft.footstone.service.IUserService;
import cn.zhuchuangsoft.footstone.service.IWarmingService;
import cn.zhuchuangsoft.footstone.utils.JsonUtils;
import cn.zhuchuangsoft.footstone.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 */
@ServerEndpoint("/imserver/{userName}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。(用HashMap >> list<websocket> 暂定同用户多地点登录能同时收到推送的消息  )
     */
    private static ConcurrentHashMap<String, List<WebSocketServer>> webSocketMap = new ConcurrentHashMap<>();
    @Autowired
    IWarmingService warmingServiceImpl = (IWarmingService) SpringUtil.getBean("warmingServiceImpl");
    @Autowired
    IUserAndDeviceService userAndDeviceServiceImpl = (IUserAndDeviceService) SpringUtil.getBean("userAndDeviceServiceImpl");
    @Autowired
    IUserService userServiceImpl = (IUserService) SpringUtil.getBean("userServiceImpl");
    @Autowired
    IDeviceService deviceServiceImpl = (IDeviceService) SpringUtil.getBean("deviceServiceImpl");
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userName = "";


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userName") String userName) throws IOException {

        if (webSocketMap.containsKey(userName) && webSocketMap.get(userName) != null && webSocketMap.get(userName).size() != 0) {
            for (WebSocketServer server : webSocketMap.get(userName)) {
                server.sendMessage(message);
            }
        } else {
            log.error("用户" + userName + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    /**
     * 连接建立成功，并推送未处理的警告 故障。
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        this.session = session;
        this.userName = userName;
        // 判断用户是否已经其他地方登陆
        if (webSocketMap.containsKey(userName)) {
            webSocketMap.get(userName).add(this);
            addOnlineCount();
        } else {
            List<WebSocketServer> webSocketServers = new ArrayList<WebSocketServer>();
            webSocketServers.add(this);
            webSocketMap.put(userName, webSocketServers);
            //在线数加1
            addOnlineCount();
        }
        log.info("用户连接:" + userName + ",当前在线人数为:" + getOnlineCount());
        try {
//            sendMessage("连接成功,userName:"+userName);
            // 推送未处理的warming
            String userCode = userServiceImpl.selectUserCode(userName);
            if (userCode != null) {
                List<String> deivceCodes = userAndDeviceServiceImpl.selectDeviceCodeByUserCode(userCode);
                if (deivceCodes.size() > 0) {
//                    List<Warming> warmings = new ArrayList<>();
                    for (String deviceCode : deivceCodes) {
                        List<Warming> warmings1 = warmingServiceImpl.selectWarmingIshandle(deviceCode);
                        if (warmings1.size() == 0) {
                            continue;
                        }
                        for (Warming warming : warmings1) {

                            String warmingMsg = warming.getWarmingMsg();
                            //处理强调前端的数据
                            String code = warming.getWarmingCode();
                            String deviceName = deviceServiceImpl.selectDeviceName(deviceCode);

                            warmingMsg = warmingMsg.replace(deviceName, "<span style=font-size:16px;color:red;font-weight:bold>" + deviceName + "</span>");

                            warmingMsg = warmingMsg.replace(code, "<span style=font-size:16px;color:red;font-weight:bold>" + code + "</span>");

                            //将这个warming的数据推送给前端
                            warming.setWarmingMsg(warmingMsg);

                            sendMessage(JsonUtils.toString(warming));
                            Thread.currentThread().sleep(10);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("用户:" + userName + ",网络异常!!!!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userName)) {
            // 判断是否是此用户最后一个在线的webSocker
            if (webSocketMap.get(userName).size() <= 1) {
                webSocketMap.remove(userName);
            } else {
                webSocketMap.get(userName).remove(this);
            }

            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userName + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 处理警告已受理（ishandler）：传一个warmingJson对象过来。(目前只有一个功能)
     * 需要handleMsg 属性 进行赋值：对应有两种处理方式 已处理 && 忽略
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        Warming warming = JsonUtils.toBean(message, Warming.class);
//        int info = warmingServiceImpl.updateIshandel(warming);
//
//        if (info <= 0) {
//            try {
//                sendMessage("处理失败");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        log.info("用户受理:" + warming.getWarmingMsg() + "，处理方式" + warming.getHandleMsg());
        log.info("前端推送数据：ID：" + message);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        log.info("用户名:" + this.userName + ",推送消息：" + message);
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userName + ",原因:" + error.getMessage());
        error.printStackTrace();
    }
}
