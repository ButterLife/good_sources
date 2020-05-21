package cn.zhuchuangsoft.footstone.advice;

import cn.zhuchuangsoft.footstone.controller.BaseController;
import cn.zhuchuangsoft.footstone.controller.ex.ControlUnsuccessfulException;
import cn.zhuchuangsoft.footstone.controller.ex.ControllerException;
import cn.zhuchuangsoft.footstone.controller.ex.ModificationNameException;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class HandlerControllerException {

    @ExceptionHandler({JSONException.class})
    public JsonResult<Void> handlerReturnException(Throwable e) {
        JsonResult<Void> jsonResult = new JsonResult<>(500);
        jsonResult.setMsg("程序异常,请联系服务商");
        return jsonResult;
    }

    @ExceptionHandler({AccessDeniedException.class})
    public JsonResult<Void> accessDeniedException(HttpServletResponse httpServletResponse, Throwable e) {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = httpServletResponse.getWriter();
            Map resultMap = new HashMap<>();
            resultMap.put("code", BaseController.NO_RIGHT_TO_VISIT);
            resultMap.put("msg", "无权限访问");
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return null;
    }

    @ExceptionHandler({ControllerException.class})
    public JsonResult<Void> handlerControllerException(Throwable e) {
        JsonResult<Void> jsonResult = new JsonResult<>(e);
        if (e instanceof ModificationNameException) {
            //修改名称异常
            jsonResult.setCode(400);
        } else if (e instanceof ControlUnsuccessfulException) {
            //控制失败
            jsonResult.setCode(401);
        } else {
            jsonResult.setCode(501);
        }
        return jsonResult;
    }

    @ExceptionHandler({Exception.class})
    public JsonResult<String> handlerException(HttpServletResponse httpServletResponse, Throwable e) {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = httpServletResponse.getWriter();
            Map resultMap = new HashMap<>();
            resultMap.put("code", 101);
            resultMap.put("msg", e.getMessage());
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
            e.printStackTrace();
        } catch (IOException ex) {
            log.info(e.getStackTrace().toString());
        }
        return null;
    }

}
