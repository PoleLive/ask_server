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
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})"})
    int addQuestion(Question question);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Question selectById(int id);

    @Select({"select", SELECT_FIELDS, "from",TABLE_NAME,"where user_id=#{userId}"})
    List<Question> selectByUserId(int userId);

    @Update({"update ", TABLE_NAME, " set content=#{content} where id=#{id}"})
    void updateContent(Question question);

    @Update({"update ", TABLE_NAME, " set title=#{title} where id=#{id}"})
    void updateTitle(Question question);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"limit #{offset},#{limit}"})
    List<Question> selectQuestions(@Param("offset") int offset,@Param("limit") int limit);
}
