package cn.edu.sdtbu.listener;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.BaseEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.service.base.BaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
    @Resource
    CacheHandler handler;
    @Resource
    OnlineJudgeProperties properties;
    @Resource
    ApplicationContext context;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        try {
            init();
        } catch (Exception ignore) {
            log.error(ignore.getMessage());
        }
        log.info("Online Judge V{} start success, click url to view document {}",
                Const.ONLINE_JUDGE_VERSION,
                AnsiOutput.toString(AnsiColor.BLUE, "http://localhost:8080/swagger-ui.html"));
    }

    @SuppressWarnings("unchecked")
    private void init() throws IOException, ClassNotFoundException {
        String allClassName = "class_pkg";
        String datas = "datas";
        String data = "data";

        // init cache
        handler.setStrategy(properties.getCacheStoreType());

        // init debug data
        if (properties.getDebug().getGeneratorData()){
            HashMap<Class<?>, BaseService<?, ?>> map = new HashMap<>(16);
            for (BaseService<?, ?> service : context.getBeansOfType(BaseService.class).values()){
                map.put(service.getTemplateType(), service);
            }
            File file = ResourceUtils.getFile("classpath:DebugData");
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            // 构建 InputStreamReader 对象，编码与写入相同
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
                sb.append((char) reader.read());
                // 转成 char 加到 StringBuffer 对象中
            }
            JSONArray array = JSON.parseObject(sb.toString()).getJSONArray(datas);
            for (int i=0;i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                Class<?> clazz = Class.forName(object.getString(allClassName));

                List lst = JSON.parseArray(object.getString(data), clazz);
                if (clazz.getName().equals(UserEntity.class.getName())){
                    lst.forEach(o -> ((UserEntity)o).setPassword(
                        BCrypt.hashpw(((UserEntity)o).getPassword(), BCrypt.gensalt()))
                    );
                }
                if (CollectionUtils.isNotEmpty(lst)) {
                    map.get(lst.get(0).getClass()).saveAll(lst);
                }
                log.info(lst.toString());
            }
        }
    }
}

