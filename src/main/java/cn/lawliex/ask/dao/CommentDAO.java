package cn.lawliex.ask.dao;


import cn.lawliex.ask.model.Answer;
import cn.lawliex.ask.model.Comment;
import cn.lawliex.ask.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Terence on 2016/12/27.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, created_date, entity_type, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{content}, #{userId}, #{entityId}, #{createdDate}, #{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select c.*, q.title question_title, u.name author,head_url from comment c " +
            "left join question q on q.id = c.entity_id and entity_type = 1 left " +
            "join user u on c.user_id = u.id where c.id=#{id}"})
    Comment selectById(int id);

    @Select({"select c.*, u.name author,head_url from comment c " +
            "left join user u on c.user_id = u.id where entity_type=#{entityType} order by id desc"})
    List<Comment> selectComments(@Param("entityType")int entityType);

    @Select({"select c.*, u.name author,head_url from comment c " +
            "left join user u on c.user_id = u.id where entity_Id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntityId(@Param("entityId") int entityId, @Param("entityType")int entityType);

//    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc limit #{offset}, #{limit}"})
//    List<Comment> selectByEntityId(@Param("entityId") int entityId, @Param("entityType")int entityType, @Param("offset") int offset, @Param("limit")int limit);

    @Select({"select c.*, head_url " , "from comment c left join user u on c.user_id = u.id where c.user_id=#{userId} and c.entity_type=1"})
    List<Comment> selectAnswerByUserId(int userId);

    @Select({"select c.*, u.head_url, u.name author, q.title question_title, (select count(*) from comment cc where c.id = cc.entity_id and cc.entity_type = 2) comment_count" +
            " from comment c left join user u on c.user_id = u.id left join question q on q.id = c.entity_id " +
            "where c.entity_type = 1 and c.id = #{id} "})
    Answer selectAnswerById(int id);

    @Select({"select c.*, u.head_url, u.name author, q.title question_title, (select count(*) from comment cc where c.id = cc.entity_id and cc.entity_type = 2) comment_count" +
            " from comment c left join user u on c.user_id = u.id left join question q on q.id = c.entity_id " +
            "where c.entity_type = 1 order by id desc"})
    List<Answer> selectAnswers();

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(@Param("id")int id,@Param("status")int status );


    @Select({"select q.*, name from",TABLE_NAME," q left join user u on q.user_id = u.id order by id desc limit #{offset},#{limit}"})
    List<Question> selectQuestions(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(*) from comment where entity_type=1 and user_id=#{userId}"})
    int countAnswerByUserId(int userId);

    @Select({"select count(*) from comment where entity_type=#{entityType} and entity_id=#{entityId}"})
    int countByEntityId(@Param("entityType")int entityType,@Param("entityId")int entityId);
}
