package cn.lawliex.ask.dao;

import cn.lawliex.ask.model.Feed;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * Created by Terence on 2017/1/6.
 */
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, type, created_date, data, name, head_url, entity_type, entity_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into feed(",INSERT_FIELDS,") values(#{userId},#{type},#{createdDate},#{data},#{name},#{headUrl},#{entityType},#{entityId})"})
    int addFeed(Feed feed);

    @Select({"select * from feed where user_id={userId} order by created_date desc"})
    List<Feed> selectByUserId(int userId);

    @Select({"select * from feed where id=#{id}"})
    Feed selectById(int id);

    List<Feed> selectUserFeeds(@Param("maxId")int maxId,@Param("userIds")List<Integer> userIds,@Param("count")int count);
}
