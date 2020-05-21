package cn.zhuchuangsoft.footstone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jrs = new Jackson2JsonRedisSerializer(Object.class);
        StringRedisSerializer srs = new StringRedisSerializer();
        jrs.setObjectMapper(objectMapper);
        template.setKeySerializer(srs);
        template.setValueSerializer(jrs);
        template.setHashKeySerializer(srs);
        template.setHashValueSerializer(jrs);
        template.setEnableDefaultSerializer(true);
        template.setDefaultSerializer(jrs);
        return template;
    }


}
