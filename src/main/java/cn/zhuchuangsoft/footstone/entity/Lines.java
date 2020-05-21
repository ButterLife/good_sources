package cn.zhuchuangsoft.footstone.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 控制线路开关的包装类
 */
@Data
public class Lines extends BaseEntity {

    private static final long serialVersionUID = 3891370294509793362L;
    @ApiModelProperty("线路编号")
    @JSONField(name = "LineNo")
    private Integer LineNo;
    @ApiModelProperty("线路开关 0:关 1:开")
    @JSONField(name = "Status")
    private Integer Status;

    public Lines(Integer lineNo, Integer status) {
        LineNo = lineNo;
        Status = status;
    }

}
