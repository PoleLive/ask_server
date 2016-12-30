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
    String INSERT_FIELDS = " from_id, to_id, created_date, content, has_read, conversation_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{fromId}, #{toId}, #{createdDate}, #{content}, #{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Message selectById(int id);

    @Select({"select * from", TABLE_NAME, "where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selectConversationId(@Param("conversationId") int conversationId, @Param("offset") int offset, @Param("limit") int limit);


}
