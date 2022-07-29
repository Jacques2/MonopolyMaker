package com.jacques.visualelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jacques.UI.*;
import com.jacques.monopoly.Board;
import com.jacques.monopoly.OwnershipTile;
import com.jacques.monopoly.Player;

import java.util.*;

public class TradeScreen extends ElementScreen{

    final MonopolyMaker game;
    GameScreen gs;
    Board board;

    Clickable buttonExitTrade;
    Clickable buttonConfirmTrade;

    OnScreenText tradingText = new OnScreenText("Trading", 70, 980,1.4f);

    InputBox selfMoneyInput;
    OnScreenText selfSelectedPropertiesText = new OnScreenText("Selected Properties:\n",200,440,0.5f);
    Set<OwnershipTile> selfPropertiesToTrade = new TreeSet<>();
    PropertySelectBox selfPropertiesBox;


    InputBox otherMoneyInput;
    OnScreenText otherSelectedPropertiesText = new OnScreenText("Selected Properties:\n",900,440,0.5f);
    Set<OwnershipTile> otherPropertiesToTrade = new TreeSet<>();
    PropertySelectBox otherPropertiesBox;

    MultiSelection<Player> otherSelector;

    OrthographicCamera camera;

    public TradeScreen(final GameScreen gs, final MonopolyMaker gam, Board board) {
        this.gs = gs;
        game = gam;
        this.board = board;

        buttonExitTrade = new Clickable(new Texture("core/assets/buttonExitTrade.png"),300,900,250,80,"ExitTrade");
        clickables.add(buttonExitTrade);

        buttonConfirmTrade = new Clickable(new Texture("core/assets/buttonAcceptTrade.png"),300,200,250,80,"ConfirmTrade");
        clickables.add(buttonConfirmTrade);

        //--------------------------------------------

        selfPropertiesBox = new PropertySelectBox(this,200,600,1);
        selfPropertiesBox.setSingleSelect(false);

        selfMoneyInput = new InputBox(new Texture("core/assets/inputBoxActive.png"),new Texture("core/assets/inputBox.png"),200,750,200,60,"MoneyInput");
        selfMoneyInput.setElementScreen(this);
        selfMoneyInput.setInputType(InputBox.NUMBERSONLY);
        clickables.add(selfMoneyInput);
        game.getInputProcessor().addInputBox(selfMoneyInput);

        selfSelectedPropertiesText.color = Color.RED;
        texts.add(selfSelectedPropertiesText);

        //-------------------------------------------

        otherSelectedPropertiesText.color = Color.RED;
        texts.add(otherSelectedPropertiesText);

        otherPropertiesBox = new PropertySelectBox(this,900,600,2);
        otherPropertiesBox.setSingleSelect(false);

        otherMoneyInput = new InputBox(new Texture("core/assets/inputBoxActive.png"),new Texture("core/assets/inputBox.png"),900,750,200,60,"MoneyInput");
        otherMoneyInput.setElementScreen(this);
        otherMoneyInput.setInputType(InputBox.NUMBERSONLY);
        clickables.add(otherMoneyInput);
        game.getInputProcessor().addInputBox(otherMoneyInput);

        //--------------------------------------------

        otherSelector = new MultiSelection(this,900,900);
        game.getInputProcessor().addScrollBox(selfPropertiesBox);
        game.getInputProcessor().addScrollBox(otherPropertiesBox);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MonopolyMaker.FRAME_WIDTH, MonopolyMaker.FRAME_HEIGHT);

