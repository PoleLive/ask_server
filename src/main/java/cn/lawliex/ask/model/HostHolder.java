package cn.lawliex.ask.model;

import org.springframework.stereotype.Component;

/**
 * Created by Terence on 2016/12/28.
 */
@Component
public class HostHolder {
    ThreadLocal<User> users = new ThreadLocal<>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
