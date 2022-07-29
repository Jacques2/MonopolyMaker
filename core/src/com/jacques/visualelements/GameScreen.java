package com.jacques.visualelements;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.jacques.UI.*;
import com.jacques.monopoly.*;
import net.lingala.zip4j.model.FileHeader;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameScreen extends ElementScreen {
    final MonopolyMaker game;
    SpriteBatch batch;
    private Texture monopoly;
    Board board = new Board();
    ArrayList<VisualCharacter> players = new ArrayList<>();
    Vector2 jailPosition = new Vector2(75,929);
    TradeScreen ts;

    Player currentPlayer;
    OwnershipTile currentlySelectedProperty;

    Clickable buttonRollDice;
    Clickable buttonTrade;
    YesNoButton buttonNo;
    YesNoButton buttonYes;
    PropertySelectBox propertiesBox;
    OnScreenText diceRoll = new OnScreenText("This text will be automatically updated with the dice roll", 1050, 960, Color.RED);
    OnScreenText currentPlayerTurn = new OnScreenText("Player turn: ", 1030, 1000, Color.RED);
    OnScreenText currentPlayerMoney = new OnScreenText("Money: ", 1100, 600, Color.RED);
    MultiStateTextButton buttonMortgage;
    MultiStateTextButton buttonUnmortgage;
    MultiStateTextButton buttonBuyHouse;
    MultiStateTextButton buttonSellHouse;

    private final OrthographicCamera camera;

    boolean playerMoving = false;


    public GameScreen (final MonopolyMaker game) {

        /*
        //sample 3 players for debugging
        players.add(new VisualCharacter(new Texture("core/assets/player1.png"),new Player("Player 1")));
        players.add(new VisualCharacter(new Texture("core/assets/player2.png"),new Player("Player 2")));
        players.add(new VisualCharacter(new Texture("core/assets/player3.png"),new Player("Player 3")));
         */

        for (UserData data : game.playerData){
            VisualCharacter newPlayer = new VisualCharacter(new Texture("core/assets/player" + data.sprite + ".png"),new Player(data.username));
            if (data.computerPlayer) newPlayer.player.setComputerPlayer();
            players.add(newPlayer);
        }

        board.addPlayers(players);

        buttonRollDice = new Clickable(new Texture("core/assets/buttonRollDice.png"),1100,800,250,80,"RollDice");
        clickables.add(buttonRollDice);

        buttonTrade = new Clickable(new Texture("core/assets/buttonTrade.png"),1100,80,250,80,"Trade");
        clickables.add(buttonTrade);

        buttonYes = new YesNoButton(new Texture("core/assets/buttonYes.png"),new Texture("core/assets/buttonYesInactive.png"),1100,700,80,80,"Yes");
        buttonYes.disable();
        clickables.add(buttonYes);

        buttonNo = new YesNoButton(new Texture("core/assets/buttonNo.png"),new Texture("core/assets/buttonNoInactive.png"),1250,700,80,80,"No");
        buttonNo.disable();
        clickables.add(buttonNo);

        buttonMortgage = new MultiStateTextButton(new Texture("core/assets/buttonMortgage.png"),new Texture("core/assets/buttonMortgageInactive.png"),1340,395,250,45,"Mortgage");
        buttonMortgage.setElementScreen(this);
        buttonMortgage.setText("Mortgage Property");
        clickables.add(buttonMortgage);

        buttonUnmortgage = new MultiStateTextButton(new Texture("core/assets/buttonUnmortgage.png"),new Texture("core/assets/buttonUnmortgageInactive.png"),1340,345,250,45,"Unmortgage");
        buttonUnmortgage.setElementScreen(this);
        buttonUnmortgage.setText("Unmortgage Property");
        clickables.add(buttonUnmortgage);

        buttonBuyHouse = new MultiStateTextButton(new Texture("core/assets/buttonBuyHouseActive.png"),new Texture("core/assets/buttonBuyHouseInactive.png"),1340,250,90,90,"BuyHouse");
        buttonBuyHouse.setElementScreen(this);
        buttonBuyHouse.setText("");
        clickables.add(buttonBuyHouse);

        buttonSellHouse = new MultiStateTextButton(new Texture("core/assets/buttonSellHouseActive.png"),new Texture("core/assets/buttonSellHouseInactive.png"),1440,250,90,90,"SellHouse");
        buttonSellHouse.setElementScreen(this);
        buttonSellHouse.setText("");
        clickables.add(buttonSellHouse);

        propertiesBox = new PropertySelectBox(this,1000,400,1);
        game.getInputProcessor().addScrollBox(propertiesBox);

        texts.add(diceRoll);
        texts.add(currentPlayerTurn);
        texts.add(currentPlayerMoney);

        ts = new TradeScreen(this,game,board);
        this.game = game;
        batch = new SpriteBatch();
        System.out.println(Gdx.files.getLocalStoragePath());
        try{
            FileHeader fileHeader = board.boardZip.getFileHeader("board.png");
            InputStream inputStream = board.boardZip.getInputStream(fileHeader);
            monopoly = new Texture(new Pixmap(new Gdx2DPixmap(inputStream,Gdx2DPixmap.GDX2D_BLEND_NONE)));
        }
        catch (Exception e){
            System.out.println("Board load failed: using fallback texture");
            monopoly = new Texture(Gdx.files.internal("core/assets/boardFallback.png"));
        }

        houseVisuals = board.getAllHouseVisuals();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 1000);


        for (VisualCharacter vc : players){
            vc.addAction(Actions.moveTo(getCoords(vc.player).x,getCoords(vc.player).y,0));
            vc.player.setGameScreen(this);
        }

    }

    private Vector2 getCoords(Player p){
        int playerPositionIndex = p.getPosition();
        return getCoords(playerPositionIndex);
    }

    private Vector2 getCoords(int index){
        try{
            return board.boardData.getTile(index).getPosition();
        }
        catch (Exception e){
            System.out.println(index);
        }
        return null;
    }

    private DicePair mostRecentRoll;
    public void draw(){
        ScreenUtils.clear(0, 0, 0.2f, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(monopoly,0,0,1000,1000);
        int size = 80;
        for (VisualCharacter vc : players){
            if (vc.player.isActive()){
                batch.draw(vc.GetTexture(),vc.getX()-(size/2),1000-vc.getY()-(size/2),size,size);
            }
        }
        try{
            for (Clickable c : clickables){
                batch.draw(c.skin, c.skin.getX(), c.skin.getY(), c.skin.getWidth(), c.skin.getHeight());
            }
        }
        catch (Exception e){
            System.out.println("error drawing clickables");
        }
        String rollValues = "None";
        if (mostRecentRoll != null){
            if (mostRecentRoll.hasDicePair()){
                rollValues = mostRecentRoll.getDicePair().dice1 + " & " + mostRecentRoll.getDicePair().dice2;
            }
        }
        currentPlayerTurn.setText("Current Player turn: " + currentPlayer.getName());
        diceRoll.setText("Dice Roll: " + rollValues);
        currentPlayerMoney.setText("Money: " + currentPlayer.getMoney());
        try {
            for (OnScreenText text : texts){
                game.font.setColor(text.color);
                game.font.getData().setScale(text.scale,text.scale);
                try{
                    if (text.wrap){
                        game.font.draw(batch, text.text, text.x, text.y,text.targetWidth, Align.left,true);
                    }
                    else{
                        game.font.draw(batch, text.text, text.x, text.y,0,text.getTextLength(),text.targetWidth, Align.left,false,"-");
                    }
                }
                catch (Exception ignored){
                }
            }
        }
        catch (Exception e){
            System.out.println("failed to write all texts this frame");
        }
        for (HouseVisual visual : houseVisuals){
            if (!visual.needToDraw()) continue;
            batch.draw(visual.getTexture(),visual.getX()-(visual.getWidth()/2),visual.getY()-(visual.getHeight()/2),visual.getWidth()/2,visual.getHeight()/2,visual.getWidth(),visual.getHeight(),1,1,visual.getRotation(),0,0,visual.getTextureWidth(),visual.getTextureHeight(),false,false);
        }
        batch.end();
        camera.update();
    }

    private boolean moveStatus = false;
    @Override
    public void render (float delta) {
        for (VisualCharacter player : players) {
            if (!player.hasActions() && player.hasQueuedAction()) {
                Vector2 newCoords = player.nextAction();
                player.addAction(Actions.moveTo(newCoords.x, newCoords.y, 0.3F));
                moveStatus = true;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.J)) {
            throw new HeadlessException();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            int latestChar = game.getInputProcessor().getMostRecentChar();
            if (latestChar >= 8 && latestChar <= 16 && MonopolyMaker.DEBUG_MODE){
                Dice.enabled = false;
                Dice.setDice(Integer.parseInt(Input.Keys.toString(latestChar)),0);
                rollDiceAndMove();
            }
            if (latestChar == Input.Keys.ESCAPE){
                game.setScreen(new MainMenuScreen(game, null));
            }
        }

        if (moveStatus && !hasPlayerActions()){
            moveStatus = false;
            afterAction();
        }

        clickCheck();
        playerChangeCheck();

        if (!playerMoving && currentPlayer.isComputerPlayer()){
            rollDiceAndMove();
        }

        for (VisualCharacter player : players) {
            player.act((float) 0.05);
        }
        draw();
    }

    public void playerChangeCheck(){
        if (!board.gameInProgress) return;
        if (board.getPlayerTurn() == null) return;
        if (board.getPlayerTurn().equals(currentPlayer)){
            return;
        }
        currentPlayer = board.getPlayerTurn();
        propertyRefresh();
    }

    public void propertyRefresh(){
        if (currentPlayer == null) return;
        List<OwnershipTile> ownedProperty = currentPlayer.getOwnedProperty();
        Collections.sort(ownedProperty);
        propertiesBox.setProperties(ownedProperty);
    }

    public void afterAction(){
        if (action != null && action.propertyPurchaseDecision != null){
            if (currentPlayer.isComputerPlayer() && action.propertyPurchaseDecision.getValue() < currentPlayer.getMoney()-100){
                action.propertyPurchaseDecision.buyProperty(action.player);
                propertyRefresh();
            }
            else {
                buttonNo.enable();
                buttonYes.enable();
            }
        }

        playerMoving = false;

        if (action.messageboxmessage != null){
            new MessageBox(this, action.messageboxmessage);
        }
    }

    Action action;
    public void clickCheck(){
        if (Gdx.input.justTouched()){
            Vector3 coordsClicked = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int ix = (int) coordsClicked.x;
            int iy = (int) coordsClicked.y;
            System.out.println("Clicked - X:" + ix + " Y:" + iy);
            for (Clickable c : clickables){
                if (c.checkIfClicked(ix,iy)){
                    String identifier = c.click();
                    System.out.println("Button clicked: " + identifier);
                    if (Objects.equals(identifier, "RollDice")){
                        if (!currentPlayer.isComputerPlayer()){
                            Dice.enabled = true;
                            rollDiceAndMove();
                        }
                    }
                    if (Objects.equals(identifier, "Yes")){
                        action.propertyPurchaseDecision.buyProperty(action.player);
                        buttonNo.disable();
                        buttonYes.disable();
                        propertyRefresh();
                    }
                    if (Objects.equals(identifier, "No")){
                        buttonNo.disable();
                        buttonYes.disable();
                        board.incrementPlayerTurnIfOver();
                    }
                    if (Objects.equals(identifier, "MessageBox")){
                        Object messageboxparent = c.getParent();
                        if (messageboxparent instanceof MessageBox){
                            ((MessageBox) messageboxparent).destroy();
                        }
                        break;
                    }
                    if (Objects.equals(identifier, "Property")){
                        if (c instanceof RadioButton){
                            RadioButton button = (RadioButton) c;
                            if (button.getEnabled()){
                                currentlySelectedProperty = button.enableProperty();
                            }
                            else {
                                currentlySelectedProperty = null;
                                button.deselectCurrentProperty();
                            }
                            propertySelected();
                        }
                    }
                    if (Objects.equals(identifier, "Mortgage")){
                        if (c instanceof MultiStateTextButton){
                            if (((MultiStateTextButton) c).getEnabled() && this.currentlySelectedProperty != null){
                                currentPlayer.modifyMoney(currentlySelectedProperty.mortgage());
                                ((MultiStateTextButton) c).disable();
                            }
                        }
                        propertySelected();
                    }
                    if (Objects.equals(identifier, "Unmortgage")){
                        if (c instanceof MultiStateTextButton){
                            if (((MultiStateTextButton) c).getEnabled() && this.currentlySelectedProperty != null){
                                if (currentlySelectedProperty.canAffordUnmortgage(currentPlayer)){
                                    currentPlayer.debitMoney(currentlySelectedProperty.unmortgage(currentPlayer));
                                }
                                ((MultiStateTextButton) c).disable();
                            }
                        }
                        propertySelected();
                    }
                    if (Objects.equals(identifier,"BuyHouse")){
                        if (currentlySelectedProperty instanceof Property && ((MultiStateTextButton) c).getEnabled()){
                            ((Property) currentlySelectedProperty).upgradeWithChecks(currentPlayer);
                        }
                        propertySelected();
                    }
                    if (Objects.equals(identifier,"SellHouse")){
                        if (currentlySelectedProperty instanceof Property && ((MultiStateTextButton) c).getEnabled()){
                            ((Property) currentlySelectedProperty).downgradeWithChecks(currentPlayer);
                        }
                        propertySelected();
                    }
                    if (Objects.equals(identifier, "DownArrow")){
                        if (c instanceof MultiStateButton){
                            Object parentObj = ((MultiStateButton) c).getParent();
                            if (parentObj instanceof PropertySelectBox){
                                ((PropertySelectBox) parentObj).modifyIndex(1);
                            }
                        }
                    }
                    if (Objects.equals(identifier, "UpArrow")){
                        if (c instanceof MultiStateButton){
                            Object parentObj = ((MultiStateButton) c).getParent();
                            if (parentObj instanceof PropertySelectBox){
                                ((PropertySelectBox) parentObj).modifyIndex(-1);
                            }
                        }
                    }
                    if (Objects.equals(identifier, "Trade")){
                        game.setScreen(ts);
                    }
                }
            }
        }
    }

    public void clearUI(){
        currentlySelectedProperty = null;
        propertiesBox.clearAll();
        propertySelected();
    }

    public void propertySelected(){
        buttonMortgage.disable();
        buttonUnmortgage.disable();
        buttonBuyHouse.disable();
        buttonSellHouse.disable();
        if (currentlySelectedProperty == null) return;
        if (currentlySelectedProperty.isMortgaged()){
            if (currentlySelectedProperty.canAffordUnmortgage(currentPlayer)){
                buttonUnmortgage.enable();
            }
        }
        else {
            buttonMortgage.enable();
        }

        if (currentlySelectedProperty instanceof Property){
            Property castProperty = (Property) currentlySelectedProperty;

            if (castProperty.getSetOwner() != null && castProperty.getSetOwner().equals(currentPlayer)){
                Set parentSet = castProperty.getParentSet();
                if (parentSet.checkForUpgrade(castProperty)){
                    buttonBuyHouse.enable();
                }
                if(parentSet.checkForDowngrade(castProperty)){
                    buttonSellHouse.enable();
                }
            }
        }
    }

    public void rollDiceAndMove(){
        playerMoving = true;
        if (hasPlayerActions()) {
            playerMoving = false;
            return;
        }
        buttonNo.disable();
        buttonYes.disable();
        action = nextAction();
        if (action == null) return;
        if (action.stillInJail && action.playerTurnOver) playerMoving = false;
        if (action != null && action.isNewPlayerTurn){
            clearUI();
        }
    }

    public Action nextAction(){
        if (!hasPlayerActions()){
            Action currentAction;
            currentAction = board.gameLoop();
            if (currentAction.hasGameEnded){
                game.setScreen(new MainMenuScreen(game, board.playerOrder.get(0)));
                return null;
            }

            if (currentAction.diceRoll != null){
                mostRecentRoll = currentAction.diceRoll;
            }

            Player playerToAction = currentAction.player;
            try{
                VisualCharacter currentVisualCharacter = playerToAction.getVS();
                for (int space: currentAction.spacesMoved){
                    Vector2 coords = getCoords(space);
                    currentVisualCharacter.queueAction(coords);
                }
                if (currentAction.goingToJail){
                    currentVisualCharacter.queueAction(jailPosition);
                }
            }
            catch (Exception ignored){
                System.out.println("A");
                action.isNewPlayerTurn = true;
            };
            return currentAction;
        }
        return null;
    }

    private boolean hasPlayerActions(){
        for(VisualCharacter player : players){
            if(player.hasActions()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void show() {
        game.getInputProcessor().setCam(camera);
        game.getInputProcessor().screen = this;
        propertyRefresh();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        batch.dispose();
        monopoly.dispose();
    }
}
