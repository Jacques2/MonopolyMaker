package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;
import com.jacques.visualelements.ElementScreen;

public class MultiStateTextButton extends MultiStateButton{

    OnScreenText text;
    ElementScreen screen;

    public MultiStateTextButton(Texture textureEnabled, Texture textureDisabled, float x, float y, float width, float height, String identifier) {
        super(textureEnabled, textureDisabled, x, y, width, height, identifier);
        text = new OnScreenText("textbox",(int)(x+20),(int)(y+height));
        text.setScale(0.5f);
        disable();
    }

    public void setElementScreen(ElementScreen screen) {
        this.screen = screen;
        screen.addText(text);
    }

    public void setText(String text){
        this.text.setText(text);
    }
    public void setTextScale(float scale){
        text.setScale(scale);
    }

    public OnScreenText getText() {
        return text;
    }
}
