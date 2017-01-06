package cn.lawliex.ask.controller;

import cn.lawliex.ask.async.EventModel;
import cn.lawliex.ask.async.EventProducer;
import cn.lawliex.ask.async.EventType;
import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.CommentService;
import cn.lawliex.ask.service.LikeService;
import cn.lawliex.ask.service.QuestionService;
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
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;


    @RequestMapping(path = {"/like/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("entityType")int entityType, @RequestParam("entityId")int entityId,@RequestParam("ticket")String ticket){
        User user = userService.getUserByTicket(ticket);
        int ownerId = 0;
        switch (entityType){
            case 0:
                User owner = userService.getUser(entityId);
                ownerId = owner.getId();
                break;
            case 1:
                Question question = questionService.getQuestion(entityId);
                ownerId = question.getUserId();
                break;
            case 2:
                Answer answer = commentService.getAnswerById(entityId);
                ownerId = answer.getUserId();
                break;
            case 3:
                Comment comment = commentService.getComment(entityId);
                ownerId = comment.getUserId();
                break;
        }

        Map<String,Object> map = new HashMap<>();
        long count = likeService.like(entityType,entityId,user.getId());
        map.put("count",count);
        map.put("msg","success");
        EventModel eventModel = new EventModel();
        eventModel.setActorId(user.getId())
                .setEntityId(entityId)
                .setEntityType(entityType)
                .setEntityOwnerId(ownerId)
                .setType(EventType.LIKE);
        eventProducer.fireEvent(eventModel);
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
