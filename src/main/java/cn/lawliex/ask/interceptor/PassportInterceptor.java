package cn.lawliex.ask.interceptor;

import cn.lawliex.ask.dao.LoginTicketDAO;
import cn.lawliex.ask.dao.UserDAO;
import cn.lawliex.ask.model.HostHolder;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * Created by Terence on 2016/12/28.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    UserService userService;

    @Autowired
    private HostHolder hostHolder;

    String path;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = request.getParameter("ticket");
        path = request.getServletPath();
        logger.info("pre:"+path);
        if(path.equals("/login") || path.equals("/register") || path.contains("."))
            return true;
        if(ticket != null){
            User user = userService.getUserByTicket(ticket);
            if(user != null ){
                hostHolder.setUser(user);
                logger.debug(path);
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("post:"+ path);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        logger.info("after:"+ path);
    }
}
