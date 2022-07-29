package com.jacques.monopoly;

public class Dice {
    static DicePair latestDice;
    public static boolean enabled = true;
    static int forced1, forced2;
    public static DicePair rollDice(){
        DicePair newDice = new DicePair();
        int roll1 = Board.random.nextInt(6) + 1;
        int roll2 = Board.random.nextInt(6) + 1;
        if (!enabled){
            roll1 = forced1;
            roll2 = forced2;
        }
        newDice.addRoll(roll1 + roll2);
        newDice.setDice(roll1,roll2);
        if (roll1 == roll2){
            newDice.setDouble();
        }
        latestDice = newDice;
        return newDice;
    }
    public static void setDice(int d1, int d2){
        forced1 = d1;
        forced2 = d2;
    }
}
