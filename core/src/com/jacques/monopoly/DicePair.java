package com.jacques.monopoly;

public class DicePair {
    private int diceValue = 0;
    private boolean isDouble = false;
    private final IndividualDice dicePair = new IndividualDice();

    public DicePair(){}

    public DicePair(int d1,int d2){
        dicePair.dice1 = d1;
        dicePair.dice2 = d2;
    }

    public void addRoll(int value){
        diceValue += value;
    }
    public void setDouble(){
        isDouble = true;
    }
    public boolean isDouble(){
        return isDouble;
    }
    public int getDiceValue(){
        return diceValue;
    }
    public void invalidateDouble(){ //used for escaping jail but not gaining a second turn
        isDouble = false;
    }
    public void setDice(int dice1, int dice2){
        dicePair.dice1 = dice1;
        dicePair.dice2 = dice2;
    }

    public boolean hasDicePair(){
        return dicePair != null;
    }

    public IndividualDice getDicePair(){
        return dicePair;
    }

    public class IndividualDice{
        public int dice1;
        public int dice2;
    }

}
