package com.jacques.monopoly;

import java.io.Serializable;

public abstract class OwnershipTile extends Tile implements Serializable, Comparable<OwnershipTile> {
    transient boolean isOwned;
    transient Player owner = null;
    transient private boolean isMortgaged;
    private int value;
    public static final transient double mortgageMultiplier = 0.5;
    Set parentSet;

    public OwnershipTile(String name, int value, int index){
        this.name = name;
        this.value = value;
        isOwned=false;
        isMortgaged = false;
        this.index = index;
    }

    public void clearOwner(){
        isOwned = false;
        owner = null;
        isMortgaged = false;
    }

    public Action action(Player currentPlayer, Action action){
        action = action(currentPlayer,action, 1);
        return action;
    }

    public void buyProperty(Player currentPlayer){
        currentPlayer.modifyMoney(-value);
        System.out.println(currentPlayer.name + " buys " + name + " for £" + value);
        setPropertyOwner(currentPlayer);
    }

    public void transferOwnership(Player newPlayer){
        owner.removeOwnedProperty(this);
        setPropertyOwner(newPlayer);
    }

    public void setPropertyOwner(Player currentPlayer){
        owner = currentPlayer;
        isOwned = true;
        currentPlayer.obtainProperty(this);
    }

    public Action action(Player currentPlayer, Action action, double eventMultiplier) {
        if (!isOwned){
            if (currentPlayer.getMoney() > value){ //checks to buy
                action.propertyPurchaseDecision = this;
            }
        }
        else{
            if (owner == currentPlayer){
                return action;
            }
            int rentToPay = getRent(eventMultiplier);
            int amountPaid = currentPlayer.debitMoney(rentToPay,owner);
            owner.modifyMoney(amountPaid);
            System.out.println(currentPlayer.name + " paid rent of £" + amountPaid);
        }
        return action;
    }

    public boolean isMortgaged(){
        return isMortgaged;
    }

    public int mortgage(){
        if (!isMortgaged){
            isMortgaged = true;
            return (int) (value * mortgageMultiplier);
        }
        return 0;
    }

    public int getUnmortgageCost(){
        return (int) ((value * OwnershipTile.mortgageMultiplier) * 1.1);
    }

    public boolean canAffordUnmortgage(Player player){
        return player.getMoney() >= getUnmortgageCost();
    }

    public int unmortgage(Player player){
        if (player.getMoney() >= getUnmortgageCost()){
            isMortgaged = false;
            return getUnmortgageCost();
        }
        return 0;
    }

    public abstract int getRent(double eventMultiplier);

    @Override
    public int compareTo(OwnershipTile ot){
        return this.index - ot.index;
    }

    public void setParent(Set s){
        parentSet = s;
    }

    public Set getParentSet() {
        return parentSet;
    }

    public int getNumberOwnedPropertiesInSet(){
        int prop = 0;
        for (OwnershipTile ot : getParentSet().properties){
            if (ot.owner != null && ot.owner.equals(this.owner)) prop++;
        }
        return prop;
    }

    public int getValue() {
        return value;
    }
}
