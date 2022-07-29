package com.jacques.monopoly;

import java.io.Serializable;
import java.util.*;


class Card implements Serializable{
    String description;
    int eventType;
    int[] param;
    transient Queue<Card> shuffledCardDeck;
    Card(String description, int eventType, int[] params){
        this.description = description;
        this.eventType = eventType;
        this.param = params;
    }
    public void returnToPile(){
        if (shuffledCardDeck != null){
            shuffledCardDeck.add(this);
            shuffledCardDeck = null;
        }
    }
}


public class CardDeck implements Serializable {

    public String deckName;
    String cardType;
    List<Card> cardDeck = new ArrayList<>();
    transient Queue<Card> shuffledCardDeck = new LinkedList<>();

    public CardDeck(String deckName){
        this.deckName = deckName;
        //generateCardSet();
    }

    public void shuffle(){
        Collections.shuffle(cardDeck,Board.random);
        shuffledCardDeck = new LinkedList<>();
        for(Card c: cardDeck){
            shuffledCardDeck.add(c);
        }
    }

    public void addCard(String description, int eventType){
        cardDeck.add(new Card(description,eventType, new int[]{}));
    }

    public void addCard(String description, int eventType, int[] params){
        cardDeck.add(new Card(description,eventType,params));
    }

    public Action action(Player currentPlayer, Action action) {
        Card currentCard = shuffledCardDeck.remove();
        System.out.println(deckName + ": " + currentCard.description);
        action.messageboxmessage = deckName + ": " + currentCard.description;
        Events.executeEvent(currentPlayer,currentCard.eventType,currentCard.param,action);
        if (currentCard.eventType == 5){
            currentCard.shuffledCardDeck = shuffledCardDeck;
            currentPlayer.addCard(currentCard);
        }
        else {
            shuffledCardDeck.add(currentCard);
        }
        return action;
    }
}
