package cn.zhuchuangsoft.footstone;

import cn.zhuchuangsoft.footstone.config.RsaKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@MapperScan("cn.zhuchuangsoft.footstone.mappers")
@EnableCaching
@EnableSwagger2
@EnableScheduling
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAsync
public class FootstoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(FootstoneApplication.class, args);

    }
}
