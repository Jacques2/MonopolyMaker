package com.jacques.visualelements;

import com.badlogic.gdx.Screen;
import com.jacques.UI.Clickable;
import com.jacques.UI.OnScreenText;

import java.util.ArrayList;
import java.util.List;

public class ElementScreen implements Screen {

    List<Clickable> clickables = new ArrayList<>();
    List<OnScreenText> texts = new ArrayList<>();
    List<HouseVisual> houseVisuals;

    public void addClickable(Clickable c){
        clickables.add(c);
    }

    public void removeClickable(Clickable c){
        clickables.remove(c);
    }

    public void addText(OnScreenText o){
        texts.add(o);
    }

    public void removeText(OnScreenText o){
        texts.remove(o);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

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
