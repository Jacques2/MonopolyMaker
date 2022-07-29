package com.jacques.monopoly;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class Set {
    private String setName;
    private Color setColor = Color.BLACK;
    ArrayList<OwnershipTile> properties = new ArrayList<>();
    private boolean allowHouses = true;

    public void isHousesAllowed(){
        allowHouses = false;
    }

    public boolean allowHouses() {
        return allowHouses;
    }

    public void addProperty(Property p){
        properties.add(p);
        p.setParent(this);
    }

    public Set(String setName, Color setColor, ArrayList<OwnershipTile> properties){
        this.setName = setName;
        this.setColor = setColor;
        this.properties = properties;
        for(OwnershipTile p : properties){
            p.setParent(this);
        }
    }

    public boolean checkForUpgrade(Property p){
        if (p.houses >= 5) return false;
        Double minimumHouses = Double.POSITIVE_INFINITY;
        for (OwnershipTile property : properties){
            if (property instanceof Property && minimumHouses > ((Property) property).houses){
                minimumHouses = (double) ((Property) property).houses;
            }
        }
        return p.houses == minimumHouses;
    }

    public boolean checkForDowngrade(Property p){
        if (p.houses <= 0) return false;
        Double maximumHouses = Double.NEGATIVE_INFINITY;
        for (OwnershipTile property : properties){
            if (property instanceof Property && maximumHouses < ((Property) property).houses){
                maximumHouses = (double) ((Property) property).houses;
            }
        }
        System.out.println(p.houses);
        System.out.println(maximumHouses);
        return p.houses == maximumHouses;
    }

    public Color getSetColor() {
        return setColor;
    }

    public boolean hasSameOwner(){
        Player owner = null;
        if (properties.size() == 0){
            return false;
        }
        owner = properties.get(0).owner;
        if (owner == null) return false;
        for (OwnershipTile p : properties){
            if (!owner.equals(p.owner)) return false;
        }
        return true;
    }

    public Player getSetOwner(){
        if (hasSameOwner()) return properties.get(0).owner;
        return null;
    }

    public boolean checkForDevelopments(){
        for (OwnershipTile tile : properties){
            if (tile instanceof Property p){
                if (p.houses > 0) return true;
            }
        }
        return false;
    }
}
