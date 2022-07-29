package com.jacques.monopoly;

public class CardTile extends Tile {
    transient CardDeck cardDeck;

    public CardTile(String deckName, int index) {
        name = deckName;
        this.index = index;
    }

    public void setDeck(CardDeck cardDeck){
        this.cardDeck = cardDeck;
    }

    @Override
    public Action action(Player currentPlayer, Action action) {
        return cardDeck.action(currentPlayer,action);
    }
}
