package com.flappybird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.flappybird.assets.AssetsPath;
import com.flappybird.config.GameConfig;
import com.flappybird.entity.Background;
import com.flappybird.entity.Obstacle;
import com.flappybird.entity.Player;
import com.flappybird.util.GdxUtils;
import com.flappybird.util.ViewportUtils;
import com.flappybird.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont scoreFont;
    private BitmapFont gameOverFont;
    private BitmapFont smallFont;

    private final GlyphLayout layout = new GlyphLayout();
    private final GlyphLayout smallLayout = new GlyphLayout();

    private Texture backgroundTexture;
    private Texture playerTexture;

    private DebugCameraController debugCameraController;

    private GameController controller;

    public GameRenderer(GameController controller) {
        this.controller = controller;
        init();
    }

    private void init(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT , camera);
        renderer= new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH , GameConfig.HUD_HEIGHT , hudCamera);
        batch = new SpriteBatch();
        scoreFont = new BitmapFont(Gdx.files.internal(AssetsPath.SCORE_FONT));
        gameOverFont = new BitmapFont(Gdx.files.internal(AssetsPath.GAMEOVER_FONT));
        smallFont = new BitmapFont(Gdx.files.internal(AssetsPath.GAMEOVER_FONT_SMALL));

        backgroundTexture = new Texture(Gdx.files.internal(AssetsPath.BACKGROUND));
        playerTexture = new Texture(Gdx.files.internal(AssetsPath.PLAYER_TEXTURE));

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta){
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        GdxUtils.clearScreen();

        renderGameplay();

        renderUI();

        renderDebug();
    }

    public void resize(int width , int height){
        viewport.update(width,height,true);
        hudViewport.update(width,height,true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    private void renderGameplay(){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Background background = controller.getBackground();
        batch.draw(backgroundTexture , 0, 0 , background.getWidth() , background.getHeight());

        Player player = controller.getPlayer();
        batch.draw(playerTexture , player.getX() - player.getWidth()/2f , player.getY() - player.getHeight()/2f , player.getWidth() , player.getHeight());

        batch.end();
    }

    private void renderUI(){
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        if(controller.isgameOver() && controller.isStart()){

            String gameOverText = "GAME OVER!!";
            layout.setText(gameOverFont , gameOverText);
            gameOverFont.draw(batch, gameOverText , (GameConfig.HUD_WIDTH - layout.width)/2f , 2*(GameConfig.HUD_HEIGHT + layout.height)/3f );

            String scoreText = "Score: " + controller.getScore();
            smallFont.draw(batch , scoreText , (GameConfig.HUD_WIDTH - layout.width)/2f , 2*(GameConfig.HUD_HEIGHT + layout.height)/3f - layout.height - 20f);

            String highScoreText = "High Score: " + controller.getHighScore();
            smallLayout.setText(smallFont , highScoreText);
            smallFont.draw(batch, highScoreText , (GameConfig.HUD_WIDTH + layout.width)/2f - smallLayout.width , 2*(GameConfig.HUD_HEIGHT + layout.height)/3f - layout.height - 20f);

        }else if(controller.isgameOver() && !controller.isStart()){

            String startText = "START";
            layout.setText(gameOverFont , startText);
            gameOverFont.draw(batch , startText , (GameConfig.HUD_WIDTH - layout.width)/2f , 2*(GameConfig.HUD_HEIGHT + layout.height)/3f);

        }else{

            String scoreText = "" + controller.getScore();
            layout.setText(scoreFont , scoreText);
            scoreFont.draw(batch , scoreText , (GameConfig.HUD_WIDTH - layout.width)/2f , 3*(GameConfig.HUD_HEIGHT + layout.height)/4f);
        }

        batch.end();
    }

    private void renderDebug(){
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        ViewportUtils.drawGrid(viewport,renderer);
    }

    private void drawDebug(){
        renderer.setColor(Color.RED);
        Player player = controller.getPlayer();
        player.drawDebug(renderer);

        renderer.setColor(Color.BROWN);

        Array<Obstacle> obstacles = controller.getObstacles();
        for (Obstacle obstacle : obstacles){
            obstacle.drawDebug(renderer);
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        gameOverFont.dispose();
        scoreFont.dispose();
        smallFont.dispose();
        backgroundTexture.dispose();
        playerTexture.dispose();
    }
}
