package cn.edu.sdtbu.model.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 22:12
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private Integer port          = null;
    private String  host          = null;
    private Integer maxTotal      = Runtime.getRuntime().availableProcessors() * 2;
    private Integer maxIdle       = Runtime.getRuntime().availableProcessors() * 2;
    private Integer minIdle       = 4;
    private Integer maxWaitMillis = 1000;
    private String  password      = null;
}
