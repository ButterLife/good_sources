package cn.zhuchuangsoft.footstone.config;

import cn.zhuchuangsoft.footstone.entity.Payload;
import cn.zhuchuangsoft.footstone.entity.User;
import cn.zhuchuangsoft.footstone.utils.JwtUtils;
import cn.zhuchuangsoft.footstone.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties("rsa.key")
public class RsaKeyProperties {

    private String pubKeyFile;
    private String priKeyFile;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void createRsaKey() {
        try {
            publicKey = RsaUtils.getPublicKey(pubKeyFile);
            privateKey = RsaUtils.getPrivateKey(priKeyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsernameByToken(HttpServletRequest request) throws Exception {
        String header = request.getHeader("Authorization");
        String token = header.replace("Bearer ", "");
        Payload<User> payload = JwtUtils.getInfoFromToken(token, publicKey, User.class);
        return payload.getUserInfo().getUsername();
    }


}

