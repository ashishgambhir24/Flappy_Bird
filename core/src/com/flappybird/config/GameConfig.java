package com.flappybird.config;

public class GameConfig {

    public static final float WIDTH= 480.0f;
    public static final float HEIGHT= 800.0f;

    public static final float HUD_WIDTH = 480.0f;
    public static final float HUD_HEIGHT = 800.0f;

    public static final float WORLD_WIDTH= 9.0f;
    public static final float WORLD_HEIGHT= 15.0f;

    public static final float WORLD_CENTER_X= WORLD_WIDTH/2f;
    public static final float WORLD_CENTER_Y= WORLD_HEIGHT/2f;

    public static final float OBSTACLE_SPAWN_TIME = 2f;

    public static final float MAX_PLAYER_DOWN_SPEED = 0.06f;
    public static final float MAX_PLAYER_UP_SPEED = 1.2f;
    public static final float MIN_PLAYER_Y = 4f;
    public static final float OBSTACLE_X_SPEED = 0.03f;
    public static final float OBSTACLE_MIN_HEIGHT = 1.5f;
    public static final float OBSTACLE_GAP = 3f;
    public static final float PLAYER_ACCELERATION = 0.12f;


    private GameConfig() {}
}
