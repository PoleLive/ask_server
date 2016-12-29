package cn.lawliex.ask.configuration;

import cn.lawliex.ask.interceptor.PassportInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Terence on 2016/12/28.
 */
@Component
public class AskWebConfiguration extends WebMvcConfigurerAdapter{
    Logger logger = LoggerFactory.getLogger(WebMvcConfigurerAdapter.class);
    @Autowired
    PassportInterceptor passportInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/*")
                .addPathPatterns("/*/*");

        //logger.error("初始化中。。。。。。。。。。。。。");
        super.addInterceptors(registry);
    }
}
