package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.service.CommentService;
import cn.lawliex.ask.service.FollowService;
import cn.lawliex.ask.service.QuestionService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Terence on 2016/12/30.
 */
@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/follow/followers"},method = {RequestMethod.POST})
    @ResponseBody
    public String getFollowers(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId){
        List<Integer> list = followService.getFollowers(entityType, entityId,0, 20);
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("msg","success");
        return JsonUtil.getJSONString(0,map);
    }
    @RequestMapping(path = {"/follow/follow"},method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId, @RequestParam("ticket")String ticket){

        if(followService.follow(userService.getUserByTicket(ticket).getId(),entityType,entityId)){
            return JsonUtil.getJSONString(0,"ok");
        }
        return JsonUtil.getJSONString(-1, "error");
    }
    @RequestMapping(path = {"/follow/unfollow"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId, @RequestParam("ticket")String ticket){

        if(followService.unfollow(userService.getUserByTicket(ticket).getId(),entityType,entityId)){
            return JsonUtil.getJSONString(0,"ok");
        }
        return JsonUtil.getJSONString(-1, "error");
    }
    @RequestMapping(path = {"/follow/isfollower"},method = {RequestMethod.POST})
    @ResponseBody
    public String isFollower(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId, @RequestParam("ticket")String ticket){
        if(followService.isFollower(userService.getUserByTicket(ticket).getId(),entityType,entityId)){
            return JsonUtil.getJSONString(0,"yes");
        }
        return JsonUtil.getJSONString(-1,"no");
    }
    @RequestMapping(path = {"/follow/isfollowered"},method = {RequestMethod.POST})
    @ResponseBody
    public String isFollowered(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId, @RequestParam("ticket")String ticket){
        int userId = userService.getUserByTicket(ticket).getId();
        boolean isFollower = followService.isFollower(userId,entityType,entityId);
        if(isFollower && followService.unfollow(userId,entityType,entityId)){
            return JsonUtil.getJSONString(0,"yes, but now no");
        }else if(!isFollower && followService.follow(userId,entityType,entityId)){
            return JsonUtil.getJSONString(1, "no, but now yes");
        }
        return JsonUtil.getJSONString(-1,"error");
    }
}
