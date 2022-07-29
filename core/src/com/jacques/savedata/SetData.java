package com.jacques.savedata;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;
import java.util.List;

public class SetData implements Serializable {
    public String identifier;
    public List<Integer> properties;
    private String hex;
    private boolean allowHouses = true;

    public SetData(String identifier, List<Integer> properties, String hex){
        this.identifier = identifier;
        this.properties = properties;
        this.hex = hex;
    }

    public SetData(String identifier, List<Integer> properties, String hex, boolean allowHouses){
        this.identifier = identifier;
        this.properties = properties;
        this.hex = hex;
        this.allowHouses = allowHouses;
    }

    public Color getColor(){
        return Color.valueOf(hex);
    }
}
