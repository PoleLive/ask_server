package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.LoginTicket;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
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
        String ticket  = userService.addTicket(user.getId());

        map.put("msg","登录成功");
        map.put("data",user);
        map.put("ticket",ticket);
        return JsonUtil.getJSONString(0, map);
        //return JSON.toJSONString(user);
    }
    @RequestMapping(path = {"/test"},method = {RequestMethod.POST})
    @ResponseBody
    public String test(){
        //logger.error("test...............");
        return JsonUtil.getJSONString(0,"已经登录");
//        User user = userService.getUserByTicket(ticket);
//        if(user != null){
//            return JsonUtil.getJSONString(0,"已经登录");
//        }
//        return JsonUtil.getJSONString(-1,"还没有登录");
    }

}
