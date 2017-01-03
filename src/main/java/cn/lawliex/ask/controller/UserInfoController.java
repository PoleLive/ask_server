package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.model.UserInfo;
import cn.lawliex.ask.service.*;
import cn.lawliex.ask.util.JedisAdapter;
import cn.lawliex.ask.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
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
public class UserInfoController {
    @Autowired
    UserService userService;
    @Autowired
    JedisAdapter jedisAdapterl;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    FollowService followService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/user/info"}, method = {RequestMethod.POST})
    @ResponseBody
    public String getUserInfo(@RequestParam("userId") int userId){
        User user = userService.getUser(userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setHeadUrl(user.getHeadUrl());
        userInfo.setLikeCount((int)likeService.getLikeCount(0,user.getId()));
        userInfo.setFolloweeCount((int)followService.getFolloweeCount(user.getId(),0));
        userInfo.setFollowingCount((int)followService.getFollowerCount(0,user.getId()));
        userInfo.setAnswerCount(commentService.countAnswerByUserId(user.getId()));
        userInfo.setQuestionCount(questionService.getUserQuestionCount(user.getId()));
        userInfo.setMotto("世界那么大，你不想去看看吗...");
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo",userInfo);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }

}
