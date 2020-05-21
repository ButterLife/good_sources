package cn.zhuchuangsoft.footstone.security;

import cn.zhuchuangsoft.footstone.config.RsaKeyProperties;
import cn.zhuchuangsoft.footstone.constants.Constants;
import cn.zhuchuangsoft.footstone.controller.BaseController;
import cn.zhuchuangsoft.footstone.entity.Payload;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.utils.JwtUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtVerityFilter extends BasicAuthenticationFilter {

    private RsaKeyProperties prop;

    public JwtVerityFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop) {
        super(authenticationManager);
        this.prop = prop;
    }


    /**
     * 无token返回错误信息
     *
     * @param response
     * @throws IOException
     */
    private static void responseErro(HttpServletResponse response, String msg) throws IOException {
        //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        Map resultMap = new HashMap<>();
        resultMap.put("code", BaseController.TOKEN_EXPIRED);
        resultMap.put("msg", msg);
        String jsonString = JSON.toJSONString(resultMap);
        out.write(jsonString);
        out.flush();
        out.close();
    }

    /**
     * 需要权限的接口验证token
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //配置不需要拦截的地址
//        if(request.getRequestURI().contains(Constants.LAN_URL_GUEST_SMS)
//                ||request.getRequestURI().contains(Constants.LAN_URL_GUEST_ADD)
//                ||request.getRequestURI().contains(Constants.LAN_URL_GUEST_SELECT)
//                ||request.getRequestURI().contains(Constants.LAN_URL_GUEST_RETIEVE)){
//            chain.doFilter(request, response);
//            return;
//        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            //如果没有token
            responseErro(response, "无权访问");

        } else {
            String token = header.replace("Bearer ", "");
            //验证token是否合法
            try {
                Payload<User> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), User.class);
                User user = payload.getUserInfo();
                if (user != null) {
                    UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getRoles());
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    request.setAttribute("user", user);
                    chain.doFilter(request, response);
                }
            } catch (Exception e) {
                responseErro(response, "请重新登录");
                //responseErro(response);
            }
        }
    }


}
