package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: jack
 * Date: 2/16/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rotator {
    public static Direction convertToDirection(Vector3 currentFling) {
        float x = currentFling.x;
        float y = currentFling.y;
        float absX = Math.abs(x);
        float absY = Math.abs(y);

        if (absY > absX) {
            if (y < 0){
                if(absY/2 > absX) {
                    return Direction.North;
                } else if(x > 0) {
                    return Direction.NorthWest;
                } else {
                    return Direction.NorthEast;
                }
            } else {
                if(absY/2 > absX) {
                    return Direction.South;
                } else if(x > 0) {
                    return Direction.SouthEast;
                } else {
                    return Direction.SouthWest;
                }
            }
        } else {
            if (x > 0){
                if(absX/2 > absY) {
                    return Direction.East;
                } else if(y < 0) {
                    return Direction.NorthWest;
                } else {
                    return Direction.SouthEast;
                }
            } else {
                if(absX/2 > absY) {
                    return Direction.West;
                } else if(y < 0) {
                    return Direction.NorthEast;
                } else {
                    return Direction.SouthWest;
                }
            }
        }
    }
    public static int getRotation(Direction currentDirection){
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
}
