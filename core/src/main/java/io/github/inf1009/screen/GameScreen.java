package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.*;


//core game logic goes here
public class GameScreen implements Screen {
    final Tetris game;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Array<FallingBlock> fallingBlocks; // List of falling blocks
    private float spawnTimer; // Timer for spawning new blocks
    private Grid grid;

    Texture backgroundTexture;

    PlayerObject bucket;
    Block square;

    Vector2 touchPos;
    int worldWidth;
    int worldHeight;

    private float gameSpeed = 1f;
    private float timer = 0;

    public GameScreen(final Tetris game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        touchPos = new Vector2();
        batch = game.batch;

        worldWidth = (int) game.fitViewport.getWorldWidth();
        worldHeight = (int) game.fitViewport.getWorldHeight();

        // load the images for the background, bucket and droplet
        backgroundTexture = new Texture("background.png");

        bucket = new PlayerObject("bucket.png", 1, 1, 10, 10, (int) worldWidth, (int) worldHeight);
        fallingBlocks = new Array<>();
        spawnTimer = 0;

        grid = new Grid(worldWidth, worldHeight);
        square = new Block(10, worldHeight -1, worldWidth, worldHeight);

    }

    @Override
    public void show() {

    }

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
        draw();

        if (timer >= gameSpeed) {
            square.update();
            timer = 0;
        }

    }

    private void input() {
        bucket.input();
        square.input();
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
        bucket.draw(batch, worldWidth);

        batch.end();
     // Draw falling blocks
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (FallingBlock block : fallingBlocks) {
            block.draw(shapeRenderer);
        }
        shapeRenderer.end();

        grid.drawGrid(shapeRenderer, game.fitViewport);
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
                fallingBlocks.removeIndex(i); // Remove block if it falls below the screen
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
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        bucket.dispose();
        shapeRenderer.dispose();
    }
}

