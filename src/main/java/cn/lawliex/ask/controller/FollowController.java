package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
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

import java.util.ArrayList;
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
        List<User> users = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        for(int i = 0; i < list.size();i++){
            User user = userService.getUser(list.get(i));
            users.add(user);
        }
        map.put("list",users);
        map.put("msg","success");
        return JsonUtil.getJSONString(0,map);
    }

    @RequestMapping(path = {"/follow/followees"},method = {RequestMethod.POST})
    @ResponseBody
    public String getFollowees(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId){
        List<Integer> list = followService.getFollowees(entityId, entityType,0, 20);
        List<User> users = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        for(int i = 0; i < list.size();i++){
            User user = userService.getUser(list.get(i));
            users.add(user);
        }
        map.put("list",users);
        map.put("msg","success");
        return JsonUtil.getJSONString(0,map);
    }

    @RequestMapping(path = {"/follow/followquestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String getFollowQuestions( @RequestParam("userId")int userId){
        List<Integer> list = followService.getFollowees(userId, 1,0, 20);
        List<Question> questions = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        for(int i = 0; i < list.size();i++){
            Question question = questionService.getQuestion(list.get(i));
            questions.add(question);
        }
        map.put("datas",questions);
        map.put("msg","success");
        return JsonUtil.getJSONString(0,map);
    }

    @RequestMapping(path = {"/follow/answer"},method = {RequestMethod.POST})
    @ResponseBody
    public String getFollowAnswers( @RequestParam("userId")int userId){
        List<Integer> list = followService.getFollowees(userId, 2,0, 20);
        List<Answer> answers = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        for(int i = 0; i < list.size();i++){
            Answer answer = commentService.getAnswerById(list.get(i));
            answers.add(answer);
        }
        map.put("datas",answers);
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
