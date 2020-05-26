package cn.zhuchuangsoft.footstone.entity.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class Datum {
    @JsonProperty("$id")
    private String $id;
    @JsonProperty("EnergyID")
    private String energyID;
    @JsonProperty("ControllerID")
    private String controllerID;
    @JsonProperty("TimeStamp")
    private Integer timeStamp;
    @JsonProperty("Interval")
    private Object interval;
    @JsonProperty("Lines")
    private List<Line> lines = new ArrayList<Line>();
    @JsonProperty("AddTime")
    private String addTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
