package com.jacques.visualelements;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class HouseVisual implements Serializable {
    int x,y;
    final int width = 60;
    final int height = 25;
    float rotation;
    static transient ArrayList<Texture> textures;
    private transient int state = -1;

    public HouseVisual(int x, int y, float rotation){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public HouseVisual(int x, int y, int width, int height, float rotation){
        this.x = x;
        this.y = y;
        //this.width = width;
        //this.height = height;
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean needToDraw(){
        return state >= 0;
    }

    public Texture getTexture(){
        if (state >= 0){
            return textures.get(state);
        }
        return textures.get(0);
    }

    public int getTextureWidth(){
        Texture t = getTexture();
        return t.getWidth();
    }

    public int getTextureHeight(){
        Texture t = getTexture();
        return t.getHeight();
    }


    public static void setTextures(Texture ... textureSet){
        textures = new ArrayList<>();
        textures.addAll(Arrays.asList(textureSet));
    }
}
