package com.jacques.visualelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacques.UI.*;
import com.jacques.monopoly.Action;
import com.jacques.monopoly.Board;
import com.jacques.monopoly.Player;
import com.jacques.monopoly.UserData;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainMenuScreen extends ElementScreen {

    final MonopolyMaker game;
    OrthographicCamera camera;

    private List<BoardFile> boards = new ArrayList<>();


    MultiSelection<BoardFile> boardSelection;
    MultiSelection<Integer> playerCountSelection;
    MultiSelection<Integer> currentPlayerSelection;
    MultiSelection<String> playingPieceSelection;

    MultiStateButton buttonIsComputer;

    List<UserData> userData = new ArrayList<>();

    InputBox nameInputBox;

    private class BoardFile {
        public String filename;
        public String boardname;

        public boolean isValid = false;

        private class BoardName{
            public String boardName;
        }

        public BoardFile(String filename, String boardname){
            this.filename = filename;
            this.boardname = boardname;
        }

        public BoardFile(String filename){
            this.filename = filename;
            try {
                ZipFile boardZip = new ZipFile(filename);
                FileHeader fileHeader = boardZip.getFileHeader("data.json");
                InputStream inputStream = boardZip.getInputStream(fileHeader);
                Reader r = new InputStreamReader(inputStream);
                Type t = new TypeToken<BoardName>(){}.getType();
                BoardName newboardName = new Gson().fromJson(r,t);
                boardname = newboardName.boardName;
                isValid = true;
            } catch (Exception e) {
                isValid = false;
                return;
            }
        }

        @Override
        public String toString(){
            if(boardname == null) return filename;
            return boardname;
        }
    }

    public MainMenuScreen(final MonopolyMaker gam, Player winner) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MonopolyMaker.FRAME_WIDTH, MonopolyMaker.FRAME_HEIGHT);

        //browse button
        Clickable browseButton = new Clickable(new Texture("core/assets/buttonBrowse.png"),130,400,250,80,"Browse");
        clickables.add(browseButton);

        //play game button
        Clickable playButton = new Clickable(new Texture("core/assets/buttonPlay.png"),500,200,250,80,"Play");
        clickables.add(playButton);

        //selection for the board
        boardSelection = new MultiSelection<>(this, 80,510);

        //selection for the player piece
        playingPieceSelection = new MultiSelection<>(this,1000,540);
        playingPieceSelection.setElems(Arrays.asList("Battleship","Boot","Iron","RaceCar","ScottieDog","Thimble","TopHat","Wheelbarrow"));

        //selection for total player count
        playerCountSelection = new MultiSelection<>(this,1000,900);
        playerCountSelection.setLooping(false);
        for (int i = 2; i < 9; i++) {
            playerCountSelection.addElem(i);
        }

        for (int i = 1; i < 9; i++) {
            userData.add(new UserData("PLAYER " + i));
        }

        //computer player option
        buttonIsComputer = new MultiStateButton(new Texture("core/assets/buttonYes.png"),new Texture("core/assets/buttonNo.png"),1000,630,80,80,"CompPlayer");
        buttonIsComputer.disable();
        clickables.add(buttonIsComputer);

        //nameinput
        nameInputBox = new InputBox(new Texture("core/assets/inputBoxActive.png"),new Texture("core/assets/inputBox.png"),980,730,450,40,"NameInput");
        nameInputBox.setElementScreen(this);
        nameInputBox.setInputType(InputBox.ALPHANUMERIC);
        nameInputBox.setMaxLength(20);
        nameInputBox.setTextScale(0.8f);
        clickables.add(nameInputBox);
        game.getInputProcessor().addInputBox(nameInputBox);

        //selection for current player count
        currentPlayerSelection = new MultiSelection<>(this,1000,800);
        currentPlayerSelection.setLooping(false);
        checkForPlayerModification();

        //connect input box to multi selection
        nameInputBox.setConnectedPlayerName(userData.get(currentPlayerSelection.getElem()-1));

        //texts
        texts.add(new OnScreenText("Welcome to Monopoly!", 100, 800, Color.WHITE));
        texts.add(new OnScreenText("Total Players:", 765, 967, Color.WHITE));
        texts.add(new OnScreenText("Currently selected player: ", 565, 867, Color.WHITE));
        texts.add(new OnScreenText("Name: ", 870, 793, Color.WHITE));
        texts.add(new OnScreenText("Computer Player? ", 697, 716, Color.WHITE));
        texts.add(new OnScreenText("Playing Piece", 757, 610, Color.WHITE));

        if (winner != null){
            new MessageBox(this, winner.getName() + " won the game!!");
        }

        refreshBoards();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

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

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            System.out.println("debug mode on");
            game.DEBUG_MODE = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            System.out.println("debug mode off");
            game.DEBUG_MODE = false;
        }

        clickCheck();
    }

    Action action;
    public void clickCheck() {
        if (Gdx.input.justTouched()) {
            Vector3 coordsClicked = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int ix = (int) coordsClicked.x;
            int iy = (int) coordsClicked.y;
            System.out.println("Clicked - X:" + ix + " Y:" + iy);
            for (Clickable c : clickables) {
                if (c.checkIfClicked(ix, iy)) {
                    String identifier = c.click();
                    System.out.println("Button clicked: " + identifier);
                    if (Objects.equals(identifier, "Browse")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JFileChooser chooser = new JFileChooser();
                                JFrame f = new JFrame();
                                f.setVisible(true);
                                f.toFront();
                                f.setVisible(false);
                                f.setAlwaysOnTop(true);
                                int res = chooser.showOpenDialog(f);
                                f.dispose();
                                if (res == JFileChooser.APPROVE_OPTION) {
                                    BoardFile newFile = new BoardFile(chooser.getSelectedFile().toString());
                                    System.out.println("File path selected: " + chooser.getSelectedFile().toString());
                                    if (newFile.isValid){
                                        try{
                                            Files.copy(new File(chooser.getSelectedFile().toString()).toPath(),new File(Board.BOARD_DIR + "\\" + chooser.getSelectedFile().getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                            System.out.println("File added to list!");
                                        }
                                        catch (Exception e){
                                            System.out.println("Could not copy file.");
                                        }
                                    }
                                    refreshBoards();
                                }
                            }
                        }).start();
                    }
                    if (Objects.equals(identifier, "Play")) {
                        if (Board.workingBoard == null || Board.workingBoard.equals("")) return;
                        game.playerData = userData.stream().limit(playerCountSelection.getElem()).collect(Collectors.toList());
                        game.setScreen(new GameScreen(game));
                        dispose();
                    }
                    if (Objects.equals(identifier, "CompPlayer")) {
                        if (c instanceof MultiStateButton) ((MultiStateButton) c).toggle();
                        UserData currentPlayerData = userData.get(currentPlayerSelection.getElem()-1);
                        currentPlayerData.computerPlayer = buttonIsComputer.getEnabled();
                    }
                    if (Objects.equals(identifier, "MultiLeft")){
                        if (c instanceof MultiStateButton msb){
                            if (msb.getParent() instanceof MultiSelection ms){
                                ms.prev();
                                if (ms.equals(playingPieceSelection)){
                                    UserData currentPlayerData = userData.get(currentPlayerSelection.getElem()-1);
                                    currentPlayerData.sprite = (String) ms.getElem();
                                }
                                if (ms.equals(boardSelection) && ms.getElem() instanceof BoardFile){
                                    Board.workingBoard = ((BoardFile) ms.getElem()).filename;
                                }
                                checkForPlayerModification();
                            }
                        }
                    }
                    if (Objects.equals(identifier, "MultiRight")){
                        if (c instanceof MultiStateButton msb){
                            if (msb.getParent() instanceof MultiSelection ms){
                                ms.next();
                                if (ms.equals(playingPieceSelection)){
                                    UserData currentPlayerData = userData.get(currentPlayerSelection.getElem()-1);
                                    currentPlayerData.sprite = (String) ms.getElem();
                                }
                                if (ms.equals(boardSelection) && ms.getElem() instanceof BoardFile){
                                    Board.workingBoard = ((BoardFile) ms.getElem()).filename;
                                }
                                checkForPlayerModification();
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkForPlayerModification(){
        int playercount = playerCountSelection.getElem();
        if (currentPlayerSelection.getCount() != playercount || currentPlayerSelection.getElem() > playercount){
            List<Integer> playerNumberList = new ArrayList<>();
            for (int i = 0; i < playercount; i++) {
                playerNumberList.add(i+1);
            }
            currentPlayerSelection.setElems(playerNumberList);
        }
        UserData currentPlayerData = userData.get(currentPlayerSelection.getElem()-1);
        System.out.println("name: " + currentPlayerData.username);
        nameInputBox.setText(currentPlayerData.username);
        buttonIsComputer.setState(currentPlayerData.computerPlayer);
        playingPieceSelection.goToElem(currentPlayerData.sprite);
        if (currentPlayerSelection.getCount() != 0) {
            nameInputBox.setConnectedPlayerName(currentPlayerData);
        }

    }

    private void refreshBoards(){
        File directory = new File(Board.BOARD_DIR);
        if (!directory.exists()){
            directory.mkdir();
        }

        for (String file : directory.list()){
            try{
                BoardFile newfile = new BoardFile(Board.BOARD_DIR + "\\" + file);
                if (newfile.isValid){
                    boards.add(newfile);
                    System.out.println("added file: " + newfile);
                }
                else{
                    System.out.println(newfile + " is not a valid board file");
                }
            }
            catch (Exception e){
                System.out.println("file error");
                e.printStackTrace();
            }
        }
        boardSelection.setElems(boards);
        System.out.println(boardSelection.getElem());
        if (boardSelection.getElem() == null){
            Board.workingBoard = "";
        }
        else {
            Board.workingBoard = boardSelection.getElem().filename;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}