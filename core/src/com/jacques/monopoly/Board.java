package com.jacques.monopoly;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacques.savedata.*;
import com.jacques.visualelements.HouseVisual;
import com.jacques.visualelements.MonopolyMaker;
import com.jacques.visualelements.VisualCharacter;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    public static final String BOARD_DIR = Paths.get("").toAbsolutePath().toString() +"/boards";

    public static String workingBoard;

    public BoardData boardData;
    public static Random random = new Random();
    public boolean gameInProgress = true;
    public List<Player> playerOrder = new ArrayList<>();
    Dice dice = new Dice();

    int maxTurns = 100; //temp for testing

    public ZipFile boardZip;

    public Board(){
        boardData = new BoardData();
        String path = Paths.get("").toAbsolutePath() + "\\core\\currentboard\\board";

        //.dat deserialisation
        //boardData = BoardData.deserialize(path+".dat");
        //json deserialisation - perfect for rapid debugging


//        try{
//            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//            BufferedReader reader;
//            try{
//                InputStream in = getClass().getResourceAsStream("/core/currentboard/board.json");
//                reader = new BufferedReader(new InputStreamReader(in));
//            }
//            catch (Exception e){
//                reader = Files.newBufferedReader(new File(path+".json").toPath(), Charset.forName("UTF-8"));
//            }
//            Type t = new TypeToken<BoardData>(){}.getType();
//            boardData = gson.fromJson(reader, t);
//            reader.close();
//            System.out.println("successful json read");
//        }
//        catch (Exception e){
//            System.out.println("json read failed");
//            e.printStackTrace();
//        }


        //File reading from zip to objects - Use this for final

        try{
            boardZip = new ZipFile(workingBoard);
            System.out.println("Loading board from location: " + workingBoard);
            FileHeader fileHeader = boardZip.getFileHeader("data.json");
            //System.out.println(boardZip.getFileHeaders().size());
            InputStream inputStream = boardZip.getInputStream(fileHeader);
            Reader r = new InputStreamReader(inputStream);
            Type t = new TypeToken<BoardData>(){}.getType();
            boardData = new Gson().fromJson(r,t);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        if (MonopolyMaker.DEBUG_MODE) random = new Random(20);


        Events.setBoard(this.boardData,this.playerOrder);

        boardData.tilesToBoard();

        boardData.setDataToSets();

        boardData.shuffleDecks();

        HouseVisual.setTextures(new Texture("core/assets/house1.png"),new Texture("core/assets/house2.png"),new Texture("core/assets/house3.png"),new Texture("core/assets/house4.png"),new Texture("core/assets/house5.png"));
    }

    public List<HouseVisual> getAllHouseVisuals(){
        List<HouseVisual> visuals = new ArrayList<>();
        for (Tile t: boardData.boardTiles){
            if(t instanceof Property){
                Property p = (Property) t;
                if (p.hasVisual()){
                    visuals.add(p.getHouseVisual());
                    p.getHouseVisual().setState(-1);
                }
            }
        }
        return visuals;
    }

    public void addPlayers(ArrayList<VisualCharacter> players){
        Player masterPlayer = players.get(0).player;
        for (Tile o : boardData.boardTiles){
            if (o instanceof OwnershipTile && MonopolyMaker.DEBUG_MODE){
                //debug line to give all properties to first player
                //((OwnershipTile) o).setPropertyOwner(masterPlayer);
                //debug lines to give properties to random players
                int r = random.nextInt(players.size());
                ((OwnershipTile) o).setPropertyOwner(players.get(r).player);
            }
        }
        for (VisualCharacter p : players){
            playerOrder.add(p.player);
        }
    }

    private void incrementPlayerTurn(){
        currentPlayerPosition++;
        playerDoubles = 0;
        if (currentPlayerPosition >= playerOrder.size()){
            currentPlayerPosition = 0;
        }
    }
    
    private int currentPlayerPosition = 0;
    public Action gameLoop(){
        Action currentAction = new Action();
        if (gameInProgress & maxTurns > 0){
            //maxTurns--; //used for debugging
            boolean isNewPlayerTurn = incrementPlayerTurnIfOver();
            currentAction = rollAndMove(playerOrder.get(currentPlayerPosition));
            if (currentAction == null){
                currentAction = new Action();
                System.out.println(playerOrder.get(0).name + " has won the game");
                currentAction.hasGameEnded = true;
                currentAction.messageboxmessage = playerOrder.get(0).name + " has won the game";
                gameInProgress = false;
            } else if (playerOrder.size() < 1) {
                System.out.println("Abrupt game ending");
                gameInProgress = true;
            }
            currentAction.isNewPlayerTurn = isNewPlayerTurn;
            playerOrder.removeIf(i -> !i.isActive());
        }
        return currentAction;
    }

    public boolean incrementPlayerTurnIfOver(){
        if (isCurrentPlayerTurnOver){
            incrementPlayerTurn();
            isCurrentPlayerTurnOver = false;
            return true;
        }
        return false;
    }

    public Player getPlayerTurn(){
        try {
            return playerOrder.get(currentPlayerPosition);
        }
        catch (Exception e){
            return null;
        }
    }

    private int playerDoubles = 0;
    private boolean isCurrentPlayerTurnOver = true;
    public Action rollAndMove(Player currentPlayer){
        Action currentAction = new Action();
        currentAction.player = currentPlayer;
        if (currentPlayer.isActive()){
            if (currentPlayer.computerPlayer) buildingPhase(currentPlayer);
            DicePair diceRoll = dice.rollDice();
            currentAction.diceRoll = diceRoll;
            if (playerDoubles < 3) {
                if (diceRoll.isDouble()){
                    System.out.println(currentPlayer.name + " rolled a double");
                    playerDoubles++;
                    if (playerDoubles >= 3){
                        System.out.println(currentPlayer.name +  " rolled 3 doubles in a row!");
                        currentPlayer.putInJail(boardData.jailSquare);
                        currentAction.playerTurnOver = true;
                    }
                }
                if (jailCheck(currentPlayer,diceRoll)){
                    int prevPosition = currentPlayer.getPosition();
                    int newposition = prevPosition + diceRoll.getDiceValue();
                    if (newposition > boardData.boardSize - 1){ //check if passed go
                        newposition -= boardData.boardSize;
                        currentPlayer.passGo();
                    }
                    currentPlayer.setPosition(newposition);
                    for (int i = prevPosition; i != currentPlayer.getPosition();) {
                        i++;
                        if (i >= boardData.boardSize){
                            i -= boardData.boardSize;
                        }
                        currentAction.addSpace(i);
                    }
                    System.out.println(currentPlayer.getName() + " -> " + boardData.getTile(currentPlayer.getPosition()).name);
                    currentAction = boardData.getTile(currentPlayer.getPosition()).action(currentPlayer, currentAction);
                }
                else {
                    currentAction.stillInJail = true;
                    currentAction.playerTurnOver = true;
                }
            }
            if (!diceRoll.isDouble()){
                isCurrentPlayerTurnOver = true;
            }
        }
        if (getActivePlayers() <= 1){
            System.out.println("game ending");
            return null;
        }
        return currentAction;
    }

    public int getActivePlayers(){
        int activePlayers = 0;
        for (Player p : playerOrder){
            if (p.isActive()){
                activePlayers++;
            }
        }
        return activePlayers;
    }

    public boolean jailCheck(Player currentPlayer, DicePair diceRoll){
        if(currentPlayer.checkJailCards()) return true;
        if (currentPlayer.inJail){
            if (diceRoll.isDouble()){
                currentPlayer.leaveJail();
                System.out.println(currentPlayer.name + " has rolled a double to leave Jail");
                diceRoll.invalidateDouble();
            }
            else {
                return currentPlayer.incrementJailTime();
            }
        }
        return true;
    }

    public void buildingPhase(Player currentPlayer){
        boolean mortgagePhase;
        do {
            List<OwnershipTile> mortgagePossibilities = currentPlayer.getMortgagedProperty();
            if (mortgagePossibilities.size() == 0) {
                break;
            }
            mortgagePhase = false;
            for (OwnershipTile tile: mortgagePossibilities){
                int unmortgageCost = (int) ((tile.getValue() * OwnershipTile.mortgageMultiplier) * 1.1);
                if (currentPlayer.money - unmortgageCost > 100){
                    currentPlayer.debitMoney(tile.unmortgage(currentPlayer));
                    System.out.println(currentPlayer.name + " unmortgaged " + tile.name);
                    mortgagePhase = true;
                    break;
                }
            }
        }
        while (mortgagePhase);
        boolean houseBuildingPhase;
        do {
            ArrayList<Property> housePossibilities = currentPlayer.getPropertiesToDevelop();
            if (housePossibilities.size() == 0) {
                break;
            }
            houseBuildingPhase = false;
            for (Property p: housePossibilities){
                if (currentPlayer.money - p.upgradeCost > 100){
                    p.upgradeWithChecks(currentPlayer);
                    System.out.println(currentPlayer.name + " upgraded " + p.name + " to " + p.getDevelopmentAmount() + " (Money: £" +currentPlayer.money + ")");
                    houseBuildingPhase = true;
                    break;
                }
            }
        }
        while (houseBuildingPhase);
    }


    public List<Player> getPlayerOrder() {
        return playerOrder;
    }
}
