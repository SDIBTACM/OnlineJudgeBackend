package cn.edu.sdtbu.debug;

import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-28 14:34
 */
@Slf4j
public class DebugUtil {
    public static final String ALL_CLASS_NAME = "class_pkg";
    public static final String ALL_DATA       = "all-data";
    public static final String DATA           = "data";
    public static final String DATA_FILE      = "classpath:DebugData";


    public static String loadDebugData() throws IOException {
        File              file        = ResourceUtils.getFile(DATA_FILE);
        InputStream       inputStream = new FileInputStream(file);
        InputStreamReader reader      = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        // 构建 InputStreamReader 对象，编码与写入相同
        StringBuilder sb = new StringBuilder();
        while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成 char 加到 StringBuffer 对象中
        }
        return sb.toString();
    }

    public static void defaultEntityFieldGenerator(List<Object> target) {
        if (CollectionUtils.isEmpty(target)) {
            return;
        }
        if (target.get(0) instanceof UserEntity) {
            target.forEach(o -> {
                UserEntity user = (UserEntity) o;
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                user.setDeleteAt(OnlineJudgeConstant.TIME_ZERO);
            });
        } else if (target.get(0) instanceof ProblemEntity) {
            ProblemEntity buffer = new ProblemParam().transformToEntity();
            target.forEach(o -> SpringUtil.cloneWithoutNullVal(buffer, o));
        }
    }
}
