package cn.edu.sdtbu.model.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.nio.channels.Selector;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 20:09
 * @deprecated
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "online-judge.debug")
public class DebugProperties {
    private Boolean timeCost = false;
    public Boolean getTimeCost(){
        return timeCost;
    }
}