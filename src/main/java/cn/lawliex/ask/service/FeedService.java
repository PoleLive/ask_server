package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.FeedDAO;
import cn.lawliex.ask.model.Feed;
import cn.lawliex.ask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Feed> getUsersFeeds(int maxId, List<Integer> userIds, int count){
        List<Feed> ans = feedDAO.selectUserFeeds(maxId,userIds,count);
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
