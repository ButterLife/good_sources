package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.websocket.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class SocketController {
    public static void main(String[] args) {
        try {
            Date date = new Date();
            String str = "yyy-MM-dd HH:mm:ss";
            String time = "2020-05-18 17:36:02";
            SimpleDateFormat sdf = new SimpleDateFormat(str);
            Date dates = sdf.parse(time);
            System.out.println(date.getTime() < dates.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("page")
    public ModelAndView page() {
        return new ModelAndView("websocket");
    }

    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
