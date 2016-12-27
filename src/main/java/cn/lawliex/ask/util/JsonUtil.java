package cn.lawliex.ask.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by Terence on 2016/12/27.
 */
public class JsonUtil {
    public static String getJSONString(int code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toJSONString();

    }
    public static String getJSONString(int code, String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }
    public static String getJSONString(int code, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> it : map.entrySet()){
            json.put(it.getKey(),it.getValue());
        }
        return json.toJSONString();
    }
}
