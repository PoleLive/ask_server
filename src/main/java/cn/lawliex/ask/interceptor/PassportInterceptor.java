package cn.lawliex.ask.interceptor;

import cn.lawliex.ask.dao.LoginTicketDAO;
import cn.lawliex.ask.dao.UserDAO;
import cn.lawliex.ask.model.HostHolder;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Terence on 2016/12/28.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = request.getParameter("ticket");

        if(ticket != null){
            User user = userService.getUserByTicket(ticket);
            if(user != null ){
                hostHolder.setUser(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
