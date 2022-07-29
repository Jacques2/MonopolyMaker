package com.jacques.UI;

import com.badlogic.gdx.graphics.Color;

public class OnScreenText {
    public String text;
    public int x;
    public int y;
    public float scale = 1;
    public float targetWidth = 999999;
    public Color color = Color.BLACK;
    public boolean wrap = true;

    public OnScreenText(String text, int x, int y){
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void setValues(String text, int x, int y){
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public OnScreenText(String text, int x, int y, float scale){
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public OnScreenText(String text, int x, int y, Color color){
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void setColor(int r, int g, int b){
        this.color = new Color(r,g,b,1);
    }

    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
    }

    public void appendCoords(int x, int y){
        this.x += x;
        this.y += y;
    }

    public void appendText(String characters){
        text = text + characters;
    }

    public void subtractText(){
        if (text.length() <= 0) return;
        text = text.substring(0, text.length() - 1);
    }

    public int getTextLength(){
        return text.length();
    }

    public void setWrap(boolean wrap){
        this.wrap = wrap;
    }
}
