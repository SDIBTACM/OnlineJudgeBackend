package cn.edu.sdtbu.listener;

import cn.edu.sdtbu.model.properties.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * after application start need to do
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 08:45
 */

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Online Judge V{} start success, click url to view document {}",
                Const.ONLINE_JUDGE_VERSION,
                AnsiOutput.toString(AnsiColor.BLUE, "http://localhost:8080/swagger-ui.html"));
    }
}
