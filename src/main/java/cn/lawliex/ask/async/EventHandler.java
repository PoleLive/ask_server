package cn.lawliex.ask.async;

import java.util.List;

/**
 * Created by Terence on 2017/1/5.
 */
public interface EventHandler {
    void handleEvent(EventModel eventModel);
    List<EventType> getFollowEvent();
}
