package com.flappybird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.flappybird.config.GameConfig;
import com.flappybird.entity.Background;
import com.flappybird.entity.Obstacle;
import com.flappybird.entity.Player;

public class GameController {

    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);
    private Player player;
    private final float startPlayerX = GameConfig.WORLD_WIDTH/4f;
    private final float startPlayerY = (GameConfig.WORLD_HEIGHT + GameConfig.MIN_PLAYER_Y)/2f;

    private Array<Obstacle> obstacles = new Array<>();
    private float obstacleTimer = 4f;

    private Pool<Obstacle> obstaclePool;

    private boolean gameOver = true;
    private boolean start =false;

    private int score=0;
    private int highScore = 0;

    private Background background;

    public GameController(){
        init();
    }

    private void init(){
        player = new Player();
        player.setPosition(startPlayerX,startPlayerY);

        obstaclePool = Pools.get(Obstacle.class, 10);

        background = new Background();
        background.setPosition(0,0);
        background.setSize(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT);
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public Background getBackground() {
        return background;
    }

    public void update(float delta){
        if(isgameOver()){
            playAgain();
        }
        else {
            updatePlayer(delta);
            updateObstacles(delta);
            updateScore();


            if(isPlayerCollidingWithObstaclesorGround()){
                log.debug("Collision Detected, GAME OVER!!");
                start =true;
                gameOver = true;
            }
        }
    }

    public boolean isgameOver(){
        return gameOver;
    }

    public boolean isStart() {
        return start;
    }

    private void updatePlayer(float delta){
        player.update(delta);
        blockPlayerFromLeaving();
    }

    private void blockPlayerFromLeaving(){
        float playerY = MathUtils.clamp(player.getY() , GameConfig.MIN_PLAYER_Y + player.getHeight()/2f , GameConfig.WORLD_HEIGHT - player.getHeight()/2f);
        player.setPosition(player.getX() , playerY);
    }

    private void updateObstacles(float delta){
        for (Obstacle obstacle: obstacles){
            obstacle.update();
        }
        createNewObstacle(delta);
        removePassedObstacle();
    }

    private void createNewObstacle(float delta){
        obstacleTimer+=delta;
        if(obstacleTimer>=GameConfig.OBSTACLE_SPAWN_TIME){
            float min = GameConfig.OBSTACLE_MIN_HEIGHT;
            float max = GameConfig.WORLD_HEIGHT -GameConfig.MIN_PLAYER_Y -  GameConfig.OBSTACLE_MIN_HEIGHT - GameConfig.OBSTACLE_GAP;

            float obstaclex = GameConfig.WORLD_WIDTH;
            float obstacleheight1 = MathUtils.random(min,max);
            float obstacley2 = GameConfig.MIN_PLAYER_Y + obstacleheight1 + GameConfig.OBSTACLE_GAP;
            float obstacleheight2 = GameConfig.WORLD_HEIGHT - obstacley2;

            Obstacle obstacle = obstaclePool.obtain();
            obstacle.setPosition(obstaclex , obstacley2 , obstacleheight1 , obstacleheight2);

            obstacles.add(obstacle);
            obstacleTimer = 0;
        }
    }

    private void removePassedObstacle(){
        if(obstacles.size>0){
            Obstacle first = obstacles.first();
            float minObstacleX = -first.getObstacleWidth();
            if(first.getX()<minObstacleX){
                obstacles.removeValue(first,true);
                obstaclePool.free(first);
            }
        }
    }

    private boolean isPlayerCollidingWithObstaclesorGround(){

        float playerFootY = player.getY() - player.getHeight()/2f;
        if(playerFootY<=GameConfig.MIN_PLAYER_Y){
            return true;
        }

        for (Obstacle obstacle : obstacles){
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)){
                return true;
            }
        }
        return false;
    }

    private void updateScore(){
        if(obstacles.size>0){
            Obstacle first = obstacles.first();
            if(first.isScoreNotUpdated() && first.isPassedPlayer(player)){
                score++;
                log.debug("" + score);
            }
        }

        if(highScore<score){
            highScore = score;
        }
    }

    private void playAgain(){
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            gameOver = false;
            score=0;
            player.setyTimer(0);
            restart();
        }
    }

    private void restart(){
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX,startPlayerY);
    }
}

