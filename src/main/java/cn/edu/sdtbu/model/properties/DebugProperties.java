package cn.edu.sdtbu.model.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 20:09
 * @deprecated
 */
@Getter
@Setter
//@Configuration
//@ConfigurationProperties(prefix = "online-judge.debug")
public class DebugProperties {
    private Boolean timeCost = false;
}