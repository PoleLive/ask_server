package cn.lawliex.ask.async;

import cn.lawliex.ask.util.JedisAdapter;

import cn.lawliex.ask.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Terence on 2017/1/5.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{
    Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    Map<EventType,List<EventHandler>> config = new HashMap<>();
    ApplicationContext applicationContext;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String,EventHandler> entry : beans.entrySet()){
                for(EventType type : entry.getValue().getFollowEvent()){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = RedisKeyUtil.getEventKey();
                while(true){
                    List<String> messages = jedisAdapter.brpop(0,key);
                    for(String message : messages){
                        if(message.equals(key))
                            continue;
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for(EventHandler handler : config.get(eventModel.getType())){
                            handler.handleEvent(eventModel);
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}