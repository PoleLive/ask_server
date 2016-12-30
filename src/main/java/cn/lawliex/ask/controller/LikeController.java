package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.LikeService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JedisAdapter;
import cn.lawliex.ask.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Terence on 2016/12/30.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/like/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId,@RequestParam("ticket")String ticket){
        User user = userService.getUserByTicket(ticket);

        Map<String,Object> map = new HashMap<>();
        long count = likeService.like(entityType,entityId,user.getId());
        map.put("count",count);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }
    @RequestMapping(path = {"/like/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId,@RequestParam("ticket")String ticket){
        User user = userService.getUserByTicket(ticket);

        Map<String,Object> map = new HashMap<>();
        long count = likeService.disLike(entityType,entityId,user.getId());
        map.put("count",count);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }
    @RequestMapping(path = {"/like/count"},method = {RequestMethod.POST})
    @ResponseBody
    public String likeCount(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId,@RequestParam("ticket")String ticket){
        Map<String,Object> map = new HashMap<>();
        long count = likeService.getLikeCount(entityType,entityId);
        map.put("count",count);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }
}
