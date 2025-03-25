package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.inf1009.*;
import io.github.inf1009.manager.*;

public class GameScreen implements Screen {
    private Tetris game;
    private SceneManager sceneManager;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Grid grid;
    private Texture backgroundTexture;
    private Texture pausetexture;

    private BlockShape block;
    private float gameSpeed = 0.3f; //lower = faster
    private float timer = 0;
    private int worldWidth, worldHeight;

    private ViewportManager viewportManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private GameStateManager gameStateManager;

    public GameScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = game.GRID_COLUMNS;
        worldHeight = game.GRID_ROWS;

        backgroundTexture = new Texture("game_background.jpg");
        pausetexture= new Texture("game_pause.png");

        grid = new Grid(worldWidth, worldHeight);
        block = BlockFactory.createRandomBlock(worldWidth, worldHeight);

        viewportManager = game.viewportManager;
        movementManager = new MovementManager(block, grid);
        inputManager = new InputManager(movementManager);
        Gdx.input.setInputProcessor(inputManager);

        gameStateManager = new GameStateManager();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        viewportManager.draw();
        logic();

        if (inputManager.gamepause) {
    		pause();
    	}
    	else {
    		resume();
    		draw();
    		timer += delta;
    	}

        gameStateManager.checkIllegalMove(block, grid);

        //return to main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.backgroundMusic.stop();
            sceneManager.menuMusic.play();
        }

        if (gameStateManager.isGameOver() || Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            sceneManager.setScreen(new GameOverScreen(game));
            sceneManager.backgroundMusic.stop();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void logic() {
        if (timer >= gameSpeed) {
            block.fall(grid);
            timer = 0;
        }

        if (block.bottomCollision(grid)) {
            block.placeOnGrid(grid); // places all sub-blocks into grid
            grid.clearRow();

            // Spawn a new block with a fresh random type
            block = BlockFactory.createRandomBlock(grid.getColumns(), grid.getRows());

            movementManager.setBlock(block);
        }


    }



    private void draw() {
        ScreenUtils.clear(Color.NAVY);

        batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        batch.end();
        grid.draw(shapeRenderer, viewportManager.getFitViewport());
        block.draw(shapeRenderer);
    }


    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {
    	timer=0;
    	sceneManager.backgroundMusic.pause();

        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(pausetexture, 0, 0, worldWidth, worldHeight);
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

