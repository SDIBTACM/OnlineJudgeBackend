package cn.edu.sdtbu.model.properties;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    private CacheStoreType cacheStoreType = CacheStoreType.DEFAULT;
    private String url = "https://acm.sdtbu.edu.cn";
    private MailProperties mail = new MailProperties();
}
