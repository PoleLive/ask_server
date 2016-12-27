package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class RegisterController {
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/register"})
    @ResponseBody
    public String register(@RequestParam("username") String username, @RequestParam("password") String password){
        Map<String, Object> map = new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            return JsonUtil.getJSONString(-1, "用户名不能为空");
        }
        if(StringUtils.isBlank(password)){
            return JsonUtil.getJSONString(-1, "密码不能为空");
        }
        User tmp = userService.getUser(username);
        if(tmp != null){
            return JsonUtil.getJSONString(-1,"用户名已经存在");
        }
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHead_url(username);
        if(userService.addUser(user) > 0){
            return JsonUtil.getJSONString(0,"注册成功");
        }
        return JsonUtil.getJSONString(-1, "注册失败");

    }
}
