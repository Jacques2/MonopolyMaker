package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;
import com.jacques.visualelements.ElementScreen;

import java.util.ArrayList;
import java.util.List;

public class MultiSelection<T> {

    public ElementScreen screen;
    private int x;
    private int y;
    private Options<T> options;
    private boolean allowLooping = true;

    private class Options<T>{
        private ArrayList<T> options;
        private int index = 0;
        public void addOption(T element){
            if (options == null) options = new ArrayList<>();
            options.add(element);
        }
        public void setOptions(List<T> elements){
            options = new ArrayList<>();
            for (T elem : elements){
                options.add(elem);
            }
        }
        public T getCurrentElement(){
            if (options == null) return null;
            if (options.size() <= 0) return null;
            return options.get(index);
        }
        public void nextIndex(){
            if (options == null) return;
            if (index + 1 >= options.size()){
                if (!allowLooping) return;
                index = 0;
            }
            else index++;
        }
        public void prevIndex(){
            if (options == null) return;
            if (index - 1 <= -1){
                if (!allowLooping) return;
                index = options.size()-1;
            }
            else index--;
        }
        public void resetIndex(){
            index = 0;
        }
    }

    MultiStateButton leftButton;
    MultiStateButton rightButton;
    Clickable centerBox;
    OnScreenText currentSelection;


    public MultiSelection(ElementScreen gs, int x, int y){
        screen = gs;
        this.x = x;
        this.y = y;

        leftButton = new MultiStateButton(new Texture("core/assets/buttonMultiSelectionLeft.png"),new Texture("core/assets/buttonMultiSelectionLeft.png"),x,y,60,60,"MultiLeft");
        leftButton.setParent(this);
        screen.addClickable(leftButton);

        rightButton = new MultiStateButton(new Texture("core/assets/buttonMultiSelectionRight.png"),new Texture("core/assets/buttonMultiSelectionRight.png"),x+360,y,60,60,"MultiRight");
        rightButton.setParent(this);
        screen.addClickable(rightButton);

        centerBox = new Clickable(new Texture("core/assets/buttonMultiSelectionCenter.png"),x+60,y,300,60,"MultiCenter");
        screen.addClickable(centerBox);

        currentSelection = new OnScreenText("",x+70,y+70);
        currentSelection.setWrap(false);
        currentSelection.setTargetWidth(290);
        screen.addText(currentSelection);
    }

    public void indexChanged(){
        T value = options.getCurrentElement();
        if (value == null) return;
        currentSelection.setText(value.toString());
    }

    public void next(){
        if(options == null) return;
        options.nextIndex();
        indexChanged();
    }
    public void prev(){
        if(options == null) return;
        options.prevIndex();
        indexChanged();
    }

    public void addElem(T elem){
        if (options == null) options = new Options<>();
        options.addOption(elem);
        indexChanged();
    }

    public void setElems(List<T> elems){
        if (options == null) options = new Options<>();
        options.setOptions(elems);
        options.resetIndex();
        indexChanged();
    }

    public void goToElem(T elem){
        int localIndex = -1;
        for (T item : options.options){
            localIndex++;
            if (item.equals(elem)){
                options.index = localIndex;
                indexChanged();
                return;
            }
        }
    }

    public T getElem(){
        if (options == null) return null;
        T value = options.getCurrentElement();
        if (value == null) return null;
        return value;
    }
    public void clear(){
        options = new Options<>();
        options.options = new ArrayList<>();
    }

    public void setLooping(boolean setting){
        allowLooping = setting;
    }

    public int getCount(){
        if (options == null) return 0;
        return options.options.size();
    }
}
