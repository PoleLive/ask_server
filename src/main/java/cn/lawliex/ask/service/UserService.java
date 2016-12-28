package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.LoginTicketDAO;
import cn.lawliex.ask.dao.UserDAO;
import cn.lawliex.ask.model.LoginTicket;
import cn.lawliex.ask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Terence on 2016/12/27.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public int addUser(User user){
        return userDAO.addUser(user);
    }
    public User getUser(String name){
        return userDAO.selectByName(name);
    }
    public User getUser(int id){
        return userDAO.selectById(id);
    }
    public void updatePassword(User user){
        userDAO.updatePassword(user);
    }

    public int addLoginTicket(LoginTicket loginTicket){
        return loginTicketDAO.addLoginTicket(loginTicket);
    }
    public LoginTicket getLoginTicketById(int id){
        return loginTicketDAO.selectByUserId(id);
    }
    public LoginTicket getLoginTicketByUserId(int userId){
        return loginTicketDAO.selectByUserId(userId);
    }
    public void logout(LoginTicket loginTicket){
        loginTicketDAO.updateStatus(loginTicket.getId(),1);
    }
    public User getUserByTicket(String ticket){
        LoginTicket loginTicket = getLoginTicket(ticket);

        if(loginTicket!=null && loginTicket.getStatus() == 0){
            return userDAO.selectById(loginTicket.getUserId());
        }
        return null;
    }
    public LoginTicket getLoginTicket(String ticket){
        return loginTicketDAO.selectByTicket(ticket);
    }
    public String addTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 100 * 24 * 3600 );
        loginTicket.setExpired(date);
        String ticket = UUID.randomUUID().toString().replace("-","");
        loginTicket.setTicket(ticket);
        addLoginTicket(loginTicket);
        return ticket;
    }
}
