package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.CommentDAO;
import cn.lawliex.ask.dao.QuestionDAO;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Terence on 2016/12/27.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment){
        return  commentDAO.addComment(comment);
    }
    public Comment getComment(int id){
        return commentDAO.selectById(id);
    }
    public List<Comment> getQuestionAnswer(int questionId, int offset, int limit){
        return commentDAO.selectByEntityId(questionId, 1, offset, limit);
    }
    public List<Comment> getAnswerComment(int answerId, int offset, int limit){
        return  commentDAO.selectByEntityId(answerId, 2, offset, limit);
    }
    void deleteComment(Comment comment){
        commentDAO.updateStatus(comment.getId(),1);
    }
}
