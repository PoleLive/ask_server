package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.User;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class LoginController {
    @RequestMapping(path = {"login"},method = {RequestMethod.GET})
    @ResponseBody
    private String login(String username,String password){
        User user = new User();
        user.setName("lawliex");
        user.setPassword("123");
        return JSON.toJSONString(user);
    }
}
