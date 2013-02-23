package com.deliberatesoftware.TurtleEscape2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

public class ChoosePlayersScreen implements Screen {
    private SpriteBatch spriteBatch;
    private Map<Integer, Texture> numImages = new HashMap<Integer, Texture>();

    private TurtleEscape2 game;
    private int numPlayers = 2;
    private Texture plus;
    private Texture minus;
    private Texture play;
    int imageWidth = TurtleEscape2.WIDTH/4;
    int xPlusStart = 0;
    int yPlusStart = ((TurtleEscape2.HEIGHT/4)*3) - imageWidth/2;
    Rectangle playButton = new Rectangle(TurtleEscape2.WIDTH/4, TurtleEscape2.HEIGHT/8,
            TurtleEscape2.WIDTH/2, TurtleEscape2.WIDTH/2);
    Rectangle numberButton = new Rectangle(0, (TurtleEscape2.HEIGHT / 4)*3 - (TurtleEscape2.WIDTH / 2),
            TurtleEscape2.WIDTH, TurtleEscape2.WIDTH);
    Rectangle minusButton = new Rectangle(0, yPlusStart,
            imageWidth, imageWidth);
    Rectangle plusButton = new Rectangle((TurtleEscape2.WIDTH/4)*3, yPlusStart,
            imageWidth, imageWidth);
    private OrthographicCamera camera;

    public ChoosePlayersScreen ( TurtleEscape2 game ) {
        this.game = game;
    }

    @Override
    public void show() {
        for (int i = 0; i <= 33; i ++){
            numImages.put(i, new Texture(Gdx.files.internal("numbers/"+i+".png")));
        }
        plus = new Texture(Gdx.files.internal("numbers/+.png"));
        minus = new Texture(Gdx.files.internal("numbers/-.png"));
        play = new Texture(Gdx.files.internal("play.png"));
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, TurtleEscape2.WIDTH, TurtleEscape2.HEIGHT);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(numImages.get(numPlayers), numberButton.x, numberButton.y, numberButton.width, numberButton.height);
        spriteBatch.draw(plus, plusButton.x, plusButton.y, plusButton.width, plusButton.height);
        spriteBatch.draw(minus, minusButton.x, minusButton.y, minusButton.width, minusButton.height);
        spriteBatch.draw(play, playButton.x, playButton.y, playButton.width, playButton.height);
        spriteBatch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (playButton.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new HealthScreen(game, numPlayers));
            } else if (Gdx.input.getX() < (TurtleEscape2.WIDTH/2)){
                if (numPlayers > 1) {
                    numPlayers--;
                }
            } else {
                if (numPlayers < 8) {
                    numPlayers++;
                }
            }
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
