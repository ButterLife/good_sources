package cn.zhuchuangsoft.footstone.entity.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
public class Line {
    @JsonProperty("$id")
    private String $id;
    @JsonProperty("LineID")
    private String lineID;
    @JsonProperty("LineNo")
    private Integer lineNo;
    @JsonProperty("Energy")
    private Double energy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
