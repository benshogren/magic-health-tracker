package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    Rectangle topButton;
    Rectangle bottomButton;

    @Override
    public void create() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        healthImage = new Texture(Gdx.files.internal("health.png"));
        for (int i = 0; i <= 24; i ++){
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
        topButton = new Rectangle(0, (heightmid*2)-75, 75,75);
        bottomButton = new Rectangle(0, heightmid-75, 75,75);
    }


    private int widthmid = 480/2;
    private int heightmid = 800/2;
    private Direction currentDirection = Direction.South;
    private int lastRotate = 0;
    private int pulse = 0;
    public enum Direction{
        East,
        West,
        North,
        South,
        SouthEast,
        SouthWest,
        NorthWest,
        NorthEast
    }
    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        //Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawTop(Player1Health);
        drawBottem(Player2Health);
        batch.end();

        // process user input
        if (listener.hadFling) {
            setDirection();
            listener.hadFling = false;
        } else if (listener.hadTap) {
            Vector3 touchPos = listener.lastTap;
            listener.hadTap = false;
            camera.unproject(touchPos);
            if (touchPos.y > heightmid) { //player 1
                if(topButton.contains(touchPos.x, touchPos.y) && Player1Health < 24) {
                    Player1Health++;
                } else{
                    if (Player1Health > 0) {
                        Player1Health--;
                    }
                }
            } else {
                if(bottomButton.contains(touchPos.x, touchPos.y) && Player2Health < 24) {
                    Player2Health++;
                } else{
                    if (Player2Health > 0) {
                        Player2Health--;
                    }
                }
            }
        }
    }

    private void setDirection() {
        float x = listener.currentFling.x;
        float y = listener.currentFling.y;
        float absX = Math.abs(x);
        float absY = Math.abs(y);

        if (absY > absX) {
            if (y < 0){
                if(absY/2 > absX) {
                    currentDirection = Direction.North;
                } else if(x > 0) {
                    currentDirection = Direction.NorthWest;
                } else {
                    currentDirection = Direction.NorthEast;
                }
            } else {
                if(absY/2 > absX) {
                    currentDirection = Direction.South;
                } else if(x > 0) {
                    currentDirection = Direction.SouthEast;
                } else {
                    currentDirection = Direction.SouthWest;
                }
            }
        } else {
            if (x > 0){
                if(absX/2 > absY) {
                    currentDirection = Direction.East;
                } else if(y < 0) {
                    currentDirection = Direction.NorthWest;
                } else {
                    currentDirection = Direction.SouthEast;
                }
            } else {
                if(absX/2 > absY) {
                    currentDirection = Direction.West;
                } else if(y < 0) {
                    currentDirection = Direction.NorthEast;
                } else {
                    currentDirection = Direction.SouthWest;
                }
            }
        }
    }

    private void drawTop(int health) {
        float percent = getHealthPercent(health);
        batch.setColor(0, 1, 0, percent);
        batch.draw(healthImage, 0, heightmid, 480, heightmid);
        batch.setColor(0, 0, 0, 1);
        Sprite num = new Sprite(numImages.get(health));
        num.setPosition(0, heightmid);
        num.setSize(480, heightmid);
        num.setOrigin(widthmid, heightmid / 2);
        num.setRotation(getRotation());
        num.draw(batch);
        batch.draw(plus, topButton.x, topButton.y, topButton.height, topButton.width);
    }
    private int getRotation(){
        switch(currentDirection){
            case North:
                return 180;
            case South:
                return 0;
            case West:
                return -90;
            case East:
                return 90;
            case SouthWest:
                return -45;
            case SouthEast:
                return 45;
            case NorthWest:
                return 135;
            case NorthEast:
                return 215;
        }
        return 0;
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
        Sprite num = new Sprite(numImages.get(health));
        num.setPosition(0, 0);
        num.setOrigin(widthmid, heightmid/2);
        num.setSize(480, heightmid);
        num.setRotation(getRotation());
        num.draw(batch);
        batch.draw(plus, bottomButton.x, bottomButton.y, bottomButton.height, bottomButton.width);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        dropImage.dispose();
        bucketImage.dispose();
        //dropSound.dispose();
        //rainMusic.dispose();
        batch.dispose();
        for(int i = 0; i <= 24; i++){
            numImages.get(i).dispose();
        }
        plus.dispose();
        minus.dispose();
    }
}