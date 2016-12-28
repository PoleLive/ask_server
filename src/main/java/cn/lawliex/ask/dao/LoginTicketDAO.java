package cn.lawliex.ask.dao;

import cn.lawliex.ask.model.LoginTicket;
import cn.lawliex.ask.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Terence on 2016/12/28.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(" +
            "#{userId}, #{ticket}, #{expired}, #{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    LoginTicket selectById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Select({"select", SELECT_FIELDS, "from",TABLE_NAME,"where user_id=#{userId}"})
    LoginTicket selectByUserId(int userId);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(int id, int status);
}
