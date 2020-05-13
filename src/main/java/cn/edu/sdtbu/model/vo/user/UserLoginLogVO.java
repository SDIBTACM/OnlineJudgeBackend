package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
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