        tradingText.setColor(Color.WHITE);
        texts.add(tradingText);
    }

    @Override
    public void show() {
        game.getInputProcessor().setCam(camera);
        game.getInputProcessor().screen = this;
        propertyRefresh();

        selfPropertiesBox.clearSelectedPropertyList();
        selfSelectedPropertiesText.setText("Selected Properties:\n");
        selfMoneyInput.clear();

        otherPropertiesBox.clearSelectedPropertyList();
        otherSelectedPropertiesText.setText("Selected Properties:\n");
        otherMoneyInput.clear();

        selfMoneyInput.maxFunds = gs.currentPlayer.getMoney();

        otherSelector.clear();


        for (VisualCharacter p : gs.players){
            if (!p.player.equals(gs.currentPlayer)) otherSelector.addElem(p.player);
        }
        playerUpdate();
    }

    public void clickCheck() throws ConcurrentModificationException {
        if (Gdx.input.justTouched()) {
            Vector3 coordsClicked = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int ix = (int) coordsClicked.x;
            int iy = (int) coordsClicked.y;
            System.out.println("Clicked - X:" + ix + " Y:" + iy);
            for (Clickable c : clickables) {
                if (c.checkIfClicked(ix, iy)) {
                    String identifier = c.click();
                    System.out.println("Button clicked: " + identifier);
                    if (Objects.equals(identifier, "ExitTrade")) {
                        game.setScreen(gs);
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
                    if (Objects.equals(identifier, "Property")){
                        if (c instanceof RadioButton){
                            if (((RadioButton) c).getEnabled()){
                                ((RadioButton) c).enableProperty();
                            }
                            else {
                                ((RadioButton) c).deselectCurrentProperty();
                            }
                            propertyUpdate();
                        }
                    }
                    if (Objects.equals(identifier, "MultiLeft")){
                        if (c instanceof MultiStateButton msb){
                            if (msb.getParent() instanceof MultiSelection ms){
                                ms.prev();
                                playerUpdate();
                            }
                        }
                    }
                    if (Objects.equals(identifier, "MultiRight")){
                        if (c instanceof MultiStateButton msb){
                            if (msb.getParent() instanceof MultiSelection ms){
                                ms.next();
                                playerUpdate();
                            }
                        }
                    }
                    if (Objects.equals(identifier, "ConfirmTrade")){
                        if (selfMoneyInput.getValue().equals("")) selfMoneyInput.setText("0");
                        if (otherMoneyInput.getValue().equals("")) otherMoneyInput.setText("0");
                        if(compAuthoriseTrade()) commitTrade();
                    }
                }
            }
        }
    }

    private void playerUpdate(){
        Player p = otherSelector.getElem();
        if (p == null) return;
        otherPropertiesBox.resetIndex();
        otherMoneyInput.maxFunds = p.getMoney();
        List<OwnershipTile> playerOwnedProperty = p.getUndevelopedProperty();
        Collections.sort(playerOwnedProperty);
        otherPropertiesBox.setProperties(playerOwnedProperty);
    }

    private void propertyUpdate(){
        selfSelectedPropertiesText.setText("Selected Properties:\n");
        selfPropertiesToTrade = selfPropertiesBox.getSelectedProperty();
        for (OwnershipTile ot : selfPropertiesToTrade){
            selfSelectedPropertiesText.appendText(ot.getName() + "\n");
        }

        otherSelectedPropertiesText.setText("Selected Properties:\n");
        otherPropertiesToTrade = otherPropertiesBox.getSelectedProperty();
        for (OwnershipTile ot : otherPropertiesToTrade){
            otherSelectedPropertiesText.appendText(ot.getName() + "\n");
        }
    }

    public boolean compAuthoriseTrade(){
        Player otherPlayer = otherSelector.getElem();
        if (!otherPlayer.isComputerPlayer()) return true;
        int humanValue = Integer.parseInt(selfMoneyInput.getValue());
        int compValue = Integer.parseInt(otherMoneyInput.getValue());
        for (OwnershipTile ot : selfPropertiesToTrade){
            humanValue += ot.getValue();
        }
        for (OwnershipTile ot : otherPropertiesToTrade){
            compValue += ot.getValue();
        }
        if (humanValue > compValue){
            return true;
        }
        else{
            new MessageBox(this,"This trade is NOT acceptable!");
            return false;
        }
    }

    public void commitTrade(){
        Player currentPlayer = gs.currentPlayer;
        Player otherPlayer = otherSelector.getElem();
        int selfFunds = Integer.parseInt(selfMoneyInput.getValue());
        int otherFunds = Integer.parseInt(otherMoneyInput.getValue());
        for (OwnershipTile ot : selfPropertiesToTrade){
            ot.transferOwnership(otherPlayer);
        }
        for (OwnershipTile ot : otherPropertiesToTrade){
            ot.transferOwnership(currentPlayer);
        }
        otherPlayer.modifyMoney(currentPlayer.debitMoney(selfFunds));
        currentPlayer.modifyMoney(otherPlayer.debitMoney(otherFunds));
        System.out.println("Trade completed");
        game.setScreen(gs);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        try{
            clickCheck();
        }
        catch (ConcurrentModificationException cme){
            System.out.println("Clickable check failed");
        }
        draw();
    }

    public void propertyRefresh(){
        List<OwnershipTile> ownedProperty = board.getPlayerTurn().getUndevelopedProperty();
        Collections.sort(ownedProperty);
        selfPropertiesBox.setProperties(ownedProperty);
    }

    public void draw(){
        game.batch.begin();
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        for (Clickable c : clickables){
            game.batch.draw(c.skin, c.skin.getX(), c.skin.getY(), c.skin.getWidth(), c.skin.getHeight());
        }
        for (OnScreenText text : texts){
            game.font.setColor(text.color);
            game.font.getData().setScale(text.scale,text.scale);
            try{
                game.font.draw(game.batch, text.text, text.x, text.y,0,text.getTextLength(),text.targetWidth, Align.left,text.wrap,"-");
            }
            catch (Exception ignored){
            }
        }

        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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
    public void dispose() {

    }
}
