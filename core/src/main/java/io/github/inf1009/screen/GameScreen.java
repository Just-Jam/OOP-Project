package io.github.inf1009.screen;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.inf1009.*;
import io.github.inf1009.event.EventManager;
import io.github.inf1009.manager.*;

public class GameScreen implements Screen {
    private Tetris game;
    private SceneManager sceneManager;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Grid grid;
    private Texture backgroundTexture;
    private Texture pausetexture;
    private Texture nextBlockBoard;

    private int worldWidth, worldHeight, gameWidth;
    private String player_name;

    private ViewportManager viewportManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private GameStateManager gameStateManager;
    private EntityManager entityManager;
    private CharSequence ptest=String.valueOf(0);
    ScoreManager scoreManager = new ScoreManager();

    public GameScreen(final Tetris game, String name) {
    	player_name=name;
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = game.TOTAL_COLUMNS;
        worldHeight = game.GRID_ROWS;
        gameWidth = game.GRID_COLUMNS;

        backgroundTexture = new Texture("game_background.jpg");
        pausetexture= new Texture("game_pause.png");
        nextBlockBoard = new Texture("next_block_board.png");

        grid = new Grid(gameWidth, worldHeight, game.getSoundManager());
        entityManager = new EntityManager(grid);
        entityManager.spawnNewBlock();

        viewportManager = game.viewportManager;
        movementManager = new MovementManager(grid, game.getSoundManager());
        inputManager = new InputManager(movementManager);
        Gdx.input.setInputProcessor(inputManager);

        gameStateManager = new GameStateManager();
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        viewportManager.draw();
        entityManager.update();


        if (inputManager.gamepause) {
    		pause();
    	}
    	else {
    		resume();
    		draw();
            gameStateManager.update(delta);
    	}

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    private void handleGameOver() {
        	//Initialize the boolean flag to allow this code to be printed just once
        	boolean scoresPrinted = false;

        	if (!scoresPrinted) {
                int finalScore = grid.getPlayerScore(); //Get latest visible score
                //For debugging the output
                //System.out.println("DEBUG: Final Score at Game Over = " + finalScore);

                //Adding score to database (Name, score)
                scoreManager.addScore(player_name, finalScore);

                List<ScoreEntry> topScores = scoreManager.getHighScores();
                for (int i = 0; i < topScores.size(); i++) {
                    ScoreEntry entry = topScores.get(i);
                    //Change this to print method in game screen to print on screen
                    System.out.println((i + 1) + ". " + entry.name + " - " + entry.score);
                }
                //boolean flag to stop the code above from looping
                scoresPrinted = true;
                sceneManager.setScreen(new GameOverScreen(game,player_name));
                sceneManager.backgroundMusic.stop();

                /* Only use this for testing
                scoreManager.clearScores(); //clear old score for testing fresh start
                scoreManager.addScore("Player1", finalScore);
                */
            }
    }

    private void draw() {
        ScreenUtils.clear(Color.NAVY);

        batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, gameWidth, worldHeight);
        batch.draw(nextBlockBoard, game.GRID_COLUMNS, 6, 4, 8);
        game.font.draw(game.batch, ptest , (worldWidth-1.88f), (worldHeight-0.7f));
        batch.end();

        // Draw the grid and current block
        grid.draw(shapeRenderer, viewportManager.getFitViewport());
        entityManager.draw(shapeRenderer, batch);

        ptest=String.valueOf(grid.getPlayerScore());
        // Draw next blocks preview
        drawNextBlocksPreview();
    }


    private void drawNextBlocksPreview() {
        shapeRenderer.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);

        float previewX = gameWidth + 1.4f;
        float previewY = worldHeight - 4.3f;

        batch.begin();
        for (int i = 0; i < entityManager.getNextBlocks().size(); i++) {
            BlockShape previewBlock = entityManager.getNextBlocks().get(i);
            float currentOffsetY = previewY - i * 2f; // vertical spacing
            previewBlock.drawNextBlocks(batch, previewX, currentOffsetY);
        }
        batch.end();
        ptest=String.valueOf(grid.getPlayerScore());
    }


    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {
    	sceneManager.backgroundMusic.pause();

        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(pausetexture, 0, 0, gameWidth, worldHeight);
        batch.end();
    }

    @Override
    public void resume() {
    	sceneManager.backgroundMusic.play();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        shapeRenderer.dispose();
    }
}
