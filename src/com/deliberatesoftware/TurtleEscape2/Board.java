package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

public class Board {

    private final int heightMid;
    private final int widthMid;

    public Board(int widthmid, int heightmid) {
        this.widthMid = widthmid;
        this.heightMid = heightmid;
    }

    HashMap<Integer, Rectangle> makeFourPlayerBoard() {
        HashMap<Integer, Rectangle> fourPlayerBoard = new HashMap<Integer, Rectangle>();
        fourPlayerBoard.put(1, new Rectangle(0, heightMid, widthMid, heightMid));
        fourPlayerBoard.put(2, new Rectangle(widthMid, heightMid, widthMid, heightMid));
        fourPlayerBoard.put(3, new Rectangle(0, 0, widthMid, heightMid));
        fourPlayerBoard.put(4, new Rectangle(widthMid, 0, widthMid, heightMid));
        return fourPlayerBoard;
    }

    HashMap<Integer, Rectangle> makeTwoPlayerMap() {
        HashMap<Integer, Rectangle> twoPlayerBoard = new HashMap<Integer, Rectangle>();
        twoPlayerBoard.put(1, new Rectangle(0, heightMid, 480, heightMid));
        twoPlayerBoard.put(2, new Rectangle(0, 0, 480, heightMid));
        return twoPlayerBoard;
    }
}