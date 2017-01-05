package cn.lawliex.ask.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Terence on 2017/1/5.
 */
public class EventModel {
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;
    private EventType type;
    private Map<String,String> exts = new HashMap<>();

    public EventModel setActorId(int actorId){
        this.actorId = actorId;
        return this;
    }
    public EventModel setEntityType(int entityType){
        this.entityType = entityType;
        return this;
    }
    public EventModel setEntityId(int entityId){
        this.entityId = entityId;
        return this;
    }
    public EventModel setEntityOwnerId(int ownerId){
        this.entityOwnerId = ownerId;
        return this;
    }
    public EventModel setType(EventType type){
        this.type = type;
        return this;
    }
    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }
    public String getExt(String key){
        return exts.get(key);
    }

    public int getActorId() {
        return actorId;
    }

    public int getEntityType() {
        return entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventType getType() {
        return type;
    }
}
