package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

public class Board {

    private final int heightMid;
    private final int widthMid;
    private final int height;
    private final int width;

    public Board(int widthmid, int heightmid) {
        this.widthMid = widthmid;
        this.heightMid = heightmid;
        this.height = heightmid*2;
        this.width = widthmid*2;
    }

    HashMap<Integer, Rectangle> makeOnePlayerMap() {
        HashMap<Integer, Rectangle> onePlayerBoard = new HashMap<Integer, Rectangle>();
        onePlayerBoard.put(1, new Rectangle(0, 0, width, height));
        return onePlayerBoard;
    }
    HashMap<Integer, Rectangle> makeTwoPlayerMap() {
        HashMap<Integer, Rectangle> twoPlayerBoard = new HashMap<Integer, Rectangle>();
        twoPlayerBoard.put(1, new Rectangle(0, heightMid, width, heightMid));
        twoPlayerBoard.put(2, new Rectangle(0, 0, width, heightMid));
        return twoPlayerBoard;
    }

    HashMap<Integer, Rectangle> makeFourPlayerMap() {
        HashMap<Integer, Rectangle> fourPlayerBoard = new HashMap<Integer, Rectangle>();
        fourPlayerBoard.put(1, new Rectangle(0, heightMid, widthMid, heightMid));
        fourPlayerBoard.put(2, new Rectangle(widthMid, heightMid, widthMid, heightMid));
        fourPlayerBoard.put(3, new Rectangle(0, 0, widthMid, heightMid));
        fourPlayerBoard.put(4, new Rectangle(widthMid, 0, widthMid, heightMid));
        return fourPlayerBoard;
    }

    HashMap<Integer, Rectangle> makeSixPlayerMap() {
        HashMap<Integer, Rectangle> sixPlayerBoard = new HashMap<Integer, Rectangle>();
        sixPlayerBoard.put(1, new Rectangle(0, (height/3)*2, widthMid,height/3));
        sixPlayerBoard.put(2, new Rectangle(widthMid, (height/3)*2, widthMid,height/3));
        sixPlayerBoard.put(3, new Rectangle(0, (height/3), widthMid,height/3));
        sixPlayerBoard.put(4, new Rectangle(widthMid, (height/3), widthMid,height/3));
        sixPlayerBoard.put(5, new Rectangle(0, 0, widthMid,height/3));
        sixPlayerBoard.put(6, new Rectangle(widthMid, 0, widthMid,height/3));
        return sixPlayerBoard;
    }

    HashMap<Integer, Rectangle> makeEightPlayerMap() {
        HashMap<Integer, Rectangle> sixPlayerBoard = new HashMap<Integer, Rectangle>();
        sixPlayerBoard.put(1, new Rectangle(0, (height/4)*3, widthMid,height/4));
        sixPlayerBoard.put(2, new Rectangle(widthMid, (height/4)*3, widthMid,height/4));
        sixPlayerBoard.put(3, new Rectangle(0, (height/4)*2, widthMid,height/4));
        sixPlayerBoard.put(4, new Rectangle(widthMid, (height/4)*2, widthMid,height/4));
        sixPlayerBoard.put(5, new Rectangle(0, height/4, widthMid,height/4));
        sixPlayerBoard.put(6, new Rectangle(widthMid, height/4, widthMid,height/4));
        sixPlayerBoard.put(7, new Rectangle(0, 0, widthMid,height/4));
        sixPlayerBoard.put(8, new Rectangle(widthMid, 0, widthMid,height/4));
        return sixPlayerBoard;
    }
}