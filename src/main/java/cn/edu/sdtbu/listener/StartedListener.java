package cn.edu.sdtbu.listener;

import cn.edu.sdtbu.debug.DebugUtil;
import cn.edu.sdtbu.debug.GeneratorFakeData;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.service.RefreshService;
import cn.edu.sdtbu.service.base.BaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * after application start need to do
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 08:45
 */

@Slf4j
@Configuration
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Resource
    CacheHandler handler;
    @Resource
    RefreshService refreshService;
    @Resource
    OnlineJudgeProperties properties;
    @Resource
    ApplicationContext context;
    @Resource
    GeneratorFakeData generatorFakeData;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        init();
        log.info("Online Judge V{} start success, click url to view [ swagger ] document {}",
                Const.ONLINE_JUDGE_VERSION,
                AnsiOutput.toString(AnsiColor.BLUE, "http://localhost:8080/swagger-ui.html"));

        log.info("Online Judge V{} start success, click url to view [ knife4j ] document {}",
            Const.ONLINE_JUDGE_VERSION,
            AnsiOutput.toString(AnsiColor.BLUE, "http://localhost:8080/doc.html"));
    }

    @SuppressWarnings("unchecked")
    private void init() {
        // init cache
        handler.init(context, properties.getCacheStoreType());

        // init debug data
        if (properties.getDebug().getGeneratorData()) {
            // get all BaseService impl and save to map.
            HashMap<Class<?>, BaseService<?, ?>> map = new HashMap<>(16);
            context.getBeansOfType(BaseService.class).values().forEach(o -> map.put(o.getTemplateType(), o));
            try {
                generatorDebugData(map);
            } catch (IOException ignore) { }
        }
        if (properties.getDebug().getRefreshAllProblemSolutionCount()) {
            refreshService.refreshSolutionCount(null);
        }
        //refresh rank list
        if (properties.getDebug().getRefreshRankList()) {
            refreshService.refreshRankList(true);
        }
        // init rank list

    }

    private void generatorDebugData(HashMap<Class<?>, BaseService<?, ?>> map) throws IOException {
        String json = DebugUtil.loadDebugData();

        // generator all fake data
        generatorFakeData.generatorAll(105);

        /**************************************** virtual rank list generator finished **********************************************/
        // parse JSON string and save to database
        JSONArray array = JSON.parseObject(json).getJSONArray(DebugUtil.ALL_DATA);
        for (int i = 0;i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            List lst;
            Class<?> clazz;
            // try parse
            try {
                clazz = Class.forName(object.getString(DebugUtil.ALL_CLASS_NAME));
                lst = JSON.parseArray(object.getString(DebugUtil.DATA), clazz);
            } catch (Exception ignore) {
                continue;
            }
            // if entity is UserEntity, encode user's password
            // if entity is other entity, fetch default fields
            DebugUtil.defaultEntityFieldGenerator(lst);

            // save to database
            if (CollectionUtils.isNotEmpty(lst)) {
                map.get(lst.get(0).getClass()).saveAll(lst);
            }
            log.info("debug data generator success, class -> [{}], data -> {}",
                clazz.getName(), JSON.toJSONString(lst));
        }
    }
}

