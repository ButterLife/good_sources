package cn.zhuchuangsoft.footstone.entity;

import lombok.Data;

import java.util.Date;

@Data
public class QueryParameters {

    private String ishandle;
    private String startTime;
    private String endTime;
    private String type;
    private String state;
    private Integer page = 1;
    private Integer limit = 20;
    private String key;


}
