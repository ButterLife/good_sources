package cn.zhuchuangsoft.footstone.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2564072665395164117L;
    private Integer code;
    private String msg;
    private Integer count;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer pages;
    private T data;

    public JsonResult() {
    }

    public JsonResult(Integer state) {
        super();
        this.code = state;
    }

    public JsonResult(Throwable e) {
        super();
        this.msg = e.getMessage();
    }

    public JsonResult(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public JsonResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(Integer code, Integer count, T data) {
        this.code = code;
        this.count = count;
        this.data = data;
    }

    public JsonResult(Integer code, T data, Integer count, Integer pageSize, Integer pageIndex, Integer pages) {
        this.code = code;
        this.count = count;
        this.data = data;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.pages = pages;
    }


}
