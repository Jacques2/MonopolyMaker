package com.jacques.monopoly;

public class Station extends OwnershipTile{
    int[] rents;

    public Station(String name, int value, int[] rents, int index){
        super(name, value, index);
        this.rents = rents;
    }

    @Override
    public int getRent(double eventMultiplier) {
        int totalStations = getNumberOwnedPropertiesInSet()-1;
        try{
            return (int) (rents[totalStations] * eventMultiplier);
        }
        catch (Exception ex){
            return 0;
        }
    }
}
