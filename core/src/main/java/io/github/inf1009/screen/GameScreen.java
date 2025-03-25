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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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

    private float gameSpeed = 0.3f; //lower = faster
    private float timer = 0;
    private int worldWidth, worldHeight;

    private ViewportManager viewportManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private GameStateManager gameStateManager;
    private EntityManager entityManager;
    private CharSequence ptest=String.valueOf(0);
    private Stage previewStage;
    private java.util.List<BlockShape> nextBlocks;
    
    public GameScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;

        worldWidth = game.GRID_COLUMNS;
        worldHeight = game.GRID_ROWS;

        backgroundTexture = new Texture("game_background.jpg");
        pausetexture= new Texture("game_pause.png");

        grid = new Grid(worldWidth, worldHeight, game.getSoundManager());
        entityManager = new EntityManager(grid);
        entityManager.spawnNewBlock();

        viewportManager = game.viewportManager;
        movementManager = new MovementManager(entityManager.getCurrentBlock(), grid, game.getSoundManager());
        inputManager = new InputManager(movementManager);
        Gdx.input.setInputProcessor(inputManager);

        gameStateManager = new GameStateManager(game.getSoundManager());
        
        previewStage = new Stage(new ScreenViewport());
        // Initialize the next-blocks queue.
        nextBlocks = new java.util.ArrayList<>();
        for (int i = 0; i < 3; i++) {
            nextBlocks.add(BlockFactory.createRandomBlock(worldWidth, worldHeight));
        }
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

        gameStateManager.checkIllegalMove(entityManager.getCurrentBlock(), grid);

        //return to main menu
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            sceneManager.setScreen(new MainMenuScreen(game));
//            sceneManager.backgroundMusic.stop();
//            sceneManager.menuMusic.play();
//        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
        handleGameOver();
    }
    private void handleGameOver() {
        if (gameStateManager.isGameOver() || Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            sceneManager.setScreen(new GameOverScreen(game));
            sceneManager.backgroundMusic.stop();
        }
    }

    public void logic() {
        if (timer >= gameSpeed) {
            entityManager.getCurrentBlock().fall(grid);
            timer = 0;
        }
        entityManager.update(movementManager, nextBlocks);
    }

    private void draw() {
        ScreenUtils.clear(Color.NAVY);

        batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        game.font.draw(game.batch, ptest , (worldWidth-1.88f), (worldHeight-0.7f));
        batch.end();
        grid.draw(shapeRenderer, viewportManager.getFitViewport());
        entityManager.draw(shapeRenderer);
        ptest=String.valueOf(grid.score);
        drawNextBlocksPreview();
    }
    
    private void drawNextBlocksPreview() {
        // Switch to the UI stage's camera (which uses screen coordinates)
        shapeRenderer.setProjectionMatrix(previewStage.getCamera().combined);
        
        // Define the preview area in screen coordinates.
        // For example, we want the preview area to be in the right 200 pixels.
        float previewX = Gdx.graphics.getWidth() - 180; // adjust as needed
        float previewY = Gdx.graphics.getHeight() - 100;  // adjust as needed
        float cellSize = 20;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < nextBlocks.size(); i++) {
            BlockShape previewBlock = nextBlocks.get(i);
            float currentOffsetY = previewY - i * (cellSize * 4); // vertical spacing between previews

            // Set the block color based on its type.
            if (previewBlock.getType() == BlockShape.BlockType.RECYCLABLE) {
                shapeRenderer.setColor(Color.GREEN);
            } else {
                shapeRenderer.setColor(Color.RED);
            }
         // Draw the block with scaling
            previewBlock.drawNextBlocks(shapeRenderer, previewX, currentOffsetY, cellSize);
        }
        shapeRenderer.end();
        
        // Restore the projection matrix to the game viewport.
        shapeRenderer.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
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

