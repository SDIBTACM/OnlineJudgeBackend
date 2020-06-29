package cn.edu.sdtbu.scheduled;

import cn.edu.sdtbu.model.enums.RankType;
import cn.edu.sdtbu.service.RefreshService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-09 17:05
 */
@Component
public class FlushRankList {
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
    @Resource
    RefreshService refreshService;
    @Scheduled(cron = "0 30 3 * * *")
    public void reloadDailyRankList() {
        for (RankType type : RankType.values()) {
            if (type != RankType.OVERALL) {
                refreshService.reloadRankList(type, false);
            }
        }
    }
}
