package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.inf1009.*;
import io.github.inf1009.manager.CollisionManager;
import io.github.inf1009.manager.InputManager;
import io.github.inf1009.manager.MovementManager;
import io.github.inf1009.manager.SceneManager;

public class GameScreen implements Screen {
    private Tetris game;
    private SceneManager sceneManager;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Grid grid;
    private Texture backgroundTexture;

    private Block square;
    private float gameSpeed = 0.3f; //lower = faster
    private float timer = 0;
    private int worldWidth, worldHeight;

    private MovementManager movementManager;
    private InputManager inputManager;

    public GameScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = (int) game.fitViewport.getWorldWidth();
        worldHeight = (int) game.fitViewport.getWorldHeight();

        backgroundTexture = new Texture("spacetron.jpg");

        grid = new Grid(worldWidth, worldHeight);
        square = new Block(0, worldHeight - 1, worldWidth, worldHeight);

        movementManager = new MovementManager(square, grid);
        inputManager = new InputManager(movementManager);
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
    	timer += delta;

        logic();
        draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.backgroundMusic.stop();
            sceneManager.menuMusic.play();
        }
    }

    public void logic() {
        if (timer >= gameSpeed) {
            square.fall(grid);
            timer = 0;
        }
        if (square.bottomCollision(grid)) {
            grid.addBlock(square.getGridX(), square.getGridY()); // Stop block and mark it
            square = new Block(square.getGridX(), worldHeight - 1, worldWidth, worldHeight); // Spawn new block
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        shapeRenderer.dispose();
    }
}
