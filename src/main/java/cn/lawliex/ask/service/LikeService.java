package cn.lawliex.ask.service;

import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.util.JedisAdapter;
import cn.lawliex.ask.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by Terence on 2016/12/30.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    QuestionService questionService;

    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }
    public long like(int entityType, int entityId,int userId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        long likeCount = jedisAdapter.scard(likeKey);
        if(entityType == 1){
            Question question = questionService.getQuestion(entityId);
            question.setLikeCount((int)likeCount);
            questionService.updateLikeCount(question);
        }
        return likeCount;
    }
    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        long likeCount = jedisAdapter.scard(likeKey);
        if(entityType == 1){
            Question question = questionService.getQuestion(entityId);
            question.setLikeCount(Math.max(0,(int)likeCount - 1));
            questionService.updateLikeCount(question);
        }
        return likeCount;
    }

}
