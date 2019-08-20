package com.flappybird.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.flappybird.config.GameConfig;

public class Player {

    private float x;
    private float y;
    private Circle bounds;

    private static final float BOUNDS_RADIUS = 0.4f; // world units
    private static final float SIZE = 2 * BOUNDS_RADIUS;

    private float yTimer =0;

    public Player() {
        bounds = new Circle(x,y,BOUNDS_RADIUS);
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setyTimer(float yTimer) {
        this.yTimer = yTimer;
    }

    public Circle getBounds() {
        return bounds;
    }

    public void update(float delta){
        yTimer+=delta;
        float yspeed = - yTimer*GameConfig.PLAYER_ACCELERATION;
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            yspeed = GameConfig.MAX_PLAYER_UP_SPEED;
            yTimer=0;
        }
        y+=yspeed;
        updateBounds();
    }


    public float getWidth(){
        return SIZE;
    }
    public float getHeight(){
        return SIZE;
    }

    private void updateBounds() {
        bounds.setPosition(x, y);
    }
}
