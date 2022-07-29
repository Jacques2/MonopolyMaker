package com.jacques.monopoly;

public class EventTile extends Tile{
    int eventType;
    int[] param;

    public EventTile(String name, int eventType, int index){
        this.name = name;
        this.eventType = eventType;
        this.index = index;
        this.param = new int[]{0};
    }
    EventTile(String name, int eventType, int index, int[] param){
        this.name = name;
        this.eventType = eventType;
        this.index = index;
        this.param = param;
    }
    @Override
    public Action action(Player currentPlayer, Action action) {
        action = Events.executeEvent(currentPlayer,eventType,param,action);
        return action;
    }
}
