package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class TurtleEscape2 extends Game {

    public static final int HEIGHT = 800;
    public static final int WIDTH = 480;

    @Override
    public void create() {
        Texture.setEnforcePotImages(false);
        setScreen( new ChoosePlayersScreen(this));
    }

}