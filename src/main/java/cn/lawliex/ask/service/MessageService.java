package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.CommentDAO;
import cn.lawliex.ask.dao.MessageDAO;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Message getMessage(int id){
        return messageDAO.selectById(id);
    }
}
