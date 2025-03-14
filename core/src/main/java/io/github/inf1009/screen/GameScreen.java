package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
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

    private Block square;
    private float gameSpeed = 0.3f; //lower = faster
    private float timer = 0;
    private int worldWidth, worldHeight;

    private MovementManager movementManager;
    private InputManager inputManager;
    private GameStateManager gameStateManager;

    public GameScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = (int) game.fitViewport.getWorldWidth();
        worldHeight = (int) game.fitViewport.getWorldHeight();

        backgroundTexture = new Texture("spacetron.jpg");
        pausetexture= new Texture("gpause.png");

        grid = new Grid(worldWidth, worldHeight);
        // Randomly assign the first block as Recyclable or Unrecyclable
        Block.BlockType initialType = MathUtils.randomBoolean() ? Block.BlockType.RECYCLABLE : Block.BlockType.UNRECYCLABLE;
        square = new Block(0, worldHeight - 1, worldWidth, worldHeight, initialType);

        movementManager = new MovementManager(square, grid);
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

        logic();

        if (inputManager.gamepause) {
    		pause();
    	}
    	else {
    		resume();
    		draw();
    		timer += delta;
    	}

        gameStateManager.checkIllegalMove(square, grid);

        //return to main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.backgroundMusic.stop();
            sceneManager.menuMusic.play();
        }

        if (gameStateManager.isGameOver()) {
            sceneManager.setScreen(new GameOverScreen(game));
            sceneManager.backgroundMusic.stop();
        }



        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void logic() {
        if (timer >= gameSpeed) {
            square.fall(grid);
            timer = 0;
        }

        if (square.bottomCollision(grid)) {
            // Use the existing type of the falling block
            Block.BlockType currentType = square.getType();

            grid.addBlock(square.getGridX(), square.getGridY(), currentType); // Keep type the same
            grid.clearRow();

            // Spawn a new block with a fresh random type
            Block.BlockType newType = MathUtils.randomBoolean() ? Block.BlockType.RECYCLABLE : Block.BlockType.UNRECYCLABLE;
            square = new Block(square.getGridX(), grid.getRows() - 1, grid.getColumns(), grid.getRows(), newType);

            movementManager.setBlock(square);
        }


    }



    private void draw() {
        ScreenUtils.clear(Color.NAVY);

        game.leftStage.act();
        game.rightStage.act();

        game.leftViewport.apply();
        game.leftStage.draw();

        game.rightViewport.apply();
        game.rightStage.draw();

        game.fitViewport.apply();

        batch.setProjectionMatrix(game.fitViewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        batch.end();
        grid.draw(shapeRenderer, game.fitViewport);
        square.draw(shapeRenderer);
    }


    @Override
    public void resize(int width, int height) {
        game.fitViewport.update(width, height, true);
        game.leftViewport.update(game.fitViewport.getLeftGutterWidth(), height, true);
        game.leftViewport.setScreenPosition(0, 0);
        game.rightViewport.update(game.fitViewport.getRightGutterWidth(), height, true);
        game.rightViewport.setScreenPosition(game.fitViewport.getRightGutterX(), 0);
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
