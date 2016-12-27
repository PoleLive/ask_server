package cn.lawliex.ask.service;

import cn.lawliex.ask.dao.UserDAO;
import cn.lawliex.ask.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Terence on 2016/12/27.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
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
}
