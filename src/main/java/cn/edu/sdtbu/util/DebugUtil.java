package cn.edu.sdtbu.util;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.service.base.BaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-28 14:34
 */
@Slf4j
public class DebugUtil {
    private static final String ALL_CLASS_NAME = "class_pkg";
    private static final String ALL_DATA = "all-data";
    private static final String DATA = "data";
    private static final String DATA_FILE = "classpath:DebugData";


    public static void generatorDebugData(HashMap<Class<?>, BaseService<?, ?>> map) throws IOException {
        // load debug data from DATA_FILE
        String json = loadDataFile();

        // parse JSON string and save to database
        JSONArray array = JSON.parseObject(json).getJSONArray(ALL_DATA);
        for (int i = 0;i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            List lst;
            Class<?> clazz;
            // try parse
            try {
                clazz = Class.forName(object.getString(ALL_CLASS_NAME));
                lst = JSON.parseArray(object.getString(DATA), clazz);
            } catch (Exception ignore) {
                continue;
            }
            // if entity is UserEntity, encode user's password
            if (clazz.getName().equals(UserEntity.class.getName())) {
                lst.forEach(o -> ((UserEntity)o).setPassword(
                    BCrypt.hashpw(((UserEntity)o).getPassword(), BCrypt.gensalt()))
                );
            }

            // save to database
            if (CollectionUtils.isNotEmpty(lst)) {
                map.get(lst.get(0).getClass()).saveAll(lst);
            }
            log.info("debug data generator success, class -> [{}], data -> {}",
                clazz.getName(), JSON.toJSONString(lst));
        }
    }

    private static String loadDataFile() throws IOException {
        File file = ResourceUtils.getFile(DATA_FILE);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // 构建 InputStreamReader 对象，编码与写入相同
        StringBuilder sb = new StringBuilder();
        while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成 char 加到 StringBuffer 对象中
        }
        return sb.toString();
    }
}
