package com.jacques.visualelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.jacques.UI.InputBox;
import com.jacques.UI.PropertySelectBox;

import java.util.ArrayList;
import java.util.List;

public class MonopolyInputProcessor implements InputProcessor {

    OrthographicCamera camera;
    ArrayList<PropertySelectBox> scrollboxs = new ArrayList<>();
    public ElementScreen screen;
    int mostRecentChar;

    List<InputBox> inputBoxes = new ArrayList<>();

    public void addInputBox(InputBox ib){
        inputBoxes.add(ib);
    }

    @Override
    public boolean keyDown(int i) {
        System.out.println("Key pressed: " + i);
        mostRecentChar = i;
        for (InputBox ib : inputBoxes){
            ib.keyPressed(i);
        }
        return false;
    }

    public int getMostRecentChar() {
        return mostRecentChar;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        if (camera == null) return false;
        Vector3 coordsClicked = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        for (PropertySelectBox scrollbox : scrollboxs){
            if (scrollbox.screen.equals(screen) && scrollbox.isCursorInRange(coordsClicked.x,coordsClicked.y)){
                if (v1 < 0){
                    scrollbox.modifyIndex(-1);
                }
                else if (v1 > 0){
                    scrollbox.modifyIndex(1);
                }
                return true;
            }
        }
        return false;
    }

    public void setCam(OrthographicCamera cam) {
        camera = cam;
    }
    public void addScrollBox(PropertySelectBox p){
        scrollboxs.add(p);
    }
}
