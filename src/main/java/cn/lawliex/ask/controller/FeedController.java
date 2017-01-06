package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Feed;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.FeedService;
import cn.lawliex.ask.service.FollowService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Terence on 2017/1/6.
 */
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/feeds/pull"},method = {RequestMethod.POST})
    @ResponseBody
    private String getFullFeeds(@Param("ticket")String ticket){
        User user = userService.getUserByTicket(ticket);
        List<Integer> followees = new ArrayList<>();
        if(user!=null && user.getId()!=0){
            followees = followService.getFollowees(user.getId(),0,Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUsersFeeds(Integer.MAX_VALUE, followees, 10);

        Map<String,Object> map = new HashMap<>();
        map.put("feeds",feeds);
        map.put("msg","success");
        return JsonUtil.getJSONString(0,map);
    }
}
