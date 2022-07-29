package com.jacques.monopoly;

import com.jacques.savedata.BoardData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Events {
    static BoardData board;
    static List<Player> playerOrder;


            /*
        Events
        1 - Advance to square (square number)
        2 - Advance to first square in list (rent multiplier, list of squares)
        3 - Change relative position (spaces)
        4 - Modify money (money)
        5 - Get out of Jail Card ()
        6 - Go to jail ()
        7 - Property Development (house price, hotel price)
        8 - Pay everyone (amount)
        9 - Receive money from everyone (amount)
         */

    public static Action executeEvent(Player player, int eventType, Action action){
        action = executeEvent(player, eventType, new int[]{0}, action);
        return action;
    }

    public static void setBoard(BoardData board, List<Player> playerOrder){
        Events.board = board;
        Events.playerOrder = playerOrder;
    }


    public static Action executeEvent(Player player, int eventType, int[] params, Action action){

        switch (eventType){
            case 1:
                int newPosition = params[0];
                if (newPosition < player.getPosition()){
                    player.passGo();
                    player.setPosition(newPosition);
                }
                action.addSpace(params[0]);
                action = board.getTile(newPosition).action(player,action);
                break;
            case 2:
                List<Integer> spaces = new ArrayList<>();
                for (int i = 1; i < params.length; i++) {
                    spaces.add(params[i]);
                }
                Collections.sort(spaces);
                int lowestNumber = spaces.get(0);
                int destination = -1;
                for (int i : spaces){
                    if (i > player.getPosition()){
                        destination = i;
                        break;
                    }
                }
                if (destination == -1){
                    destination = lowestNumber;
                    player.passGo();
                }
                player.setPosition(destination);
                action.addSpace(destination);
                if (board.getTile(player.getPosition()) instanceof OwnershipTile){
                    action = ((OwnershipTile) board.getTile(player.getPosition())).action(player,action,params[0]);
                }
                else {
                    action = board.getTile(player.getPosition()).action(player, action);
                }
                break;
            case 3:
                player.appendPosition(params[0]);
                if (player.getPosition() < 0){
                    player.appendPosition(board.boardSize);
                }
                else if (player.getPosition() > board.boardSize){
                    player.appendPosition(-board.boardSize);
                }
                board.getTile(player.getPosition()).action(player, new Action());
                action.addSpace(player.getPosition());
                break;
            case 4:
                player.modifyMoney(params[0]);
                break;
            case 5:
                break;
            case 6:
                player.putInJail(board.jailSquare);
                action.goingToJail = true;
                action.playerTurnOver = true;
                break;
            case 7:
                int cost = 0;
                int housePrice = params[0];
                int hotelPrice = params[1];
                for(OwnershipTile place : player.ownedProperty){
                    if (place instanceof Property){
                        Property p = (Property) place;
                        if (p.houses < 5){
                            cost += (p.houses*housePrice);
                        }
                        else if (p.houses == 5){
                            cost += hotelPrice;
                        }
                    }
                }
                player.debitMoney(cost);
                System.out.println(player.name + " paid £" + cost);
                break;
            case 8:
                for (Player otherPlayer : playerOrder){
                    if (otherPlayer.isActive() && otherPlayer != player){
                        int transAmount = player.debitMoney(params[0],otherPlayer);
                        otherPlayer.modifyMoney(transAmount);
                        System.out.println(player.name + " paid £" + transAmount + " to " + otherPlayer.name);
                    }
                }
                break;
            case 9:
                for (Player otherPlayer : playerOrder){
                    if (otherPlayer.isActive() && otherPlayer != player){
                        int transAmount = otherPlayer.debitMoney(params[0],player);
                        player.modifyMoney(transAmount);
                        System.out.println(otherPlayer.name + " paid £" + transAmount + " to " + player.name);
                    }
                }
                break;
        }
        return action;
    }
}
