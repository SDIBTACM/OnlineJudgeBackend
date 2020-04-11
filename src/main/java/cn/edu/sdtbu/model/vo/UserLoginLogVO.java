package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:48
 */
@Data
public class UserLoginLogVO {
    UserEntity userEntity;
    Page<LoginLogEntity> logEntityPage;
}
