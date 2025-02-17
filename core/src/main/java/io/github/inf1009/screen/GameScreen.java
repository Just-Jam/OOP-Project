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
import io.github.inf1009.manager.SceneManager;

public class GameScreen implements Screen {
    private  Tetris game;
    private  SceneManager sceneManager;
    private  ShapeRenderer shapeRenderer;
    private  SpriteBatch batch;
    private  Array<FallingBlock> fallingBlocks;
    private  Grid grid;
    private  Texture backgroundTexture;

    private Block square;
    private float spawnTimer;
    private float gameSpeed = 1f;
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
        fallingBlocks = new Array<>();

        spawnTimer = 0;
        grid = new Grid(worldWidth, worldHeight);
        square = new Block(10, worldHeight - 1, worldWidth, worldHeight);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        spawnTimer += delta;
        timer += delta;

        // Spawn a new block every 1 second
        if (spawnTimer > 1) {
            spawnTimer = 0;
            spawnFallingBlock();
        }

        updateFallingBlocks(delta);
        input();
        logic();
        draw();

        // **NEW FEATURE** - Press ESC to go back to MainMenuScreen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
        }
    }

    private void input() {
        square.input(grid.getGridMatrix());
    }

    public void logic() {
        if (timer >= gameSpeed) {
            square.fall(grid);
            timer = 0;
        }

        if (square.bottomCollision(grid.getGridMatrix())) {
            grid.addBlock(square.getGridX(), square.getGridY());
            square.setGridY(worldHeight - 1);
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

        // Draw falling blocks
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (FallingBlock block : fallingBlocks) {
            block.draw(shapeRenderer);
        }
        shapeRenderer.end();

        grid.draw(shapeRenderer, game.fitViewport);
        square.draw(shapeRenderer);
    }

    private void spawnFallingBlock() {
        float blockX = MathUtils.random(0, worldWidth - 1);
        FallingBlock block = new FallingBlock(blockX, worldHeight, 1, 1);
        fallingBlocks.add(block);
    }

    private void updateFallingBlocks(float delta) {
        for (int i = fallingBlocks.size - 1; i >= 0; i--) {
            FallingBlock block = fallingBlocks.get(i);
            block.update(delta);

            if (block.isOutOfBounds(worldHeight)) {
                fallingBlocks.removeIndex(i);
            }
        }
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
