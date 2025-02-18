package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Block;
import io.github.inf1009.Grid;
import io.github.inf1009.Tetris;
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

    public GameScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = (int) game.fitViewport.getWorldWidth();
        worldHeight = (int) game.fitViewport.getWorldHeight();

        backgroundTexture = new Texture("background.png");

        grid = new Grid(worldWidth, worldHeight);
        square = new Block(0, worldHeight - 1, worldWidth, worldHeight);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        timer += delta;

        // Use MovementManager for movement
        MovementManager.updateBlock(square,grid, delta);

        logic();
        draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
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
        }
    }


    private void draw() {
        ScreenUtils.clear(Color.DARK_GRAY);

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
        game.rightViewport.update(game.fitViewport.getRightGutterWidth(), height, true);
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
