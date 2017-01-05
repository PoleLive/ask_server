package cn.lawliex.ask.async.handler;

import cn.lawliex.ask.async.EventHandler;
import cn.lawliex.ask.async.EventModel;
import cn.lawliex.ask.async.EventType;
import cn.lawliex.ask.model.Message;
import cn.lawliex.ask.service.MessageService;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.SystemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Terence on 2017/1/5.
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Override
    public void handleEvent(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(SystemInfo.SYSTEM_ID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setContent(userService.getUser(eventModel.getActorId()).getName() + "关注了你");

        String conversationId = "";
        if(SystemInfo.SYSTEM_ID > eventModel.getEntityOwnerId())
            conversationId += eventModel.getEntityOwnerId() + "_" + SystemInfo.SYSTEM_ID;
        else
            conversationId += SystemInfo.SYSTEM_ID + "_" + eventModel.getEntityOwnerId();
        message.setConversationId(conversationId);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getFollowEvent() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
