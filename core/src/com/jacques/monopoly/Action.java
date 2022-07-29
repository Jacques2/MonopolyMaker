package com.jacques.monopoly;

import java.util.ArrayList;
import java.util.List;

public class Action {
    public Player player;
    public List<Integer> spacesMoved = new ArrayList<>();
    public boolean goingToJail = false;
    public OwnershipTile propertyPurchaseDecision = null;
    public DicePair diceRoll;
    public boolean isNewPlayerTurn = false;
    public String messageboxmessage = null;
    public boolean stillInJail = false;
    public boolean hasGameEnded = false;
    public boolean playerTurnOver = false;

    public void addSpace(int space){
        spacesMoved.add(space);
    }
}
