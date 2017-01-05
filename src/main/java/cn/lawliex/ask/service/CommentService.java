package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.CommentDAO;
import cn.lawliex.ask.dao.QuestionDAO;
import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import org.apache.ibatis.annotations.Param;
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
    @Autowired
    LikeService likeService;
    @Autowired
    QuestionService questionService;

    public int addComment(Comment comment){
        int res =   commentDAO.addComment(comment);
        if(res > 0 && comment.getEntityType() == 1){

            Question question = questionService.getQuestion(comment.getEntityId());
            question.setCommentCount(question.getCommentCount() + 1);
            questionService.updateCommentCount(question);
        }
        return res;
    }
    public List<Comment> getAnswerByUserId(int userId){
        return commentDAO.selectAnswerByUserId(userId);
    }
    public Comment getComment(int id){
        return commentDAO.selectById(id);
    }
    public List<Comment> getAnswerComment(int answerId){
        return commentDAO.selectByEntityId(answerId,2);
    }
    public List<Comment> getQuestionAnswer(int questionId){
        return commentDAO.selectByEntityId(questionId, 1);
    }
    public List<Comment> getAnswerComment(int answerId, int offset, int limit){
        return  commentDAO.selectByEntityId(answerId, 2);
    }
    void deleteComment(Comment comment){
        commentDAO.updateStatus(comment.getId(),1);
    }

    public int countByEntityId(int entityType, int entityId){
        return commentDAO.countByEntityId(entityType,entityId);
    }
    public int countAnswerByUserId(int userId){
        return commentDAO.countAnswerByUserId(userId);
    }
    public Answer getAnswerById(int id){
        Comment c = getComment(id);
        Answer answer = new Answer();
        answer.setId(c.getId());
        answer.setUserId(c.getUserId());
        answer.setEntityId(c.getEntityId());
        answer.setEntityType(c.getEntityType());
        answer.setStatus(c.getStatus());
        answer.setContent(c.getContent());
        answer.setAuthor(c.getAuthor());
        answer.setCreatedDate(c.getCreatedDate());
        answer.setQuestionTitle(c.getQuestionTitle());
        answer.setHeadUrl(c.getHeadUrl());
        answer.setLikeCount((int) likeService.getLikeCount(2,answer.getId()));

        answer.setCommentCount(countByEntityId(2,answer.getId()));
        return  answer;
    }
}
