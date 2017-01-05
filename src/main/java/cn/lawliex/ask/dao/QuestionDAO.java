package cn.lawliex.ask.dao;


import cn.lawliex.ask.model.Question;
import cn.lawliex.ask.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Terence on 2016/12/27.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count, like_count ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount},#{likeCount})"})
    int addQuestion(Question question);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Question selectById(int id);

    @Select({"select q.*, name from",TABLE_NAME," q left join user u on q.user_id = u.id where q.user_id=#{userId} order by q.id desc"})
    List<Question> selectByUserId(int userId);

    @Update({"update ", TABLE_NAME, " set content=#{content} where id=#{id}"})
    void updateContent(Question question);

    @Update({"update ", TABLE_NAME, " set title=#{title} where id=#{id}"})
    void updateTitle(Question question);

    @Update({"update ",TABLE_NAME, " set like_count=#{likeCount} where id=#{id}"})
    void updateLikeCount(Question question);

    @Update({"update ",TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    void updateCommentCount(Question question);

    @Select({"select q.*, name from",TABLE_NAME," q left join user u on q.user_id = u.id order by id desc"})
    List<Question> selectQuestions();

    @Select({"select count(*) from", TABLE_NAME, "where user_id=#{userId}"})
    int countUserQuestion(int userId);
}
