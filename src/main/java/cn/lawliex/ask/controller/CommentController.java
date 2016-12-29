package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.service.CommentService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @RequestMapping(path = {"/answer/list"},method = {RequestMethod.POST})
    @ResponseBody
    public String getCommentList(@RequestParam("questionId")int questionId){
        Map<String, Object> map = new HashMap<>();
        List<Comment> comments = commentService.getQuestionAnswer(questionId, 0, 20);
        map.put("comments",comments);
        map.put("msg","获取成功");
        return JsonUtil.getJSONString(0, map);
    }

    @RequestMapping(path = {"/answer/detail"},method = {RequestMethod.POST})
    @ResponseBody
    public String getAnswer(@RequestParam("id")int id){
        Comment answer = commentService.getComment(id);
        Map<String,Object> map = new HashMap<>();

        if(answer != null){
            map.put("msg","success");
            map.put("data",answer);
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
            return JsonUtil.getJSONString(0, map);
        }
        map.put("msg","回答添加失败");
        return JsonUtil.getJSONString(-1, map);
    }


}
