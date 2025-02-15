package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.inf1009.Tetris;


//core game logic goes here
public class GameScreen implements Screen {
    final Tetris game;
    private ShapeRenderer shapeRenderer;

    Texture backgroundTexture;
    Texture bucketTexture;
    Sprite bucketSprite;
    Vector2 touchPos;
    Rectangle bucketRectangle;


    private int gridSize;
    private int gridWidth;
    private int gridHeight;

    public GameScreen(final Tetris game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();

        // load the images for the background, bucket and droplet
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");

        touchPos = new Vector2();

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(2, 2);
        bucketRectangle = new Rectangle();

        gridHeight = (int) game.fitViewport.getWorldHeight();
        gridWidth = (int) game.fitViewport.getWorldWidth();
        gridSize = 50;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 100f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta); // move the bucket right
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta); // move the bucket left
        }
    }

    private void logic() {
        float worldWidth = game.fitViewport.getWorldWidth();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();
        float delta = Gdx.graphics.getDeltaTime();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);
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


        game.batch.setProjectionMatrix(game.fitViewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.fitViewport.getWorldWidth();
        float worldHeight = game.fitViewport.getWorldHeight();


        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        bucketSprite.draw(game.batch);
        game.batch.end();


        //draw grid
        shapeRenderer.setProjectionMatrix(game.fitViewport.getCamera().combined);
        // Begin drawing
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Set the color for the grid lines
        shapeRenderer.setColor(1, 1, 1, 1); // White color
        // Draw vertical lines
        for (int x = 0; x <= worldWidth; x++) {
            shapeRenderer.rect(x - 0.02f, 0, 0.04f, 20); // Thin rectangle for vertical line
        }
        // Draw horizontal lines as rectangles
        for (int y = 0; y <= worldHeight; y++) {
            shapeRenderer.rect(0, y - 0.02f, 32, 0.04f); // Thin rectangle for horizontal line
        }
        // End drawing
        shapeRenderer.end();


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
        bucketTexture.dispose();
    }
}

