package cn.lawliex.ask.async;

import cn.lawliex.ask.util.JedisAdapter;
import cn.lawliex.ask.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Terence on 2017/1/5.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    public boolean fireEvent(EventModel eventModel){
        try {
            String key = RedisKeyUtil.getEventKey();
            String jsonStr = JSONObject.toJSONString(eventModel);
            jedisAdapter.lpush(key, jsonStr);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
