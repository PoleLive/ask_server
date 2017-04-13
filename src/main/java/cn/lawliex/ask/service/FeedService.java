package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.FeedDAO;
import cn.lawliex.ask.model.Feed;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terence on 2017/1/6.
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    public List<Feed> getUsersFeeds(int maxId, List<Integer> userIds, int count){
        List<Feed> a = feedDAO.selectUserFeeds(maxId,userIds,count);

        ArrayList<Feed> ans = new ArrayList<>();
        for(int i = 0; i < a.size();i++)
            ans.add(a.get(i));
        for(int i = ans.size() - 1; i >= 0; i--){
            Feed f = ans.get(i);
            if(f == null) continue;
            if(f.getEntityType() == 1){
                Question question = questionService.getQuestion(f.getEntityId());
                if(question != null && question.getStatus() != 0)
                    ans.remove(i);
            }
        }
        return ans;
    }
    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return  feed.getId() > 0;
    }
    public Feed getById(int id){
        return feedDAO.selectById(id);
    }
}
