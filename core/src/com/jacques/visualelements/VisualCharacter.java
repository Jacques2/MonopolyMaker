package com.jacques.visualelements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jacques.monopoly.Player;

import java.util.LinkedList;
import java.util.Queue;

public class VisualCharacter extends Actor {
    private final Sprite sprite;
    public Player player;
    private final Queue<Vector2> actionQueue = new LinkedList(){};

    public VisualCharacter(Texture tex, Player player){
        this.sprite = new Sprite(tex);
        this.player = player;
        this.player.setVS(this);
    }

    public Sprite GetTexture(){
        return sprite;
    }

    public void queueAction(Vector2 coords){
        actionQueue.add(coords);
    }

    public Vector2 nextAction(){
        return actionQueue.remove();
    }

    public boolean hasQueuedAction(){
        return actionQueue.size() > 0;
    }

}
