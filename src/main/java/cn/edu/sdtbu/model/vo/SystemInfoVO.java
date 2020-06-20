package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-20 16:55
 */
@Data
public class SystemInfoVO {
    @ApiModelProperty(notes = "使用的缓存类型")
    CacheStoreType cache;


}
