package cn.lawliex.ask.util;

/**
 * Created by Terence on 2016/12/30.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String HALEY_LIKE = "LIKE";
    private static String HALEY_DISLIKE = "DISLIKE";
    private static String HALEY_FOLLOWER = "FOLLOWER";
    private static String HALEY_UNFOLLOWER = "UNFOLLOWER";
    private static String HALEY_FOLLOWEE = "FOLLOWEE";
    private static String HALEY_UNFOLLOWEE = "UNFOLLOWEE";
    private static String HALEY_EVENT_QUEUE = "EVENT_QUEUE";

    public static String getLikeKey(int entityType, int entityId) {
        return HALEY_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return HALEY_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return HALEY_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getUnFollowerKey(int entityType, int entityId) {
        return HALEY_UNFOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getFolloweeKey(int userId, int entityType) {
        return HALEY_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getUnFolloweeKey(int userId, int entityType) {
        return HALEY_UNFOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventKey(){
        return HALEY_EVENT_QUEUE;
    }
}
