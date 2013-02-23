package com.deliberatesoftware.TurtleEscape2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

public class ChoosePlayersScreen implements Screen {
    private SpriteBatch spriteBatch;
    private Map<Integer, Texture> numImages = new HashMap<Integer, Texture>();

    private TurtleEscape2 game;
    public ChoosePlayersScreen ( TurtleEscape2 game ) {
        this.game = game;
    }

    @Override
    public void show() {
        for (int i = 0; i <= 33; i ++){
            numImages.put(i, new Texture(Gdx.files.internal("numbers/"+i+".png")));
        }
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        //Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(numImages.get(11), 0, 0, game.WIDTH, game.HEIGHT);
        spriteBatch.end();
    }

    private void handleInput() {
        if(Gdx.input.justTouched()) {
            game.setScreen(new HealthScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
        for(int i = 0; i <= 33; i++){
            numImages.get(i).dispose();
        }
    }
}
