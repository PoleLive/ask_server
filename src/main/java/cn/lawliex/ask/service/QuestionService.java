package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.LoginTicketDAO;
import cn.lawliex.ask.dao.QuestionDAO;
import cn.lawliex.ask.dao.UserDAO;
import cn.lawliex.ask.model.LoginTicket;
import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Terence on 2016/12/27.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;
    public int addQuestion(Question question){
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // 敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }
    public Question getQuestion(int id){
        return questionDAO.selectById(id);
    }
    public List<Question> getQuestionsByUserId(int userId){
        return questionDAO.selectByUserId(userId);
    }
    public void updateContent(Question question){
        questionDAO.updateContent(question);
    }
    public void updateTitle(Question question){
        questionDAO.updateTitle(question);
    }
    public List<Question> getQuestions(int offset, int limit){
        return questionDAO.selectQuestions();
    }
    public void updateLikeCount(Question question){
        questionDAO.updateLikeCount(question);
    }
    public void updateCommentCount(Question question){
        questionDAO.updateCommentCount(question);
    }
    public int getUserQuestionCount(int userId){
        return questionDAO.countUserQuestion(userId);
    }
}
