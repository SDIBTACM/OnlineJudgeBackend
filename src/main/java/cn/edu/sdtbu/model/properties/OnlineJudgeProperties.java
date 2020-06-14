package cn.edu.sdtbu.model.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-26 16:45
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "online-judge")
public class OnlineJudgeProperties {
    private String url = "http://localhost:8080";

    @NestedConfigurationProperty
    private CacheProperties cache = new CacheProperties();

    @NestedConfigurationProperty
    private MailProperties  mail  = new MailProperties();
    @NestedConfigurationProperty
    private DebugProperties debug = new DebugProperties();
}
