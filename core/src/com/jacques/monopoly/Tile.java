package com.jacques.monopoly;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;

public abstract class Tile implements Serializable {
    protected String name;
    int index;
    private int x;
    private int y;
    public abstract Action action(Player currentPlayer, Action action);
    public int getIndex(){
        return index;
    }
    public String getName(){
        return name;
    }
    public Vector2 getPosition(){
        return new Vector2(x,y);
    }
    public void setPosition(Vector2 position){
        x = (int) position.x;
        y = (int) position.y;
    }
}
