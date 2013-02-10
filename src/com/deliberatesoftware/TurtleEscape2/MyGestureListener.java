package com.deliberatesoftware.TurtleEscape2;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGestureListener implements GestureDetector.GestureListener {
    public Vector3 lastFling;
    public boolean hadFling = false;

    public Vector3 lastTap;
    public boolean hadTap = false;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        hadTap = true;
        lastTap = new Vector3(x, y, 0);
        return true; //was processed
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        hadFling = true;
        lastFling = new Vector3(velocityX, velocityY, 0);
        return true; //was processed
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
