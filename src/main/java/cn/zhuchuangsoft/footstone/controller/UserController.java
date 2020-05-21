package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.LoginFormParams;
import cn.zhuchuangsoft.footstone.entity.ShortMessage;
import cn.zhuchuangsoft.footstone.entity.device.Device1;
import cn.zhuchuangsoft.footstone.entity.user.User;
import cn.zhuchuangsoft.footstone.entity.warming.Warming;
import cn.zhuchuangsoft.footstone.mappers.MessageMapper;
import cn.zhuchuangsoft.footstone.mappers.ZDeviceWarmingMapper;
import cn.zhuchuangsoft.footstone.service.IDeviceMessageService;
import cn.zhuchuangsoft.footstone.service.IMessageService;
import cn.zhuchuangsoft.footstone.service.IUserService;
import cn.zhuchuangsoft.footstone.service.IZDeviceWarmingService;
import cn.zhuchuangsoft.footstone.utils.JiGuangApiMethod;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import cn.zhuchuangsoft.footstone.utils.RedisUtils;
import cn.zhuchuangsoft.footstone.utils.RegexUtils;
import cn.zhuchuangsoft.footstone.websocket.WebSocketServer;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户处理
 */
@RestController
@Slf4j
public class UserController extends BaseController {
    //验证码多久有效 分钟
    private final Integer SMS_TIME = 5;
    //允许用户发送一天发送的次数
    private final Integer SMS_SEND_COUNT = 15;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeviceMessageService deviceMessageService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void setJwtVerityFilter() {

    }

    @PostMapping("/guest/select")
    @ApiOperation("查询用户名是否重复")
    public JsonResult<Void> selectUser(@ApiParam("用户名") String username) {
        if (StringUtils.isEmpty(username)) {
            return new JsonResult<>(FAILED, "用户名为空", null);
        }
        if (userService.judgeUsername(username)) {
            return new JsonResult<>(FAILED, "用户名重复", null);
        }
        return new JsonResult<>(SUCCESS);
    }

