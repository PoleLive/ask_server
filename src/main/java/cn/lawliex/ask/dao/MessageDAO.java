package cn.lawliex.ask.dao;


import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Message;
import cn.lawliex.ask.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Terence on 2016/12/27.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, created_date, content, has_read, conversation_id,type ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{fromId}, #{toId}, #{createdDate}, #{content}, #{hasRead},#{conversationId},#{type})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Message selectById(int id);

    @Select({"select m.*, u.name from_name, v.name to_name,u.head_url from_url, v.head_url to_url from message m left join user u on u.id = from_id left join user v on v.id = to_id where has_read=0 and conversation_id=#{conversationId} and to_id = #{id} order by created_date"})
    List<Message> selectConversationId(@Param("conversationId") String conversationId,@Param("id")int id);

    @Select({"select m.*, u.name from_name, u.head_url from_url, v.name to_name, v.head_url to_url from message m left join user u on u.id = from_id left join user v on v.id = to_id where from_id = #{userId} or to_id = #{userId} order by created_date desc"})
    List<Message> selectByUserId(int userId);

    @Update({"update message set has_read = 1 where to_id = #{userId} and conversation_id = #{conversationId}"})
    void updateMessageStatus(@Param("userId")int userId, @Param("conversationId")String conversatioinId);

    @Select({"select count(*) from message where has_read = 0 and to_id = #{userId} and conversation_id = #{conversationId}"})
    int getUnReadCount(@Param("userId")int userId, @Param("conversationId")String conversationId);
}
