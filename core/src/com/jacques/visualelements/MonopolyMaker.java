package com.jacques.visualelements;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jacques.monopoly.UserData;

import java.util.List;

public class MonopolyMaker extends Game {

	public static boolean DEBUG_MODE = false;

	public SpriteBatch batch;
	public BitmapFont font;

	public List<UserData> playerData;

	private MonopolyInputProcessor is = new MonopolyInputProcessor();

	public static final int FRAME_WIDTH = 1600;
	public static final int FRAME_HEIGHT = 1000;

	public void create() {

		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(is);
		Gdx.input.setInputProcessor(im);

		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("core/assets/font/font4.fnt"),Gdx.files.internal("core/assets/font/font4.png"),false);
		this.setScreen(new MainMenuScreen(this,null));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public MonopolyInputProcessor getInputProcessor(){
		return is;
	}

}