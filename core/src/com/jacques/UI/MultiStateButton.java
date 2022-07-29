package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MultiStateButton extends Clickable {

    private final Texture textureEnabled;
    private final Texture textureDisabled;

    private Object parent;

    protected boolean enabled = true;

    public MultiStateButton(Texture textureEnabled, Texture textureDisabled, float x, float y, float width, float height, String identifier) {
        super(textureEnabled, x, y, width, height, identifier);
        this.textureEnabled = textureEnabled;
        this.textureDisabled = textureDisabled;
    }

    public void disable(){
        enabled = false;
        extract(textureDisabled);
    }

    private void extract(Texture texture) {
        float x = skin.getX();
        float y = skin.getY();
        float width = skin.getWidth();
        float height = skin.getHeight();
        skin = new Sprite(texture);
        skin.setPosition(x,y);
        skin.setSize(width,height);
    }

    public void enable(){
        enabled = true;
        extract(textureEnabled);
    }

    public void toggle(){
        if (enabled){
            disable();
        }
        else {
            enable();
        }
    }

    public void setState(boolean isEnabled){
        if (isEnabled){
            enable();
        }
        else {
            disable();
        }
    }

    public boolean getEnabled(){
        return enabled;
    }

    @Override
    public String click(){
        return identifier;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }
}