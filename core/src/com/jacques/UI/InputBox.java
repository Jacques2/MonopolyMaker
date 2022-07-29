package com.jacques.UI;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.jacques.monopoly.UserData;
import com.jacques.visualelements.ElementScreen;

public class InputBox extends MultiStateTextButton{

    public static int NUMBERSONLY = 1;
    public static int LETTERSONLY = 2;
    public static int ALPHANUMERIC = 3;

    private int inputType = 3;

    private int maxLength = 5;
    public int maxFunds = -1;

    UserData connectedPlayer;

    public InputBox(Texture textureEnabled, Texture textureDisabled, float x, float y, float width, float height, String identifier) {
        super(textureEnabled, textureDisabled, x, y, width, height, identifier);
    }

    @Override
    public String click(){
        toggle();
        return identifier;
    }

    @Override
    public void setElementScreen(ElementScreen screen){
        super.setElementScreen(screen);
        setTextScale(1);
        getText().appendCoords(-5,11);
    }

    public void setInputType(int type){
        if (type == 1 ||type == 2||type == 3){
            inputType = type;
        }
        else return;
        if (type == 1){
            setText("0");
        }
        else {
            setText("");
        }
    }

    public void keyPressed (int key){
        if (!enabled) return;
        if (key == 67) getText().subtractText();
        if (getText().getTextLength() >= maxLength) return;
        boolean isNumber = false;
        boolean isLetter = false;
        if (key >= 7 && key <= 16) isNumber = true; //number keys
        if (key >= 29 && key <= 54) isLetter = true; //alphabet keys
        if (key >= 144 && key <= 153) {
            isNumber = true; //numberpad keys
            key -= 137; // convert to number key
        }
        if (isNumber && (inputType == NUMBERSONLY || inputType == ALPHANUMERIC)){
            getText().appendText(Input.Keys.toString(key));
        }
        else if (key == Input.Keys.SPACE && (inputType == LETTERSONLY || inputType == ALPHANUMERIC)) {
            getText().appendText(" ");
        }
        else if (isLetter && (inputType == LETTERSONLY || inputType == ALPHANUMERIC)){
            getText().appendText(Input.Keys.toString(key));
        }
        if (inputType == NUMBERSONLY && getText().getTextLength() >= 1){
            int textValue = Integer.parseInt(getText().text);
            if (maxFunds != -1 && textValue > maxFunds) textValue = maxFunds;
            setText(String.valueOf(textValue));
        }
        if (connectedPlayer != null){
            connectedPlayer.username = getText().text;
            System.out.println("new name: " + connectedPlayer.username);
        }
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getValue(){
        return getText().text;
    }

    public void setConnectedPlayerName(UserData s){
        connectedPlayer = s;
        System.out.println("input box set to new player");
    }

    public void clear(){
        if (inputType == ALPHANUMERIC){
            setText("");
        }
        else setText("0");
    }
}
