package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

import static com.deliberatesoftware.TurtleEscape2.Rotator.convertToDirection;
import static com.deliberatesoftware.TurtleEscape2.Rotator.getRotation;

public class HealthScreen implements Screen {
    private Board board;
    Texture dropImage;
    Texture bucketImage;
    SpriteBatch batch;
    OrthographicCamera camera;
    private BitmapFont font;
    private MyGestureListener listener;
    private Texture bgColor;
    private Map<Integer, Texture> numImages = new HashMap<Integer, Texture>();
    private Texture plus;
    Rectangle topButton;
    Rectangle bottomButton;
    int numPlayers = 0;
    private Map<Integer, Player> Players = new HashMap<Integer, Player>();
    private Map<Integer, Map<Integer, Rectangle>> Boards = new HashMap<Integer, Map<Integer, Rectangle>>();
    private int widthmid = 480/2;
    private int heightmid = 800/2;
    private Rotator.Direction currentDirection = Rotator.Direction.South;
    private int pulse = 0;
    private TurtleEscape2 game;
    private Texture bg;
    private ShapeRenderer shapeRenderer;

    public HealthScreen ( TurtleEscape2 game, int gameSize ) {
        this.game = game;
        this.numPlayers = gameSize;
    }

    @Override
    public void show() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        bgColor = new Texture(Gdx.files.internal("health2.png"));
        bg = new Texture(Gdx.files.internal("Untitled.png"));
        for (int i = 0; i <= 33; i ++){
            numImages.put(i, new Texture(Gdx.files.internal("numbers/"+i+".png")));
        }
        plus = new Texture(Gdx.files.internal("numbers/+.png"));
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // create the raindrops array and spawn the first raindrop
        //font = new BitmapFont();
        listener = new MyGestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(listener));

        font = new BitmapFont(Gdx.files.internal("test.fnt"),
                Gdx.files.internal("test.png"), false);
        topButton = new Rectangle(0, (heightmid*2)-75, 75,75);
        bottomButton = new Rectangle(0, heightmid-75, 75,75);

        board = new Board(widthmid, heightmid);
        Boards.put(1, board.makeOnePlayerMap());
        Boards.put(2, board.makeTwoPlayerMap());
        Boards.put(3, board.makeFourPlayerMap());
        Boards.put(4, board.makeFourPlayerMap());
        Boards.put(5, board.makeSixPlayerMap());
        Boards.put(6, board.makeSixPlayerMap());
        Boards.put(7, board.makeEightPlayerMap());
        Boards.put(8, board.makeEightPlayerMap());

        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player(Boards.get(numPlayers).get(i));
            Players.put(i, player);
        }
    }

    @Override
    public void render(float delta) {
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
        shapeRenderer.setProjectionMatrix(camera.combined);

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
                    if (p.PlusPosition.contains(touchPos.x, touchPos.y)) {
                        if(p.Health < 33) {
                            p.Health++;
                        }
                    } else {
                        if(p.Health > 0) {
                            p.Health--;
                        }
                    }
                }
            }
        }
    }

    private float ratio(float lower, float upper, float current) {
        return Math.abs((current-lower)/(upper-lower)-1);
    }

    private float findRatio(float lower, float upper, float percent) {
        return ((upper-lower) * (percent)) + lower;
    }

    private float findGradient(float colorLower, float colorUpper, float healthLower, float healthUpper, int health, boolean inverse) {
        if (inverse ) {
            return findRatio(colorLower, colorUpper, ratio(healthLower, healthUpper, health));
        }
        return findRatio(colorLower, colorUpper, Math.abs(ratio(healthLower, healthUpper, health)-1));
    }

    private void backgroundGradiant (int health, Rectangle pos){
        float maxRed = 0.6f;
        float maxGreen = 0.6f;
        if(health > 20){
            batch.setColor(0, maxGreen, 0, 1.0f);
        } else if(health <= 20 && health > 14){
            batch.setColor( findGradient(0, maxRed/2, 15, 20, health, true), maxGreen, 0, 1.0f);
        } else if(health < 15 && health > 9){
            batch.setColor( findGradient(maxRed/2, maxRed, 10, 14, health, true), maxGreen, 0, 1.0f);
        } else if(health < 10 && health > 4){
            batch.setColor( maxRed, findGradient(0, maxGreen, 5, 9, health, false), 0, findGradient(0.7f, 1, 5, 9, health, false));
        } else {
            batch.setColor(maxRed, 0, 0, findGradient(0, 0.7f, 0, 4, health, false));
        }
        batch.draw(bgColor, pos.x, pos.y, pos.width, pos.height);
        batch.setColor(1, 1, 1, 1);
    }

    private void colorManager(int health, Rectangle pos){
        HashMap<Integer, Color> map = new HashMap<Integer, Color>();
        map.put(20, new Color(0.0f, 0.6f, 0, 1.0f));
        map.put(19, new Color(0.1f, 0.6f, 0, 1.0f));
        map.put(18, new Color(0.2f, 0.6f, 0, 1.0f));
        map.put(17, new Color(0.3f, 0.6f, 0, 1.0f));
        map.put(16, new Color(0.4f, 0.6f, 0, 1.0f));
        map.put(15, new Color(0.5f, 0.6f, 0, 1.0f));
        map.put(14, new Color(0.6f, 0.6f, 0, 1.0f));
        map.put(13, new Color(0.6f, 0.6f, 0, 1.0f));
        map.put(12, new Color(0.6f, 0.5f, 0, 1.0f));
        map.put(11, new Color(0.6f, 0.4f, 0, 1.0f));
        map.put(10, new Color(0.6f, 0.3f, 0, 1.0f));
        map.put( 9, new Color(0.6f, 0.2f, 0, 1.0f));
        map.put( 8, new Color(0.6f, 0.1f, 0, 1.0f));
        map.put( 7, new Color(0.6f, 0.0f, 0, 1.0f));
        map.put( 6, new Color(0.5f, 0.0f, 0, 1.0f));
        map.put( 5, new Color(0.4f, 0.0f, 0, 1.0f));
        map.put( 4, new Color(0.3f, 0.0f, 0, 1.0f));
        map.put( 3, new Color(0.2f, 0.0f, 0, 1.0f));
        map.put( 2, new Color(0.1f, 0.0f, 0, 1.0f));
        map.put( 1, new Color(0.0f, 0.0f, 0, 1.0f));
        map.put( 0, new Color(0.0f, 0f, 0, 0));
        if(health > 20){
            batch.setColor(new Color(0, 0.6f, 0, 1));
        } else {
            batch.setColor(map.get(health));
        }
        batch.draw(bgColor, pos.x, pos.y, pos.width, pos.height);
        batch.setColor(1, 1, 1, 1);
    }

    private void drawPlayer(Player player) {
        Rectangle pos = player.Position;
        //float percent = getHealthPercent(player.Health);
        //backgroundGradiant(player.Health, pos);
        colorManager(player.Health,pos);

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
        Rectangle plusR = player.PlusPosition;
        batch.draw(plus, plusR.x, plusR.y, plusR.width, plusR.height);
    }

    private float getHealthPercent(int health) {
        if(health > 20){
            return 1;
        }
        return (health*5)/100f;
    }

    @Override
    public void resize(int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hide() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        dropImage.dispose();
        bucketImage.dispose();
        //dropSound.dispose();
        //rainMusic.dispose();
        for(int i = 0; i <= 33; i++){
            numImages.get(i).dispose();
        }
        plus.dispose();
        bg.dispose();
        batch.dispose();
    }
}