package com.jacques.savedata;

import com.badlogic.gdx.math.Vector2;
import com.jacques.monopoly.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BoardData implements Serializable {
    public transient static final int PROPERTY_TILE = 0;
    public transient static final int STATION_TILE = 1;
    public transient static final int UTILITY_TILE = 2;
    public transient static final int MONEY_TILE = 3;
    public transient static final int EVENT_TILE = 4;
    public transient static final int CARD_TILE = 5;

    public transient static final int PREFIX = 0;
    public transient static final int SUFFIX = 1;

    public String boardName = "London Monopoly";
    public String currencyName;
    public String currencySymbol;
    public int currencyOrder = BoardData.PREFIX;
    public int boardSize;


    public static transient String symbol;
    public static transient int order;

    public int jailSquare;
    public transient List<Tile> boardTiles = new ArrayList<>();
    public List<SetData> setData = new ArrayList<>();
    public transient ArrayList<Set> sets = new ArrayList<>();
    public List<CardDeck> cardDecks = new ArrayList<>();

    class Character{
        public int id;
        public String name;
        public Path imageLocation;
    }

    private CardDeck getCardDeckWithName(String name){
        for (CardDeck deck : cardDecks){
            if (deck.deckName.equals(name)) return deck;
        }
        return null;
    }

    public BoardData defaultBoard() {
        BoardData londonBoardData = new BoardData();
        //main board properties
        londonBoardData.boardName = "London Monopoly";
        londonBoardData.currencyName = "Pounds";
        londonBoardData.currencySymbol = "£";
        londonBoardData.currencyOrder = BoardData.PREFIX;
        londonBoardData.boardSize = 40;
        londonBoardData.jailSquare = 10;

        //card decks
        londonBoardData.cardDecks.add(new CardDeck("Chance"));
        londonBoardData.cardDecks.add(new CardDeck("Community Chest"));

        londonBoardData.addTile(new MoneyTile("Go",0,0),new Vector2(908,962));
        londonBoardData.addTile(new Property("Old Kent Road",60,50, new int[]{2,10,30,90,160,250},1,789,109,0),new Vector2(799,968));
        londonBoardData.addTile(new CardTile("Community Chest",2),new Vector2(718,964));
        londonBoardData.addTile(new Property("Whitechapel Road",60,50, new int[]{4,20,60,180,320,450},3,628,112,0),new Vector2(644,971));
        londonBoardData.addTile(new MoneyTile("Income Tax",-200,4),new Vector2(561,967));
        londonBoardData.addTile(new Station("Kings Cross Station",200, new int[]{25,50,100,200},5),new Vector2(473,971));
        londonBoardData.addTile(new Property("The Angel Islington",100,50, new int[]{6,30,90,270,400,550},6,379,105,0),new Vector2(395,971));
        londonBoardData.addTile(new CardTile("Chance",7),new Vector2(319,969));
        londonBoardData.addTile(new Property("Euston Road",100,50, new int[]{6,30,90,270,400,550},8,218,104,0),new Vector2(239,972));
        londonBoardData.addTile(new Property("Pentonville Road",120,50, new int[]{8,40,100,300,450,600},9,137,104,0),new Vector2(147,970));
        londonBoardData.addTile(new MoneyTile("Just Visiting",0,10),new Vector2(11,952));
        londonBoardData.addTile(new Property("Pall Mall",140,100, new int[]{10,50,150,450,625,750},11,105,214,270),new Vector2(31,849));
        londonBoardData.addTile(new Utility("Electric Company",120,12),new Vector2(25,776));
        londonBoardData.addTile(new Property("Whitehall",140,100, new int[]{10,50,150,450,625,750},13,105,377,270),new Vector2(23,690));
        londonBoardData.addTile(new Property("Northumberland Avenue",160,100, new int[]{12,60,180,500,700,900},14,104,457,270),new Vector2(21,608));
        londonBoardData.addTile(new Station("Marylebone Station",200, new int[]{25,50,100,200},15),new Vector2(24,528));
        londonBoardData.addTile(new Property("Bow Street",180,100, new int[]{14,70,200,550,750,950},16,105,621,270),new Vector2(29,447));
        londonBoardData.addTile(new CardTile("Community Chest",17),new Vector2(27,362));
        londonBoardData.addTile(new Property("Marlborough Street",180,100, new int[]{14,70,200,550,750,950},18,105,783,270),new Vector2(24,284));
        londonBoardData.addTile(new Property("Vine Street",200,100, new int[]{16,80,220,600,800,1000},19,105,866,270),new Vector2(22,207));
        londonBoardData.addTile(new MoneyTile("Free Parking",0,20),new Vector2(40,100));
        londonBoardData.addTile(new Property("Strand",220,150, new int[]{18,90,250,700,875,1050},21,216,896,180),new Vector2(149,77));
        londonBoardData.addTile(new CardTile("Chance",22),new Vector2(228,74));
        londonBoardData.addTile(new Property("Fleet Street",220,150, new int[]{18,90,250,700,875,1050},23,367,896,180),new Vector2(313,73));
        londonBoardData.addTile(new Property("Trafalar Square",240,150, new int[]{20,100,300,750,925,1100},24,444,892,180),new Vector2(392,69));
        londonBoardData.addTile(new Station("Fenchurch St Station",200, new int[]{25,50,100,200},25),new Vector2(475,77));
        londonBoardData.addTile(new Property("Leicester Square",260,150, new int[]{22,110,330,800,975,1150},26,612,896,180),new Vector2(554,68));
        londonBoardData.addTile(new Property("Coventry Street",260,150, new int[]{22,110,330,800,975,1150},27,692,893,180),new Vector2(639,75));
        londonBoardData.addTile(new Utility("Water Works",150,28),new Vector2(727,78));
        londonBoardData.addTile(new Property("Piccadilly",280,150, new int[]{22,130,360,850,1025,1200},29,853,893,180),new Vector2(800,74));
        londonBoardData.addTile(new EventTile("Go to Jail",6,30),new Vector2(909,83));
        londonBoardData.addTile(new Property("Regent Street",300,200, new int[]{26,130,390,900,1100,1275},31,892,794,90),new Vector2(931,202));
        londonBoardData.addTile(new Property("Oxford Street",300,200, new int[]{26,130,390,900,1100,1275},32,892,717,90),new Vector2(934,278));
        londonBoardData.addTile(new CardTile("Community Chest",33),new Vector2(927,344));
        londonBoardData.addTile(new Property("Bond Street",320,200, new int[]{28,150,450,1000,1200,1400},34,892,556,90),new Vector2(932,434));
        londonBoardData.addTile(new Station("Liverpool St Station",200, new int[]{25,50,100,200},35),new Vector2(928,528));
        londonBoardData.addTile(new CardTile("Chance",36),new Vector2(928,611));
        londonBoardData.addTile(new Property("Park Lane",350,200, new int[]{35,175,500,1100,1300,1500},37,892,308,90),new Vector2(928,692));
        londonBoardData.addTile(new MoneyTile("Super Tax",-100,38),new Vector2(923,770));
        londonBoardData.addTile(new Property("Mayfair",400,200, new int[]{50,200,600,1400,1700,2000},39,891,148,90),new Vector2(924,853));

        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to Go (Collect £200)",1,new int[]{0});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to Trafalgar Square. If you pass Go, collect £200",1,new int[]{24});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to Mayfair",1,new int[]{39});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to Pall Mall. If you pass Go, collect £200",1,new int[]{11});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to the nearest Station. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled",2,new int[]{2,5,15,25,35});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance to the nearest Station. If unowned, you may buy it from the Bank. If owned, pay wonder twice the rental to which they are otherwise entitled",2,new int[]{2,5,15,25,35});
        londonBoardData.getCardDeckWithName("Chance").addCard("Advance token to nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total ten times amount thrown.",2,new int[]{10,12,28});
        londonBoardData.getCardDeckWithName("Chance").addCard("Bank pays you dividend of £50",4,new int[]{50});
        londonBoardData.getCardDeckWithName("Chance").addCard("Get Out of Jail Free",5);
        londonBoardData.getCardDeckWithName("Chance").addCard("Go Back 3 Spaces",3,new int[]{-3});
        londonBoardData.getCardDeckWithName("Chance").addCard("Go to Jail. Go directly to Jail, do not pass Go, do not collect £200",6);
        londonBoardData.getCardDeckWithName("Chance").addCard("Make general repairs on all your property. For each house pay £25. For each hotel pay £100",7,new int[]{25,100});
        londonBoardData.getCardDeckWithName("Chance").addCard("Speeding fine £15",4,new int[]{-15});
        londonBoardData.getCardDeckWithName("Chance").addCard("Take a trip to Kings Cross Station. If you pass Go, collect £200",1,new int[]{5});
        londonBoardData.getCardDeckWithName("Chance").addCard("You have been elected Chairman of the Board. Pay each player £50",8,new int[]{50});
        londonBoardData.getCardDeckWithName("Chance").addCard("Your building loan matures. Collect £150",4,new int[]{150});

        londonBoardData.getCardDeckWithName("Community Chest").addCard("Advance to Go (Collect £200)",1,new int[]{0});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Bank error in your favour. Collect £200",4,new int[]{200});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Doctor’s fee. Pay £50",4,new int[]{-50});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("From sale of stock you get £50",4,new int[]{50});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Get Out of Jail Free",5);
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Go to Jail. Go directly to jail, do not pass Go, do not collect £200",6);
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Holiday fund matures. Receive £100",4,new int[]{100});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Income tax refund. Collect £20",4,new int[]{20});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("It is your birthday. Collect £10 from every player",9,new int[]{10});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Life insurance matures. Collect £100",4,new int[]{100});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Pay hospital fees of £100",4,new int[]{-100});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Pay school fees of £50",4,new int[]{-50});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("Receive £25 consultancy fee",4,new int[]{25});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("You are assessed for street repairs. £40 per house. £115 per hotel",7,new int[]{40,115});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("You have won second prize in a beauty contest. Collect £10",4,new int[]{10});
        londonBoardData.getCardDeckWithName("Community Chest").addCard("You inherit £100",4,new int[]{100});

        londonBoardData.setData.add(new SetData("Brown", Arrays.asList(1,3),"864C38FF"));
        londonBoardData.setData.add(new SetData("Light Blue", Arrays.asList(6,8,9),"ACDCF0FF"));
        londonBoardData.setData.add(new SetData("Pink", Arrays.asList(11,13,14),"C53884FF"));
        londonBoardData.setData.add(new SetData("Orange", Arrays.asList(16,18,19),"EC8B2CFF"));
        londonBoardData.setData.add(new SetData("Red", Arrays.asList(21,23,24),"DB2428FF"));
        londonBoardData.setData.add(new SetData("Yellow", Arrays.asList(26,27,29),"FFF004FF"));
        londonBoardData.setData.add(new SetData("Green", Arrays.asList(31,32,34),"13A857FF"));
        londonBoardData.setData.add(new SetData("Dark Blue", Arrays.asList(37,39),"0066A4FF"));
        londonBoardData.setData.add(new SetData("Utilities", Arrays.asList(12,28),"000000FF",false));
        londonBoardData.setData.add(new SetData("Stations", Arrays.asList(5,15,25,35),"000000FF",false));

        return londonBoardData;
    }

    public void shuffleDecks(){
        for (CardDeck d : cardDecks){
            d.shuffle();
        }
        for (CardTile c : cardTiles){
            for (CardDeck deck : cardDecks){
                if (Objects.equals(deck.deckName, c.getName())){
                    c.setDeck(deck);
                    break;
                }
            }
        }
    }

    private ArrayList<OwnershipTile> getPropertiesFromIndexes(List<Integer> intList){
        ArrayList<OwnershipTile> propertiesFromList = new ArrayList<>();
        for(int i: intList){
            for (Tile t: boardTiles){
                if (t.getIndex() == i && t instanceof OwnershipTile){
                    propertiesFromList.add((OwnershipTile) t);
                    break;
                }
            }
        }
        return propertiesFromList;
    }

    public void setDataToSets(){
        sets = new ArrayList<>();
        for (SetData set : setData) {
            sets.add(new Set(set.identifier,set.getColor(),getPropertiesFromIndexes(set.properties)));
        }
    }

    public Tile getTile(int index){
        for (Tile t : boardTiles){
            if (t.getIndex() == index){
                return t;
            }
        }
        return null;
    }

    private void addCard(String deckName, String description, int eventType){
        addCard(deckName,description,eventType,new int[]{});
    }

    public List<Property> properties = new ArrayList<>();
    public List<Station> stations = new ArrayList<>();
    public List<Utility> utilities = new ArrayList<>();
    public List<MoneyTile> moneyTiles = new ArrayList<>();
    public List<EventTile> eventTiles = new ArrayList<>();
    public List<CardTile> cardTiles = new ArrayList<>();

    private void addTile(Tile t,Vector2 position){
        t.setPosition(position);
        if (t instanceof Property){
            properties.add((Property) t);
        }
        if (t instanceof Station){
            stations.add((Station) t);
        }
        if (t instanceof Utility){
            utilities.add((Utility) t);
        }
        if (t instanceof MoneyTile){
            moneyTiles.add((MoneyTile) t);
        }
        if (t instanceof EventTile){
            eventTiles.add((EventTile) t);
        }
        if (t instanceof CardTile){
            cardTiles.add((CardTile) t);
        }
    }

    public void tilesToBoard(){
        boardTiles = new ArrayList<>();
        for (Property p : properties){
            boardTiles.add(p);
        }
        for (Station p : stations){
            boardTiles.add(p);
        }
        for (Utility p : utilities){
            boardTiles.add(p);
        }
        for (MoneyTile p : moneyTiles){
            boardTiles.add(p);
        }
        for (EventTile p : eventTiles){
            boardTiles.add(p);
        }
        for (CardTile p : cardTiles){
            boardTiles.add(p);
        }
        symbol = currencySymbol;
        order = currencyOrder;
    }

    private void addCard(String deckName, String description, int eventType, int[] params){
        for (CardDeck d : cardDecks){
            if (Objects.equals(d.deckName, deckName)){
                d.addCard(description,eventType,params);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardData)) return false;
        BoardData board1 = (BoardData) o;
        return Objects.equals(boardName, board1.boardName) &&
                Objects.equals(boardName, board1.boardName);
    }

    public static void serialize(BoardData boardData, String filename) {
        try {
            ObjectOutputStream out = new ObjectOutputStream((new FileOutputStream(filename)));
            out.writeObject(boardData);
            System.out.println("Serialisation successful.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BoardData deserialize(String filename){
        BoardData boardData = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            boardData = (BoardData) in.readObject();
            System.out.println("De-serialisation successful.");
        } catch (IOException e) {
            System.out.println(new File(".").getAbsolutePath());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return boardData;
    }

    private Tile getTileWithIndex(int index){
        System.out.println("search");
        for (Tile t : boardTiles){
            System.out.println(index+"/"+t.getIndex());
            if(t.getIndex() == index){
                return t;
            }
        }
        return null;
    }

    private void setPositionForTileWithIndex(int index, Vector2 position){
        Tile tile = getTileWithIndex(index);
        if (tile == null) return;
        tile.setPosition(position);
        System.out.println("pos set");
    }
}
