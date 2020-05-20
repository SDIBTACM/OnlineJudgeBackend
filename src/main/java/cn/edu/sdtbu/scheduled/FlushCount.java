package cn.edu.sdtbu.scheduled;

import cn.edu.sdtbu.service.CountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 16:21
 */
@Component
@Slf4j
public class FlushCount {
    @Resource
    CountService countService;
    /**
     * A cron-like expression, extending the usual UN*X definition to include triggers
     * on the second, minute, hour, day of month, month, and day of week.
     * <p>For example, {@code "0 * * * * MON-FRI"} means once per minute on weekdays
     * (at the top of the minute - the 0th second).
     * <p>The fields read from left to right are interpreted as follows.
     * <ul>
     * <li>second</li>
     * <li>minute</li>
     * <li>hour</li>
     * <li>day of month</li>
     * <li>month</li>
     * <li>day of week( 1~7, 1=SUN or SUN，MON，TUE，WED，THU，FRI，SAT） )</li>
     * </ul>
     */
    AtomicLong atomicLong = new AtomicLong(1);
    @Scheduled(cron = "0 */10 * * * ?")
    public void refreshResultCount() {
        //every 3 minutes flush user's solution count info and id ++
        if (countService.refreshJudgeResultByUserId(atomicLong.getAndIncrement(), true)) {
            atomicLong.set(1);
        }
    }
}
