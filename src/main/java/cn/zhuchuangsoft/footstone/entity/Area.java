package cn.zhuchuangsoft.footstone.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 阿白
 * @date 2019-12-21
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class Area extends BaseEntity {

    private String item_code;
    private String item_name;
}


