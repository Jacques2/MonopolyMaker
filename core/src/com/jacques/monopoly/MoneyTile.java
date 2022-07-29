package com.jacques.monopoly;

import com.jacques.savedata.BoardData;

import java.io.Serializable;

public class MoneyTile extends Tile implements Serializable {
    int moneyModifier;
    public MoneyTile(String name, int moneyModifier, int index){
        this.name = name;
        this.moneyModifier = moneyModifier;
        this.index = index;
    }
    @Override
    public Action action(Player currentPlayer, Action action) {
        currentPlayer.modifyMoney(moneyModifier);
        if (moneyModifier != 0){
            if (moneyModifier < 0){
                action.messageboxmessage = "-";
                moneyModifier *= -1;
            }
            else {
                action.messageboxmessage = "+";
            }
            if (BoardData.order == 0){
                action.messageboxmessage += BoardData.symbol + moneyModifier;
            }
            else{
                action.messageboxmessage += moneyModifier + BoardData.symbol;
            }
            action.messageboxmessage += ".";
        }
        return action;
    }
}
