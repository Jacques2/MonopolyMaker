package com.jacques.monopoly;

import com.badlogic.gdx.graphics.Color;
import com.jacques.visualelements.HouseVisual;

import java.util.Arrays;
import java.util.List;

public class Property extends OwnershipTile {
    int upgradeCost;
    int[] rents;
    transient int houses = 0;
    HouseVisual houseVisual;

    public int getRent(double eventMultiplier){
        if (isMortgaged()){
            System.out.println(name + " is mortgaged");
            return 0;
        }
        int rentToPay = (int) (rents[houses] * eventMultiplier);
        if (houses == 0 & getSetOwner() != null){
            rentToPay *= 2;
        }
        return rentToPay;
    }

    public Property(String name, int value, int[] rents, int index){
        super(name, value, index);
        this.rents = rents;
        this.upgradeCost = value/2;
    }

    public Property(String name, int value, int upgradeCost, int[] rents, int index, int x, int y, float rotation){
        super(name, value, index);
        this.rents = rents;
        this.upgradeCost = upgradeCost;
        houseVisual = new HouseVisual(x,y,rotation);
    }

    public boolean hasVisual(){
        return houseVisual != null;
    }

    public HouseVisual getHouseVisual() {
        return houseVisual;
    }

    public void clearOwner(){
        super.clearOwner();
        houses = 0;
    }

    public void upgradeWithChecks(Player p){
        if (p.getMoney() >= upgradeCost && houses < 5){
            p.debitMoney(upgradeCost);
            houses++;
            updateHouseVisual();
        }
    }

    public void downgradeWithChecks(Player p){
        if (houses > 0){
            houses--;
            updateHouseVisual();
            p.modifyMoney(upgradeCost/2);
        }
    }

    private void updateHouseVisual(){
        if (hasVisual()){
            houseVisual.setState(houses-1);
        }
    }

    public String getDevelopmentAmount(){
        List<String> developmentTitles = Arrays.asList("no houses","1 house","2 houses","3 houses","4 houses","a hotel");
        return developmentTitles.get(houses);
    }

    public int getHouseValue(){
        return houses * upgradeCost;
    }

    public Color getPropertyColor(){
        if (parentSet == null) return Color.BLACK;
        return parentSet.getSetColor();
    }

    public Player getSetOwner(){
        return parentSet.getSetOwner();
    }

    public Set getParentSet() {
        return parentSet;
    }
}
