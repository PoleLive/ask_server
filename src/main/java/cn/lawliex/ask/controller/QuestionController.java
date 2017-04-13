package cn.lawliex.ask.controller;

import cn.lawliex.ask.async.EventModel;
import cn.lawliex.ask.async.EventProducer;
import cn.lawliex.ask.async.EventType;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.FollowService;
import cn.lawliex.ask.service.QuestionService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class QuestionController {
    Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    FollowService followService;
    @RequestMapping(path = {"/question/list"},method = {RequestMethod.POST})
    @ResponseBody
    public String getQuestionList(){
        Map<String, Object> map = new HashMap<>();
        List<Question> questions = questionService.getQuestions(0, 10);
        for(Question q:questions){
            q.setHeadUrl(userService.getUser(q.getUserId()).getHeadUrl());
            q.setLikeCount((int) followService.getFollowerCount(1,q.getId()));
        }
        map.put("questions",questions);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/question/adminlist"},method = {RequestMethod.POST})
    @ResponseBody
    public String getQuestionAdminList(){
        Map<String, Object> map = new HashMap<>();
        List<Question> questions = questionService.getAdminQuestions(0, 10);
        for(Question q:questions){
            q.setHeadUrl(userService.getUser(q.getUserId()).getHeadUrl());
            q.setLikeCount((int) followService.getFollowerCount(1,q.getId()));
        }
        map.put("questions",questions);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/question/mylist"},method = {RequestMethod.POST})
    @ResponseBody
    public String getMyQuestionList(@RequestParam("userId") int userId){
        Map<String, Object> map = new HashMap<>();
        List<Question> questions = questionService.getQuestionsByUserId(userId);
        for(Question q:questions){
            q.setHeadUrl(userService.getUser(q.getUserId()).getHeadUrl());
        }
        map.put("questions",questions);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/question/delete"},method = {RequestMethod.POST})
    @ResponseBody
    public String deleteQuestion(@RequestParam("id") int id){
        Question question = questionService.getQuestion(id);
        Map<String,Object> map = new HashMap<>();
        question.setLikeCount((int)followService.getFollowerCount(1, question.getId()));
        question.setStatus(1);
        if(question != null){
            questionService.updateQuestionStatus(question);
            map.put("msg","success");
            return  JsonUtil.getJSONString(0, map);

        }
        return JsonUtil.getJSONString(-1,"error");
    }

    @RequestMapping(path = {"/question/canceldelete"},method = {RequestMethod.POST})
    @ResponseBody
    public String canceldeleteQuestion(@RequestParam("id") int id){
        Question question = questionService.getQuestion(id);
        Map<String,Object> map = new HashMap<>();
        question.setLikeCount((int)followService.getFollowerCount(1, question.getId()));
        question.setStatus(0);
        if(question != null ){
            questionService.updateQuestionStatus(question);
            map.put("msg","success");
            return  JsonUtil.getJSONString(0, map);

        }
        return JsonUtil.getJSONString(-1,"error");
    }

    @RequestMapping(path = {"/question/detail"},method = {RequestMethod.POST})
    @ResponseBody
    public String getQuestion(@RequestParam("id")int id){
        Question question = questionService.getQuestion(id);
        Map<String,Object> map = new HashMap<>();
        question.setLikeCount((int) followService.getFollowerCount(1,question.getId()));
        if(question != null){
            map.put("msg","success");
            map.put("data",question);
            return JsonUtil.getJSONString(0,map);
        }
        return JsonUtil.getJSONString(-1,"error");
    }

    @RequestMapping(path = {"question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,
                              @RequestParam("content")String content,
                              @RequestParam("userId")int userId
                              ){
        Map<String, Object> map = new HashMap<>();
        Question question = new Question();
        question.setUserId(userId);
        question.setTitle(title);
        question.setLikeCount(0);
        question.setContent(content);
        question.setCreatedDate(new Date());
        question.setCommentCount(0);
        if(questionService.addQuestion(question) > 0){

            EventModel eventModel = new EventModel();
            eventModel.setActorId(userId)
                    .setEntityId(question.getId())
                    .setEntityType(1)
                    .setEntityOwnerId(question.getUserId())
                    .setType(EventType.QUESTION);
            eventProducer.fireEvent(eventModel);
            map.put("msg","问题添加成功");
            return JsonUtil.getJSONString(0, map);
        }
        map.put("msg","问题添加失败");
        return JsonUtil.getJSONString(-1, map);
    }


}
