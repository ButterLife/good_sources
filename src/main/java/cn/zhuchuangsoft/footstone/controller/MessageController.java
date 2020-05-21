package cn.zhuchuangsoft.footstone.controller;

import cn.zhuchuangsoft.footstone.entity.QueryParameters;
import cn.zhuchuangsoft.footstone.service.IMessageService;
import cn.zhuchuangsoft.footstone.utils.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MessageController extends BaseController {
    @Autowired
    private IMessageService messageService;

    @GetMapping("/message")
    public JsonResult<List<Map<String, Object>>> a(QueryParameters queryParameters) {
        try {
            PageHelper.startPage(queryParameters.getPage(), queryParameters.getLimit());
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(messageService.getMessageListByQuery(queryParameters));
            return new JsonResult<List<Map<String, Object>>>(SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), pageInfo.getPages());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult<>(FAILED, "查询失败", null);
    }
}
