package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;
import java.util.Map;

public class TurtleEscape2 extends Game {
    Texture dropImage;
    Texture bucketImage;
    SpriteBatch batch;
    OrthographicCamera camera;
    private BitmapFont font;
    private int Player1Health = 18;
    private int Player2Health = 20;
    private MyGestureListener listener;
    private Texture healthImage;
    private Map<Integer, Texture> numImages = new HashMap<Integer, Texture>();
    private Texture minus;
    private Texture plus;

    @Override
    public void create() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        healthImage = new Texture(Gdx.files.internal("health.png"));
        for (int i = 0; i <= 20; i ++){
            numImages.put(i, new Texture(Gdx.files.internal("numbers/"+i+".png")));
        }
        plus = new Texture(Gdx.files.internal("numbers/+.png"));
        minus = new Texture(Gdx.files.internal("numbers/+.png"));
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        batch = new SpriteBatch();

        // create the raindrops array and spawn the first raindrop
        //font = new BitmapFont();
        listener = new MyGestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(listener));

        font = new BitmapFont(Gdx.files.internal("test.fnt"),
                Gdx.files.internal("test.png"), false);
    }


    private int widthmid = 480/2;
    private int heightmid = 800/2;
    private boolean upsideDown = false;
    private int lastRotate = 0;

    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0.3f, 0.0f, 0.0f, 1);
        //Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //font.draw(batch, "Health", widthmid, heightmid);
        if(upsideDown) {
            drawTop(Player2Health);
            drawBottem(Player1Health);
        } else {
            drawTop(Player1Health);
            drawBottem(Player2Health);
        }
        Rectangle topButton = new Rectangle(0, (heightmid*2)-75, 75,75);
        batch.draw(plus, topButton.x, topButton.y, topButton.height, topButton.width);
        Rectangle bottomButton = new Rectangle(0, heightmid-75, 75,75);
        batch.draw(plus, bottomButton.x, bottomButton.y, bottomButton.height, bottomButton.width);
        batch.end();

        // process user input
        if (listener.hadFling) {
            //camera.unproject(listener.lastFling);
            camera.rotate(lastRotate * -1);
            if (listener.lastFling.y < 0) {
                camera.rotate(180);
                lastRotate = 180;
                upsideDown = true;
            } else {
                lastRotate = 0;
                upsideDown = false;
            }
            listener.hadFling = false;
        } else if (listener.hadTap) {
            Vector3 touchPos = listener.lastTap;
            listener.hadTap = false;
            //camera.unproject(touchPos);
            if (touchPos.y <= heightmid) {
                camera.unproject(touchPos);
                if(topButton.contains(touchPos.x, touchPos.y)) {
                    Player1Health++;
                } else{
                    if (Player1Health > 0) {
                        Player1Health--;
                    }
                }
            } else {
                if(bottomButton.contains(touchPos.x, touchPos.y)) {
                    Player2Health++;
                } else{
                    if (Player2Health > 0) {
                        Player2Health--;
                    }
                }
            }
        }
    }

    private void drawTop(int health) {
        float percent = getHealthPercent(health);
        batch.setColor(0, 1, 0, percent);
        batch.draw(healthImage, 0, heightmid, 480, heightmid);
        batch.setColor(0, 0, 0, 1);
        batch.draw(numImages.get(health), 0, heightmid, 480, heightmid);
    }

    private float getHealthPercent(int health) {
        //return 0.95f;
        return (health*5)/100f;
    }

    private void drawBottem(int health) {
        float percent = getHealthPercent(health);
        batch.setColor(0, 1, 0, percent);
        batch.draw(healthImage, 0, 0, 480, heightmid);
        batch.setColor(0, 0, 0, 1);
        batch.draw(numImages.get(health),0,0, 480, heightmid);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        dropImage.dispose();
        bucketImage.dispose();
        //dropSound.dispose();
        //rainMusic.dispose();
        batch.dispose();
        for(int i = 0; i <= 20; i++){
            numImages.get(i).dispose();
        }
        plus.dispose();
        minus.dispose();
    }
}