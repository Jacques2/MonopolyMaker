package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;

public class YesNoButton extends MultiStateButton{
    public YesNoButton(Texture textureEnabled, Texture textureDisabled, float x, float y, float width, float height, String identifier) {
        super(textureEnabled, textureDisabled, x, y, width, height, identifier);
    }

    @Override
    public String click(){
        if (enabled){
            toggle();
            return identifier;
        }
        return null;
    }
}
