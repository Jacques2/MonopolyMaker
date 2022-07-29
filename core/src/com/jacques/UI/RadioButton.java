package com.jacques.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.jacques.monopoly.OwnershipTile;

public class RadioButton extends MultiStateButton{

    PropertySelectBox parent;
    OnScreenText text;
    OwnershipTile property;

    public RadioButton(Texture textureEnabled, Texture textureDisabled, float x, float y, float width, float height, String identifier) {
        super(textureEnabled, textureDisabled, x, y, width, height, identifier);
        text = new OnScreenText("Text box",(int)x+10, (int) (y+height*1.2));
        text.setScale(0.7f);
        text.setWrap(false);
        text.setTargetWidth(260);
    }

    public void setParent(PropertySelectBox parent){
        this.parent = parent;
        parent.screen.addText(text);
    }

    public void setProperty(OwnershipTile property){
        this.property = property;
        if (property != null){
            setText(property.getName());
            if (property.getParentSet() != null){
                text.setColor(property.getParentSet().getSetColor());
                if (property.getParentSet().hasSameOwner()){
                    setText(property.getName() + "*");
                }
            }
            else{
                text.setColor(Color.BLACK);
            }
        }
        else {
            setText("");
        }
    }

    public OwnershipTile enableProperty(){
        if(!enabled) return null;
        parent.setSelectedProperty(property);
        return property;
    }

    public OwnershipTile getProperty() {
        return property;
    }

    @Override
    public String click(){
        if (this.property == null){
            return null;
        }
        toggle();
        if (parent.singleSelect) parent.deselectAllOthers(this);
        System.out.println(property.getName());
        return identifier;
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public OnScreenText getText(){
        return text;
    }

    public void deselectCurrentProperty(){
        parent.removeProperty(property);
    }
}
