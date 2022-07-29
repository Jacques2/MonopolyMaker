package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Clickable {

    public Sprite skin;
    protected String identifier;
    protected float x, y;
    private Object parent;

    public Clickable(Texture texture, float x, float y, float width, float height, String identifier) {
        skin = new Sprite(texture); // your image
        this.identifier = identifier;
        skin.setPosition(x, y);
        skin.setSize(width, height);
        this.x = x;
        this.y = y;
    }
    public boolean checkIfClicked(float ix, float iy) {
        if (ix > skin.getX() && ix < skin.getX() + skin.getWidth()) {
            return iy > skin.getY() && iy < skin.getY() + skin.getHeight();
        }
        return false;
    }
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
