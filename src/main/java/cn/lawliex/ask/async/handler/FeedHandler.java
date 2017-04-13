package cn.lawliex.ask.async.handler;

import cn.lawliex.ask.async.EventHandler;
import cn.lawliex.ask.async.EventModel;
import cn.lawliex.ask.async.EventType;
import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Feed;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Terence on 2017/1/6.
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;


    private String buildFeedData(EventModel model){

        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }

        switch (model.getType()){
            case COMMENT:

                break;
            case QUESTION:
            case FOLLOW:
                //EntityType 1代表问题，0代表用户，2代表回答
                if(model.getEntityType() == 1){
                    Question question = questionService.getQuestion(model.getEntityId());
                    if(question!=null)
                        return JSONObject.toJSONString(question);
                }else if(model.getEntityType() == 2){
                    Answer answer = commentService.getAnswerById(model.getEntityId());
                    if(answer!=null)
                        return JSONObject.toJSONString(answer);
                }
                break;
            case LIKE:
                if(model.getEntityType() == 1){

                }else if(model.getEntityType() == 2){
                    Answer answer = commentService.getAnswerById(model.getEntityId());
                    if(answer != null)
                        return JSONObject.toJSONString(answer);
                }
                break;
            case ANSWER:
                Answer answer = commentService.getAnswerById(model.getEntityId());
                if(answer!=null)
                    return JSONObject.toJSONString(answer);
                break;
        }
        return null;
    }

    @Override
    public void handleEvent(EventModel eventModel) {
        Feed feed = new Feed();
        User user = userService.getUser(eventModel.getActorId());
        feed.setCreatedDate(new Date());
        feed.setUserId(eventModel.getActorId());
        feed.setType(eventModel.getType().getValue());
        feed.setName(user.getName());
        feed.setHeadUrl(user.getHeadUrl());
        feed.setData(buildFeedData(eventModel));
        feed.setEntityType(eventModel.getEntityType());
        feed.setEntityId(eventModel.getEntityId());
        if(feed.getData() == null){
            return ;
        }
        feedService.addFeed(feed);
    }

    @Override
    public List<EventType> getFollowEvent() {
        return Arrays.asList(EventType.QUESTION,EventType.FOLLOW,EventType.ANSWER,EventType.COMMENT,EventType.LIKE);
    }
}
