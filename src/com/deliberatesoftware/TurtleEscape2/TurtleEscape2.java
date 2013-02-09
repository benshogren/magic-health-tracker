package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class TurtleEscape2 extends Game {
    Texture dropImage;
    Texture bucketImage;
    SpriteBatch spriteFont;
    OrthographicCamera camera;
    private BitmapFont font;
    private int currentHealth = 20;
    private MyGestureListener listener;

    @Override
    public void create() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        spriteFont = new SpriteBatch();

        // create the raindrops array and spawn the first raindrop
        font = new BitmapFont();
        currentHealth = 20;
        listener = new MyGestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(listener));
    }

    private int rotation = 1;
    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        spriteFont.setProjectionMatrix(camera.combined);

     //   Matrix4 mx4Font = new Matrix4();
     //   mx4Font.setToRotation(new Vector3(100, 100, 0), rotation);
     //   spriteFont.setTransformMatrix(mx4Font);
        spriteFont.begin();
        font.draw(spriteFont, "Health", 480/2, 800/2);
        font.draw(spriteFont, Integer.toString(currentHealth), 20, 160);
        font.draw(spriteFont, Integer.toString(rotation), 30, 190);
        spriteFont.end();

        if(currentHealth <= 0) {
            font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        } else {
            font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        // process user input
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.y <= 400) {
                if (currentHealth > 0) {
                    currentHealth--;
                }
            } else {
                currentHealth++;
            }
        }
        if (listener.hadFling) {
            camera.rotate(180);
            listener.hadFling = false;
        }
    }


    @Override
    public void dispose() {
        // dispose of all the native resources
        dropImage.dispose();
        bucketImage.dispose();
        //dropSound.dispose();
        //rainMusic.dispose();
        spriteFont.dispose();
    }
}