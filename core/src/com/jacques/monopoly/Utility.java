package com.jacques.monopoly;

public class Utility extends OwnershipTile {

    public Utility(String name, int value, int index) {
        super(name, value, index);
    }

    @Override
    public int getRent(double eventMultiplier) {
        int latestRoll = Dice.latestDice.getDiceValue();
        if (eventMultiplier != 1){
            return (int) (latestRoll * eventMultiplier);
        }
        if (getNumberOwnedPropertiesInSet() == 2){
            return latestRoll * 10;
        }
        else {
            return latestRoll * 4;
        }
    }
}
