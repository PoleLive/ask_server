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
import cn.lawliex.ask.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * Created by Terence on 2016/12/27.
 */
@Controller
public class CommentController {
    Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/answer/list"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAnswerList(@RequestParam("questionId")int questionId){
        Map<String, Object> map = new HashMap<>();
        List<Comment> comments = commentService.getQuestionAnswer(questionId);
        List<Answer> answers = new ArrayList<>();
        for (Comment c : comments){
            Answer answer = commentService.getAnswerById(c.getId());
            answers.add(answer);
        }
        map.put("comments",answers);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/answer/ulist"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAnswerListByUserId(@RequestParam("userId")int userId){
        Map<String, Object> map = new HashMap<>();
        List<Comment> comments = commentService.getAnswerByUserId(userId);
        List<Answer> answers = new ArrayList<>();
        for (Comment c : comments){
            Answer answer = commentService.getAnswerById(c.getId());
            answers.add(answer);
        }
        map.put("answers",answers);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/comment/list"},method = {RequestMethod.POST})
    @ResponseBody
    public String getCommentList(@RequestParam("answerId")int answerId){
        Map<String, Object> map = new HashMap<>();
        List<Comment> comments = commentService.getAnswerComment(answerId);
        for (Comment c : comments){
            c.setLikeCount(getCommentLikeCount(c.getId()));
        }
        map.put("comments",comments);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }
    public int getCommentLikeCount(int id){
        return (int)likeService.getLikeCount(3,id);
    }


    @RequestMapping(path = {"/answer/detail"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAnswer(@RequestParam("id")int id){
        Answer answer = commentService.getAnswerById(id);
        Map<String,Object> map = new HashMap<>();
        if(answer != null){
            map.put("msg","success");
            map.put("data",answer);
            return JsonUtil.getJSONString(0,map);
        }
        return JsonUtil.getJSONString(-1,"error");
    }

    @RequestMapping(path = {"/comment/detail"},method = {RequestMethod.POST})
    @ResponseBody
    public String getComment(@RequestParam("id")int id){
        Comment c = commentService.getComment(id);
        Map<String,Object> map = new HashMap<>();
        if(c != null){
            map.put("msg","success");
            map.put("data",c);
            return JsonUtil.getJSONString(0,map);
        }
        return JsonUtil.getJSONString(-1,"error");
    }

    @RequestMapping(path = {"answer/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addAnswer(@RequestParam("questionId")int questionId,
                              @RequestParam("content")String content,
                              @RequestParam("userId")int userId
                              ){
        Map<String, Object> map = new HashMap<>();
        Comment answer = new Comment();
        answer.setUserId(userId);
        answer.setEntityType(1);
        answer.setContent(content);
        answer.setCreatedDate(new Date());
        answer.setEntityId(questionId);
        answer.setStatus(0);
        if(commentService.addComment(answer) > 0){
            map.put("msg","回答添加成功");
            EventModel eventModel = new EventModel();
            eventModel.setActorId(userId)
                    .setEntityId(answer.getId())
                    .setEntityType(2)
                    .setEntityOwnerId(answer.getUserId())
                    .setType(EventType.ANSWER);
            eventProducer.fireEvent(eventModel);
            return JsonUtil.getJSONString(0, map);
        }
        map.put("msg","回答添加失败");
        return JsonUtil.getJSONString(-1, map);
    }
    @RequestMapping(path = {"comment/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("answerId")int answerId,
                            @RequestParam("content")String content,
                            @RequestParam("ticket")String ticket
    ){
        Map<String, Object> map = new HashMap<>();
        Comment comment = new Comment();
        User user  = userService.getUserByTicket(ticket);
        comment.setUserId(user.getId());
        comment.setEntityType(2);
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setEntityId(answerId);
        comment.setStatus(0);
        if(commentService.addComment(comment) > 0){
            map.put("msg","回答添加成功");
            return JsonUtil.getJSONString(0, map);
        }
        map.put("msg","回答添加失败");
        return JsonUtil.getJSONString(-1, map);
    }
    //获取所有回答
    @RequestMapping(path = {"/comment/all"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAllComment(){
        List<Comment> comments = commentService.getAnswerComment();
        Map<String,Object> map = new HashMap<>();
        map.put("comments",comments);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }

    //获取所有回答
    @RequestMapping(path = {"/answer/all"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAllAnswers(){
        List<Answer> answers = commentService.getAllAnswers();
        Map<String,Object> map = new HashMap<>();
        map.put("answers",answers);
        map.put("msg","success");
        return JsonUtil.getJSONString(0, map);
    }
}
