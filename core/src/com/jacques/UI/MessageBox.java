package com.jacques.UI;

import com.badlogic.gdx.graphics.Texture;
import com.jacques.visualelements.ElementScreen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageBox {

    public ElementScreen screen;
    private int x;
    private int y;

    Clickable backgroundBox;
    static OnScreenText text = new OnScreenText("", 18, 240);;

    public MessageBox(ElementScreen gs, String message){
        new MessageBox(gs, 250,470,message);
    }

    public MessageBox(ElementScreen gs, int x, int y, String message) {
        screen = gs;
        this.x = x;
        this.y = y;

        backgroundBox = new Clickable(new Texture("core/assets/messageBox.png"), x, y, 550, 240, "MessageBox");
        backgroundBox.setParent(this);
        screen.addClickable(backgroundBox);

        text.setValues(message, x + 18, y + 240);
        text.setScale(0.6f);
        text.setColor(255,255,255);
        text.setTargetWidth(500);
        //text.setWrap(true);
        screen.addText(text);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.schedule(task,6L,TimeUnit.SECONDS);
    }

    public void destroy(){
        screen.removeClickable(backgroundBox);
        screen.removeText(text);
        backgroundBox = null;
        //text = null;
    }
    Runnable task = this::destroy;
}
