package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.CommentDAO;
import cn.lawliex.ask.dao.MessageDAO;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Terence on 2016/12/27.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getMessage(String conversationId){
        return messageDAO.selectConversationId(conversationId);
    }
    public List<Message> getMessage(int userId){
        List<Message> messages = new ArrayList<>();
        Map<String,Boolean> map = new HashMap<>();
        List<Message> list = messageDAO.selectByUserId(userId);
        for(Message m : list){
            if(map.get(m.getConversationId()) == null || map.get(m.getConversationId()) == false){
                messages.add(m);
                map.put(m.getConversationId(),true);
            }
        }
        return messages;
    }
}