    @PostMapping("/guest/add")
    @ApiOperation("添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "msgId", value = "调用发送验证码返回的msgId", required = true, paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "form"),
    })
    public JsonResult addUser(@Validated LoginFormParams formParams, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 如果参数有错误的话
            // 获取第一个错误!
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new JsonResult<LoginFormParams>(FAILED, errorMsg, formParams);
        }

        if (userService.judgeUsername(formParams.getUsername())) {
            return new JsonResult<LoginFormParams>(FAILED, "用户名重复", formParams);
        }

        if (!JiGuangApiMethod.codeCallback(formParams.getMsgId(), formParams.getCode())) {
            return new JsonResult<LoginFormParams>(FAILED, "验证码错误", formParams);
        }
        User user = new User(formParams.getUsername(), formParams.getPassword(), formParams.getMobile(), formParams.getRoleId());
        userService.increaseUser(user);
        return new JsonResult<>(SUCCESS);
    }

    @Secured("ROLE_USER")
    @PostMapping("/user/modify/password")
    @ApiOperation("修改密码")
    public JsonResult<Void> modifyPassword(HttpServletRequest request, @ApiParam("旧密码") String oldPassword, @ApiParam("新密码") String newPassword) {
        if (StringUtils.isEmpty(oldPassword)) {
            return new JsonResult<>(FAILED, "请填写旧密码", null);
        }
        if (StringUtils.isEmpty(newPassword)) {
            return new JsonResult<>(FAILED, "请填写新密码", null);
        }
        try {
            String username = getProp().getUsernameByToken(request);
            User user = userService.selectUsername(username);
            if (!userService.verifyPassword(oldPassword, user)) {
                return new JsonResult<>(FAILED, "旧密码错误", null);
            }
            user.setUpdateTime(new Date());
            user.setPassword(newPassword);
            userService.updatePassword(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonResult<>(SUCCESS);
    }

    @PostMapping("/guest/retrieve")
    @ApiOperation("找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "msgId", value = "调用发送验证码返回的msgId", required = true, paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "form")
    })
    public JsonResult<LoginFormParams> retrievePassword(LoginFormParams formParams) {
        if (StringUtils.isEmpty(formParams.getPassword())) {
            return new JsonResult<LoginFormParams>(FAILED, "请填写密码", formParams);
        }
        if (StringUtils.isEmpty(formParams.getMobile())) {
            return new JsonResult<LoginFormParams>(FAILED, "请填手机号", formParams);
        }
        if (!RegexUtils.checkMobile(formParams.getMobile())) {
            return new JsonResult<LoginFormParams>(FAILED, "手机号格式错误", formParams);
        }
        if (StringUtils.isEmpty(formParams.getCode())) {
            return new JsonResult<LoginFormParams>(FAILED, "请填验证码", formParams);
        }
        if (!userService.judgeMobile(formParams.getMobile())) {
            return new JsonResult<LoginFormParams>(FAILED, "该手机号未注册", formParams);
        }
        if (!JiGuangApiMethod.codeCallback(formParams.getMsgId(), formParams.getCode())) {
            return new JsonResult<LoginFormParams>(FAILED, "验证码错误", formParams);
        }
        User user = new User(formParams.getPassword(), formParams.getMobile());
        userService.updatePassword(user);
        return new JsonResult<>(SUCCESS);
    }

    @PostMapping("/guest/sms")
    @ApiOperation("根据手机号发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "isRetrieve", value = "是否是找回密码", required = false, paramType = "form")
    })
    public JsonResult<String> sendCode(String mobile, boolean isRetrieve) {
        if (StringUtils.isEmpty(mobile)) {
            return new JsonResult<>(FAILED, "手机号不能为空", null);
        }
        if (!RegexUtils.checkMobile(mobile)) {
            return new JsonResult<>(FAILED, "手机号格式错误", null);
        }
        boolean b = userService.judgeMobile(mobile);
        if (isRetrieve && !b) {
            return new JsonResult<>(FAILED, "此手机号用户不存在", null);
        }
        if (!isRetrieve && b) {
            return new JsonResult<>(FAILED, "手机号已注册", null);
        }
        //防止短信验证码接口被恶意调用
        String mobileKey = "mobile_" + mobile;
        String todayKey = "today_mobile_code_times_" + mobile;
        // 验证码5分钟内有效
        long times = redisUtils.getExpire(mobileKey);
        if (times > 0) {
            return new JsonResult<>(FAILED, "验证码已发送,稍后再尝试获取", null);
        }
        // 判断当前手机号今天发送密码次数是否已达上线,每天15条（具体条数根据自己的需求调用）
        Object todayTimes = redisUtils.get(todayKey);
        int todayCount = 1;
        if (todayTimes != null) {
            todayCount = new Integer(todayTimes.toString());
            if (todayCount >= SMS_SEND_COUNT) {
                //此时还可以记录当前用户的手机号，ip，调用的短信验证码类型到表中，方便系统记录与分析。系统可以分析该用户该周该月调用短信接口的次数
                //由此来分析该ip的用户是否是正常的用户，如果调用太频繁，比如连续一周或数周都在调用该接口，系统可以暂时禁用该ip发来的请求，或者降低该手机号获取短信验证码的次数
                return new JsonResult<>(FAILED, "当前手机号今日发送验证码已达上限，请明日再来", null);
            }
            todayCount++;
        }
        String msgId = JiGuangApiMethod.sendCode(mobile);
        if (StringUtils.isNotEmpty(msgId)) {
            // 保存验证码到redis
            redisUtils.set(mobileKey, msgId, 60 * SMS_TIME + 5);//redis中的code比实际要多5秒
            // 记录本号码发送验证码次数
            redisUtils.set(todayKey, todayCount + "", redisUtils.getSurplusTime());
            //记录短信
            ShortMessage message = new ShortMessage();
            message.setMobile(mobile);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.setSendTime(format.format(new Date()));
            message.setSendType("用户注册验证码");
            messageService.insertShortMessage(message);
        }


        return new JsonResult<>(SUCCESS, msgId);
    }


    @PostMapping("info/change")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {

        // 从Session中获取uid和username
        Integer id = (Integer) session.getAttribute("id");
        String username = (String) session.getAttribute("username");
        // 调用userService.changeInfo()修改个人资料
        userService.changeInfo(username, user);
        // 响应成功
        return new JsonResult<>(SUCCESS);
    }


}