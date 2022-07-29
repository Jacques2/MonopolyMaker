package com.jacques.monopoly;

import com.jacques.visualelements.GameScreen;
import com.jacques.visualelements.VisualCharacter;

import java.util.ArrayList;
import java.util.List;

public class Player {
    VisualCharacter vs;
    String name;
    int money = 1500;
    private int position = 0;
    boolean active = true; //becomes false when the player is eliminated
    boolean inJail = false;
    int turnsInJail = 0;
    boolean computerPlayer = false;
    private final List<Card> cards = new ArrayList<>();
    private GameScreen gs;

    public void setGameScreen(GameScreen gs){
        this.gs = gs;
    }

    List<OwnershipTile> ownedProperty = new ArrayList<>();
    public Player(String name){
        this.name = name;
    }

    public void setComputerPlayer(){
        computerPlayer = true;
    }

    public boolean isComputerPlayer(){
        return computerPlayer;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int pos){
        position = pos;
    }

    public void appendPosition(int modifier){
        setPosition(position + modifier);
    }

    public boolean isActive(){
        return active;
    }

    public String getName(){
        return name;
    }

    public int getMoney(){
        return money;
    }

    public void modifyMoney(int amount){
        if (amount == 0) return;
        if (amount < 0){
            debitMoney(-amount);
        }
        else{
            money += amount;
        }
    }

    public void passGo(){
        money += 200;
        System.out.println(name + " has passed GO and collected £200");
    }

    public void obtainProperty(OwnershipTile p){
        ownedProperty.add(p);
    }

    public int debitMoney(int amount){
        return debitMoney(amount, null);
    }
    
    public int debitMoney(int amount, Player playerToGive){
        if (amount == 0){
            return 0;
        }
        int amountDebited = 0;
        if (amount > money){
            boolean sellingPhase = true;
            while (sellingPhase){
                if (!downsize()){//if downsizing fails (nothing to sell)
                    amountDebited = money;
                    eliminate(playerToGive);
                    return amountDebited;
                }
                if (money > amount){
                    sellingPhase = false;
                    money -= amount;
                    amountDebited = amount;
                }
            }
        }
        else {
            money -= amount;
            amountDebited = amount;
        }
        return amountDebited;
    }

    private void eliminate(Player eliminationCause){
        active = false;
        money = 0;
        System.out.println(name + " is out of the game");
        if (eliminationCause == null){
            for (OwnershipTile property: ownedProperty ) {
                property.clearOwner();
            }
            ownedProperty.clear();
        }
        else {
            //for (OwnershipTile propertyToGive: ownedProperty){
            while (ownedProperty.size() > 0){
                OwnershipTile propertyToGive = ownedProperty.get(0);
                tradeProperty(propertyToGive,eliminationCause,0);
                System.out.println(name + " has given " + propertyToGive.name + " to " + eliminationCause.name);
            }
        }
    }

    public void putInJail(int jailSquare){
        inJail = true;
        turnsInJail = 0;
        System.out.println(name + " has been Jailed!");
        position = jailSquare;
        checkJailCards();
    }

    public void leaveJail(){
        inJail = false;
        turnsInJail = 0;
    }

    public boolean incrementJailTime(){
        turnsInJail++;
        if (turnsInJail >= 3){
            debitMoney(50);
            leaveJail();
            System.out.println(name + " has paid £50 to get out of Jail");
            return true;
        }
        System.out.println(name + " did not roll a double to get out of jail");
        return false;
    }

    public boolean checkJailCards(){
        if (!inJail){
            return true;
        }
        for (Card c : cards){
            if (c.eventType == 5){
                cards.remove(c);
                leaveJail();
                System.out.println(name + " used a get out of jail free card!");
                return true;
            }
        }
        return false;
    }

    public boolean downsize(){
        Property mostHouses = null;
        //first look for property with most houses
        for (OwnershipTile tile :ownedProperty){
            if (tile instanceof Property){
                Property p = (Property) tile;
                if (mostHouses == null){
                    mostHouses = p;
                    continue;
                }
                if (p.houses > mostHouses.houses){
                    mostHouses = p;
                }
            }
        }
        //sells a house if possible
        if (mostHouses != null){
            if (mostHouses.houses > 0){
                mostHouses.downgradeWithChecks(this);
                System.out.println(name + " downgraded " + mostHouses.name + " to " + mostHouses.getDevelopmentAmount());
                return true;
            }
        }
        //checks for unmortgaged property
        for (OwnershipTile tile: ownedProperty){
            if (!tile.isMortgaged()){
                System.out.println(name + " mortgaged " + tile.name);
                modifyMoney(tile.mortgage());
                return true;
            }
        }
        return false;
    }

    public List<OwnershipTile> getMortgagedProperty(){
        List<OwnershipTile> mortgagedProperties = new ArrayList<>();
        for(OwnershipTile tile: ownedProperty){
            if (tile.isMortgaged()){
                mortgagedProperties.add(tile);
            }
        }
        return mortgagedProperties;
    }

    public boolean tradeProperty(OwnershipTile property, Player receiver, int fee){
        if (ownedProperty.contains(property)){
            int bankFee = 0;
            if (property.isMortgaged()){
                bankFee = (int) (property.getValue() * 0.1);
            }
            if (receiver.money - fee - bankFee > 0){
                ownedProperty.remove(property);
                receiver.ownedProperty.add(property);
                modifyMoney(receiver.debitMoney(fee,receiver));
                receiver.debitMoney(bankFee);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            System.out.println("This property is not owned by " + name);
            return false;
        }
    }

    public void addCard(Card c){
        cards.add(c);
    }

    public int getPlayerValue(){
        int totalValue = getMoney();
        for (OwnershipTile p : ownedProperty){
            totalValue += p.getValue();
            if (p instanceof Property) {
                totalValue += ((Property) p).getHouseValue();
            }
        }
        return totalValue;
    }

    public void setVS(VisualCharacter c){
        vs = c;
    }

    public VisualCharacter getVS(){
        return vs;
    }

    public List<OwnershipTile> getOwnedProperty(){
        return ownedProperty;
    }

    public List<OwnershipTile> getUndevelopedProperty(){
        List<OwnershipTile> undevelopedTiles = new ArrayList<>();
        for (OwnershipTile tile : ownedProperty){
            if (tile instanceof Property p){
                if (!p.getParentSet().checkForDevelopments()) undevelopedTiles.add(p);
            }
            else undevelopedTiles.add(tile);
        }
        return undevelopedTiles;
    }

    public void removeOwnedProperty(OwnershipTile ot){
        if (ownedProperty.contains(ot)){
            ownedProperty.remove(ot);
            System.out.println(getName() + " no longer owns " + ot.getName());
        }
    }

    public ArrayList<Property> getPropertiesToDevelop(){
        ArrayList<Property> tilesToDevelop = new ArrayList<>();
        for (OwnershipTile tile : ownedProperty){
            if (!(tile instanceof Property)) continue;
            Property p = (Property) tile;
            if (p.getSetOwner() == this && p.getParentSet().hasSameOwner()){
                if (p.getParentSet().checkForUpgrade(p)) tilesToDevelop.add(p);
            }
        }
        return tilesToDevelop;
    }

    @Override
    public String toString(){
        return this.getName();
    }

}
