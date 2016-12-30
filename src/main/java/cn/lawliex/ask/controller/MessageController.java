package cn.lawliex.ask.controller;

import cn.lawliex.ask.model.Message;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.MessageService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Terence on 2016/12/30.
 */
@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/message/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toId") int toId,@RequestParam("content")String content,@RequestParam("ticket")String ticket){
        User user = userService.getUserByTicket(ticket);
        int fromId = user.getId();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setContent(content);
        String conversationId = "";
        if(fromId > toId)
            conversationId = toId+"_"+fromId;
        else
            conversationId = fromId+"_"+toId;
        message.setConversationId(conversationId);
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        int res = messageService.addMessage(message);
        if(res > 0){
            return JsonUtil.getJSONString(0,"success");
        }
        return  JsonUtil.getJSONString(-1,"error");

    }

    @RequestMapping(path = {"/message/detail"},method = {RequestMethod.POST})
    @ResponseBody
    public String messageDetail(@RequestParam("id")int id){
        Message message = messageService.getMessage(id);
        Map<String, Object> map = new HashMap<>();

        if(message!=null) {
            map.put("data",message);
            map.put("msg", "success");
            return JsonUtil.getJSONString(0, map);
        }
        return JsonUtil.getJSONString(-1, "error");
    }
}