package com.jacques.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.jacques.monopoly.OwnershipTile;
import com.jacques.visualelements.ElementScreen;

import java.util.*;

public class PropertySelectBox {

    public ElementScreen screen;
    private int x;
    private int y;
    private static Map<Integer,Integer> indexList = new HashMap<>();
    private int personalIndex = -1;

    protected boolean singleSelect = true;

    TreeSet<OwnershipTile> selectedProperty = new TreeSet<>();

    List<OwnershipTile> ownedProperty;

    ArrayList<RadioButton> buttons = new ArrayList<>();

    public PropertySelectBox(ElementScreen gs, int x, int y, int personalIndex){
        screen = gs;
        this.x = x;
        this.y = y;
        createBoxes();
        this.personalIndex = personalIndex;
        if (!indexList.containsKey(personalIndex)) indexList.put(personalIndex,0);
    }

    private int getIndex(){
        return indexList.get(personalIndex);
    }

    private void setIndex(int index){
        indexList.put(personalIndex,index);
    }

    public void resetIndex(){
        indexList.put(personalIndex,0);
    }

    public void createBoxes(){
        for (int i = 0; i < 5; i++) {
            RadioButton prop1 = new RadioButton(new Texture(Gdx.files.internal("core/assets/propertySelectBoxActive.png")),new Texture(Gdx.files.internal("core/assets/propertySelectBoxInactive.png")),x,y-(i*40),270,40,"Property");
            prop1.setParent(this);
            buttons.add(prop1);
            screen.addClickable(prop1);
        }

        MultiStateButton buttonUp = new MultiStateButton(new Texture("core/assets/buttonUpArrow.png"),new Texture("core/assets/buttonUpArrow.png"),x+273,y-15,55,55,"UpArrow");
        buttonUp.setParent(this);
        screen.addClickable(buttonUp);

        MultiStateButton buttonDown = new MultiStateButton(new Texture("core/assets/buttonDownArrow.png"),new Texture("core/assets/buttonDownArrow.png"),x+273,y-95,55,55,"DownArrow");
        buttonDown.setParent(this);
        screen.addClickable(buttonDown);

        deselectAll();
    }

    public void clearAll(){
        deselectAll();
        for (RadioButton r : buttons){
            r.setProperty(null);
        }
    }

    public void clearSelectedPropertyList(){
        selectedProperty = new TreeSet<>();
        enableButtonsWithProperty();
    }

    public void deselectAll(){
        deselectAllOthers(null);
    }

    public void deselectAllOthers(RadioButton exception){
        for (RadioButton r : buttons){
            if (!r.equals(exception)){
                r.disable();
            }
        }
    }

    public void enableButtonsWithProperty(){
        for (RadioButton r : buttons){
            if (r.getProperty() == null){
                r.disable();
                continue;
            }
            if (selectedProperty != null && selectedProperty.contains(r.getProperty())){
                r.enable();
            }
            else {
                r.disable();
            }
        }
    }

    public void setProperties(List<OwnershipTile> newOwnedProperty){
        ownedProperty = newOwnedProperty;
        refreshProperties();

    }

    private void refreshProperties(){
        for (int i = 0; i < 5; i++) {
            buttons.get(i).setText("");
            if (ownedProperty.size()-1 >= getIndex()+i && ownedProperty.size() >= 1){
                buttons.get(i).setProperty(ownedProperty.get(getIndex()+i));
            }
            else{
                buttons.get(i).setProperty(null);
            }
        }
    }

    public void modifyIndex(int modifier){
        if (ownedProperty == null){
            setIndex(0);
            return;
        }
        if (getIndex() + modifier <= -1){
            setIndex(0);
        }
        else if (getIndex() + modifier >= ownedProperty.size()){
            setIndex(ownedProperty.size()-1);
        }
        else {
            setIndex(getIndex() + modifier);
        }
        refreshProperties();
        enableButtonsWithProperty();
    }

    public void setSelectedProperty(OwnershipTile selectedProperty) {
        if (singleSelect){
            this.selectedProperty = new TreeSet<>(Arrays.asList(selectedProperty));
        }
        else {
            this.selectedProperty.add(selectedProperty);
        }
    }

    public void removeProperty(OwnershipTile property){
        if (singleSelect){
            selectedProperty = new TreeSet<>();
        }
        else if (selectedProperty.contains(property)) {
            selectedProperty.remove(property);
        }
    }

    public boolean isCursorInRange(float x, float y){
        for (RadioButton b : buttons){
            if (b.checkIfClicked(x,y)) return true;
        }
        return false;
    }

    public void setSingleSelect(boolean singleSelect) {
        this.singleSelect = singleSelect;
    }

    public TreeSet<OwnershipTile> getSelectedProperty() {
        return selectedProperty;
    }
}
