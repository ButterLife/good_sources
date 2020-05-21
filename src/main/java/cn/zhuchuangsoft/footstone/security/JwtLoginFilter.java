package cn.zhuchuangsoft.footstone.security;

import cn.zhuchuangsoft.footstone.config.RsaKeyProperties;
import cn.zhuchuangsoft.footstone.controller.BaseController;
import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.utils.JwtUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录成功失败的业务逻辑
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private RsaKeyProperties prop;

    public JwtLoginFilter() {
    }

    public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop) {
        this.authenticationManager = authenticationManager;
        this.prop = prop;
    }


    /**
     * 登录接口
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            //User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return this.authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                Map resultMap = new HashMap<>();
                resultMap.put("code", BaseController.FAILED);
                resultMap.put("msg", "用户名或密码错误");
                out.write(new ObjectMapper().writeValueAsString(resultMap));
                out.flush();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // throw new RuntimeException(e);
            return null;
        }
    }

    /**
     * 登录成功响应
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = new User();
        user.setUsername(authResult.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        String principal = objectMapper.writeValueAsString(authResult.getPrincipal());
        user = objectMapper.readValue(principal, User.class);
        user.setRoles((List<Role>) authResult.getAuthorities());
        System.out.println("---------user-------------------" + user);

        String token = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 24 * 60 * 3);
        response.addHeader("Authorization", "Bearer " + token);
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            Map resultMap = new HashMap<>();
            resultMap.put("code", BaseController.SUCCESS);
            resultMap.put("msg", "认证通过");
            resultMap.put("data", "Bearer " + token);
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
