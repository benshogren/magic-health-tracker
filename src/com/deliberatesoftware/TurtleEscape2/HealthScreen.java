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
    private int heightmid = 750/2;
    private Rotator.Direction currentDirection = Rotator.Direction.South;
    private int pulse = 0;
    private TurtleEscape2 game;
    private Texture bg;
    private ShapeRenderer shapeRenderer;
    private Rectangle resetButton;
    private boolean showResetDialog = false;
    private Texture resetDialogImage;
    private Texture playerHighlight;

    public HealthScreen ( TurtleEscape2 game, int gameSize ) {
        this.game = game;
        this.numPlayers = gameSize;
        this.highlightdPlayer = 1 + (int)(Math.random()*numPlayers);
    }

    @Override
    public void show() {
        // load the images for the droplet and the bucket, 48x48 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        bgColor = new Texture(Gdx.files.internal("health2.png"));
        bg = new Texture(Gdx.files.internal("Untitled.png"));
        playerHighlight = new Texture(Gdx.files.internal("highlight.png"));
        resetDialogImage = new Texture(Gdx.files.internal("resetDialogue.png"));
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
        resetButton = new Rectangle(0, 750, 480, 50);

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

        // drawing
        batch.begin();
        for(int i = 1; i<=numPlayers;i++) {
            drawPlayer(Players.get(i), (highlightdPlayer == i));
        }
        if (showResetDialog) {
            batch.draw(resetDialogImage, widthmid-128, heightmid-128, 256, 256);
        }
        batch.end();

        // process user input
        if(showResetDialog == false){
            if (listener.hadFling) {
                currentDirection = convertToDirection(listener.currentFling);
                listener.hadFling = false;
            } else if (listener.hadTap) {
                areRolling = false;
                Vector3 touchPos = listener.lastTap;
                listener.hadTap = false;
                camera.unproject(touchPos);
                if(resetButton.contains(touchPos.x, touchPos.y)) {
                   showResetDialog = true;
                }
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
        } else {
            if (listener.hadTap) {
                Vector3 touchPos = listener.lastTap;
                listener.hadTap = false;
                camera.unproject(touchPos);
                Rectangle yes = new Rectangle(widthmid-128, heightmid-128, 128, 128);
                Rectangle no = new Rectangle(widthmid, heightmid-128, 128, 128);
                if (yes.contains(touchPos.x, touchPos.y)) {
                    game.setScreen(new ChoosePlayersScreen(game));
                } else if (no.contains(touchPos.x, touchPos.y)) {
                    showResetDialog = false;
                }
            }
        }
        counter++;
        if (counter > counterSize) {
            if(counter > counterSize*2) { counter = 0; }
        }
        if(areRolling){
            rollCounter++;
            if (rollCounter == rollSpeed) {
                rollSpeed += 4;
                highlightdPlayer++;
                if (highlightdPlayer > numPlayers) {
                   highlightdPlayer = 1;
                }
                rollCounter = 0;
            }
            if (rollSpeed > 40) {
                areRolling = false;
            }
        }
    }

    private int rollCounter = 0;
    private boolean areRolling = true;
    private int rollSpeed = 5;
    private int highlightdPlayer = 1;

    private void colorManager(int health, Rectangle pos, boolean highlightPlayer){
        HashMap<Integer, Color> map = new HashMap<Integer, Color>();
        map.put(20, new Color(0.0f, 0.6f, 0, 1));
        map.put(19, new Color(0.1f, 0.6f, 0, 1));
        map.put(18, new Color(0.2f, 0.6f, 0, 1));
        map.put(17, new Color(0.3f, 0.6f, 0, 1));
        map.put(16, new Color(0.4f, 0.6f, 0, 1));
        map.put(15, new Color(0.5f, 0.6f, 0, 1));
        map.put(14, new Color(0.6f, 0.6f, 0, 1));
        map.put(13, new Color(0.6f, 0.6f, 0, 1));
        map.put(12, new Color(0.6f, 0.5f, 0, 1));
        map.put(11, new Color(0.6f, 0.4f, 0, 1));
        map.put(10, new Color(0.6f, 0.3f, 0, 1));
        map.put( 9, new Color(0.6f, 0.2f, 0, 1));
        map.put( 8, new Color(0.6f, 0.1f, 0, 1));
        map.put( 7, new Color(0.6f, 0.0f, 0, 1));
        map.put( 6, new Color(0.5f, 0.0f, 0, 1));
        map.put( 5, new Color(0.4f, 0.0f, 0, 1));
        map.put( 4, new Color(0.3f, 0.0f, 0, 1));
        map.put( 3, new Color(0.2f, 0.0f, 0, 1));
        map.put( 2, new Color(0.1f, 0.0f, 0, 1));
        map.put( 1, new Color(0.0f, 0.0f, 0, 1));
        map.put( 0, new Color(0.0f, 0f, 0, 0));
        if(health > 20){
            batch.setColor(new Color(0, 0.6f, 0, 1));
        } else {
            if (health < 5) {
                if (counter > counterSize) {
                    batch.setColor(new Color(0.3f, 0, 0, heartRatio()));
                } else {
                    Color c = map.get(health);
                    batch.setColor(c.r, c.g, c.b, heartRatio());
                }
            } else {
                batch.setColor(map.get(health));
            }
        }
        batch.draw(bgColor, pos.x, pos.y, pos.width, pos.height);
        batch.setColor(1, 1, 1, 1);
        if (health == 20 && highlightPlayer && areRolling) {
            batch.draw(playerHighlight, pos.x, pos.y, pos.width, pos.height);
        }
    }
    private float heartRatio() {
        if (counter > counterSize) {
            return Math.abs(((counter-counterSize)/counterSize)-1);
        }
        return counter/counterSize;
    }
    private int counter = 0;
    private final int counterSize = 30;

    private void drawPlayer(Player player, boolean highlightPlayer) {
        Rectangle pos = player.Position;
        colorManager(player.Health,pos, highlightPlayer);

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