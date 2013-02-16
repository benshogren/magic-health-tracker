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

import static com.deliberatesoftware.TurtleEscape2.Rotator.getRotation;
import static com.deliberatesoftware.TurtleEscape2.Rotator.convertToDirection;

public class TurtleEscape2 extends Game {
    Texture dropImage;
    Texture bucketImage;
    SpriteBatch batch;
    OrthographicCamera camera;
    private BitmapFont font;
    private MyGestureListener listener;
    private Texture bgColor;
    private Map<Integer, Texture> numImages = new HashMap<Integer, Texture>();
    private Texture minus;
    private Texture plus;
    Rectangle topButton;
    Rectangle bottomButton;
    int numPlayers = 4;
    private Map<Integer, Player> Players = new HashMap<Integer, Player>();
    private Map<Integer, Map<Integer, Rectangle>> Boards = new HashMap<Integer, Map<Integer, Rectangle>>();

    @Override
    public void create() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        bgColor = new Texture(Gdx.files.internal("health.png"));
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


        //two player
        HashMap<Integer, Rectangle> twoPlayerBoard = new HashMap<Integer, Rectangle>();
        twoPlayerBoard.put(1, new Rectangle(0, heightmid, 480, heightmid));
        twoPlayerBoard.put(2, new Rectangle(0, 0, 480, heightmid));
        Boards.put(2, twoPlayerBoard);

        //four player
        HashMap<Integer, Rectangle> fourPlayerBoard = new HashMap<Integer, Rectangle>();
        fourPlayerBoard.put(1, new Rectangle(0, heightmid, widthmid, heightmid));
        fourPlayerBoard.put(2, new Rectangle(widthmid, heightmid, widthmid, heightmid));
        fourPlayerBoard.put(3, new Rectangle(0, 0, widthmid, heightmid));
        fourPlayerBoard.put(4, new Rectangle(widthmid, 0, widthmid, heightmid));
        Boards.put(4, fourPlayerBoard);

        // 8 player

        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player(Boards.get(numPlayers).get(i));
            Players.put(i, player);
        }

    }


    private int widthmid = 480/2;
    private int heightmid = 800/2;
    private Rotator.Direction currentDirection = Rotator.Direction.South;
    private int lastRotate = 0;
    private int pulse = 0;
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
        for(int i = 1; i<=numPlayers;i++) {
            drawPlayer(Players.get(i));
        }
        batch.end();

        // process user input
        if (listener.hadFling) {
            currentDirection = convertToDirection(listener.currentFling);
            listener.hadFling = false;
        } else if (listener.hadTap) {
            Vector3 touchPos = listener.lastTap;
            listener.hadTap = false;
            camera.unproject(touchPos);
            for(int i=1;i<=numPlayers;i++) {
                Player p = Players.get(i);
                if (p.Position.contains(touchPos.x, touchPos.y)) {
                    if(p.Health > 0) {
                        p.Health--;
                    }
                }
            }
        }
    }

    private void drawPlayer(Player player) {
        float percent = getHealthPercent(player.Health);
        batch.setColor(0, 1, 0, percent);
        Rectangle pos = player.Position;
        batch.draw(bgColor, pos.x, pos.y, pos.width, pos.height);
        batch.setColor(0, 0, 0, 1);

        Sprite num = new Sprite(numImages.get(player.Health));
        int xCenter;
        int yCenter;
        if (pos.width > pos.height) {
            num.setPosition(pos.x + ((pos.width/2 - pos.height/2)), pos.y);
            num.setSize(pos.height, pos.height);
            xCenter = (int) (((pos.height)/2));
            yCenter = (int) (((pos.height)/2));
        } else {
            num.setPosition(pos.x, pos.y + ((pos.height/2 - pos.width/2)));
            num.setSize(pos.width, pos.width);
            xCenter = (int) (((pos.width)/2));
            yCenter = (int) (((pos.width)/2));
        }
        num.setOrigin(xCenter, yCenter); // pos.Center
        num.setRotation(getRotation(currentDirection));
        num.draw(batch);
        //batch.draw(plus, topButton.x, topButton.y, topButton.height, topButton.width);
    }

    private void drawTop(int health) {
        float percent = getHealthPercent(health);
        batch.setColor(0, 1, 0, percent);
        batch.draw(bgColor, 0, heightmid, 480, heightmid);
        batch.setColor(0, 0, 0, 1);
        Sprite num = new Sprite(numImages.get(health));
        num.setPosition(0, heightmid);
        num.setSize(480, heightmid);
        num.setOrigin(widthmid, heightmid / 2);
        num.setRotation(getRotation(currentDirection));
        num.draw(batch);
        batch.draw(plus, topButton.x, topButton.y, topButton.height, topButton.width);
    }
    private float getHealthPercent(int health) {
        return (health*5)/100f;
    }

    private void drawBottem(int health) {
        float percent = getHealthPercent(health);
        batch.setColor(0, 1, 0, percent);
        batch.draw(bgColor, 0, 0, 480, heightmid);
        batch.setColor(0, 0, 0, 1);
        Sprite num = new Sprite(numImages.get(health));
        num.setPosition(0, 0);
        num.setOrigin(widthmid, heightmid/2);
        num.setSize(480, heightmid);
        num.setRotation(getRotation(currentDirection));
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