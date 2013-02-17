package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created with IntelliJ IDEA.
 * User: jack
 * Date: 2/16/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    public int Health = 20;
    public Rectangle Position;
    public Rectangle PlusPosition;

    public Player(Rectangle pos) {
        Position = pos;
        PlusPosition = new Rectangle(pos.x, (pos.y+pos.height)-75, 75, 75);
    }

}
