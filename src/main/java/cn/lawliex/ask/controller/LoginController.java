package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @RequestMapping(path = {"login"},method = {RequestMethod.POST})
    @ResponseBody
    private String login(String username,String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            //map.put("msg","用户名不能为空");
            String msg = "用户名不能为空";
            return JsonUtil.getJSONString(-1,msg);
        }else if(StringUtils.isBlank(password)){
            String msg = "密码不能为空";
            return JsonUtil.getJSONString(-1, msg);
        }

        User user = userService.getUser(username);

        if(user == null){
            String msg = "用户名不存在";
            return JsonUtil.getJSONString(-1,msg);
        }
        if(!user.getPassword().equals(password)){
            String msg = "用户名或者用户密码错误";
            return JsonUtil.getJSONString(-1, msg);
        }

        map.put("user",user);

        return JsonUtil.getJSONString(0, map);
        //return JSON.toJSONString(user);
    }
}
