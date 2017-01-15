package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.CommentDAO;
import cn.lawliex.ask.dao.QuestionDAO;
import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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

    @Autowired
    SensitiveService sensitiveService;
    //添加评论
    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));

        int res =   commentDAO.addComment(comment);
        if(res > 0 && comment.getEntityType() == 1){
            Question question = questionService.getQuestion(comment.getEntityId());
            question.setCommentCount(question.getCommentCount() + 1);
            questionService.updateCommentCount(question);
        }
        return res;
    }
    //获取用户回答
    public List<Comment> getAnswerByUserId(int userId){
        return commentDAO.selectAnswerByUserId(userId);
    }
    //获取某个回答
    public Comment getComment(int id){
        return commentDAO.selectById(id);
    }
    //获取回答的评论列表
    public List<Comment> getAnswerComment(int answerId){
        return commentDAO.selectByEntityId(answerId,2);
    }
    //获取问题的答案列表
    public List<Comment> getQuestionAnswer(int questionId){
        return commentDAO.selectByEntityId(questionId, 1);
    }
    //获取回答的评论列表
    public List<Comment> getAnswerComment(int answerId, int offset, int limit){
        return  commentDAO.selectByEntityId(answerId, 2);
    }
    //删除评论
    void deleteComment(Comment comment){
        commentDAO.updateStatus(comment.getId(),1);
    }
    //计算评论数或回答数
    public int countByEntityId(int entityType, int entityId){
        return commentDAO.countByEntityId(entityType,entityId);
    }
    //计算用户回答数
    public int countAnswerByUserId(int userId){
        return commentDAO.countAnswerByUserId(userId);
    }
    //获取一个回答
    public Answer getAnswerById(int id){
        Answer answer =commentDAO.selectAnswerById(id);
        answer.setLikeCount((int) likeService.getLikeCount(2,answer.getId()));
        return  answer;
    }
    public  List<Answer> getAllAnswers(){
        List<Answer> answers = commentDAO.selectAnswers();
        for (Answer a : answers)
            a.setLikeCount((int) likeService.getLikeCount(2,a.getId()));
        return answers;
    }
}
