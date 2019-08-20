package com.flappybird.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.flappybird.config.GameConfig;

public class Obstacle implements Pool.Poolable {

    private float x;
    private static final float y1 = GameConfig.MIN_PLAYER_Y;
    private float height1;
    private float y2;
    private float height2;

    private Rectangle rectDown;
    private Rectangle rectUp;

    private static final float OBSTACLE_WIDTH = 1f;

    private boolean hit;
    private boolean scoreUpdated;

    public Obstacle() {
        rectDown = new Rectangle(x , y1 , OBSTACLE_WIDTH , height1);
        rectUp = new Rectangle(x , y2 , OBSTACLE_WIDTH , height2);
    }

    public void drawDebug(ShapeRenderer renderer){
        renderer.rect(rectDown.x , rectDown.y , rectDown.width , rectDown.height);
        renderer.rect(rectUp.x, rectUp.y, rectUp.width , rectUp.height);
    }

    public void setPosition(float x ,float y2, float height1 , float height2){
        this.x = x;
        this.y2 = y2;
        this.height1 = height1;
        this.height2 = height2;
        updateRectangles();
    }

    public float getObstacleWidth(){
        return OBSTACLE_WIDTH;
    }

    public float getX() {
        return x;
    }

    public Rectangle getRectDown() {
        return rectDown;
    }

    public Rectangle getRectUp() {
        return rectUp;
    }

    public void update(){
        setPosition(x-GameConfig.OBSTACLE_X_SPEED , y2 , height1 , height2);
    }

    private void updateRectangles(){
        rectDown.setPosition(x , y1);
        rectDown.setSize(OBSTACLE_WIDTH , height1);
        rectUp.setPosition(x , y2);
        rectUp.setSize(OBSTACLE_WIDTH , height2);
    }

    public boolean isPlayerColliding(Player player){
        Circle playerBounds = player.getBounds();
        boolean overlaps1 = Intersector.overlaps(playerBounds , getRectDown());
        boolean overlaps2 = Intersector.overlaps(playerBounds , getRectUp());

        hit = overlaps1 || overlaps2;

        return overlaps1 || overlaps2;


    }

    public boolean isNotHit(){return !hit;}

    public boolean isPassedPlayer(Player player){
        float playerLeftMostX= player.getX();
        float obstacleRightMostX = getX() + OBSTACLE_WIDTH/2f;

        boolean passed = obstacleRightMostX < playerLeftMostX;

        scoreUpdated = passed;

        return passed;
    }

    public boolean isScoreNotUpdated(){return !scoreUpdated;}

    @Override
    public void reset() {
        hit=false;
        scoreUpdated=false;
    }
}
