package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.inf1009.*;
import io.github.inf1009.manager.*;
import java.util.List;
import java.util.ArrayList;

public class GameScreen implements Screen {
    private Tetris game;
    private SceneManager sceneManager;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Grid grid;
    private Texture backgroundTexture;
    private Texture nextBlockBoard;
    private Texture scoreBoardTexture;

    private float gameSpeed;
    private ViewportManager viewportManager;
    private MovementManager movementManager;
    private InputManager inputManager;
    private GameStateManager gameStateManager;
    private EntityManager entityManager;
    private Stage previewStage;
    private List<BlockShape> nextBlocks;
    private String playerName;
    private ScoreManager scoreManager;

    private float timer;

    public GameScreen(final Tetris game, String name) {
        this.game = game;
        this.playerName = name;
        this.sceneManager = game.sceneManager;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = game.batch;
        this.viewportManager = game.viewportManager;
        this.scoreManager = new ScoreManager();

        int worldWidth = game.TOTAL_COLUMNS;
        int worldHeight = game.GRID_ROWS;
        int gameWidth = game.GRID_COLUMNS;

        this.scoreBoardTexture = new Texture("ScoreBoard.png");
        this.backgroundTexture = new Texture("screen/GameScreen.png");
        this.nextBlockBoard = new Texture("next_block_board.png");

        this.grid = new Grid(gameWidth, worldHeight, game.getSoundManager());
        this.entityManager = new EntityManager(grid);
        this.nextBlocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            nextBlocks.add(BlockFactory.createRandomBlock(gameWidth, worldHeight));
        }
        this.entityManager.spawnNewBlock(nextBlocks.remove(0));
        nextBlocks.add(BlockFactory.createRandomBlock(gameWidth, worldHeight));

        this.movementManager = new MovementManager(entityManager.getCurrentBlock(), grid, game.getSoundManager());
        this.inputManager = new InputManager(movementManager);
        Gdx.input.setInputProcessor(inputManager);

        this.gameStateManager = new GameStateManager(game.getSoundManager());
        this.previewStage = new Stage(new ScreenViewport());

        this.gameSpeed = 0.3f;
        this.timer = 0f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.NAVY);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        viewportManager.draw();

        if (inputManager.isGamePaused()) {
            inputManager.resetPauseFlag();
            gameStateManager.pauseGame();
            sceneManager.pushScreen(new PauseScreen(game, this, inputManager));
            return;
        }

        updateGameSpeed();
        updateLogic(delta);

        if (gameStateManager.isGameOver()) {
            int finalScore = grid.getPlayerScore();
            sceneManager.setScreen(new GameOverScreen(game, playerName, finalScore));
            sceneManager.backgroundMusic.stop();
            return;
        }

        draw();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void updateGameSpeed() {
        int score = grid.getPlayerScore();
        if (score > 3000) gameSpeed = 0.15f;
        else if (score > 2000) gameSpeed = 0.2f;
        else if (score > 1000) gameSpeed = 0.25f;
        else gameSpeed = 0.3f;
    }

    private void updateLogic(float delta) {
        timer += delta;
        if (timer >= gameSpeed) {
            entityManager.getCurrentBlock().fall(grid);
            timer = 0;
        }
        entityManager.update(movementManager, nextBlocks);
        gameStateManager.checkIllegalMove(entityManager.getCurrentBlock(), grid);
    }

    private void draw() {
        int worldWidth = game.TOTAL_COLUMNS;
        int worldHeight = game.GRID_ROWS;
        int gameWidth = game.GRID_COLUMNS;

        batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, gameWidth, worldHeight);
        batch.draw(scoreBoardTexture, game.GRID_COLUMNS, 14, 4, 2);
        batch.draw(nextBlockBoard, game.GRID_COLUMNS, 6, 4, 8);
        game.font.draw(batch, String.valueOf(grid.getPlayerScore()), worldWidth - 2.4f, worldHeight - 0.77f);
        batch.end();

        grid.draw(shapeRenderer, viewportManager.getFitViewport());
        entityManager.draw(shapeRenderer, batch);
        drawNextBlocksPreview();
    }

    private void drawNextBlocksPreview() {
        shapeRenderer.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);

        float previewX = game.GRID_COLUMNS + 1.4f;
        float previewY = game.GRID_ROWS - 4.3f;

        batch.begin();
        for (int i = 0; i < nextBlocks.size(); i++) {
            BlockShape previewBlock = nextBlocks.get(i);
            float currentOffsetY = previewY - i * 2f;
            previewBlock.drawNextBlocks(batch, previewX, currentOffsetY);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(inputManager);
        sceneManager.backgroundMusic.play();
        gameStateManager.resumeGame();
    }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {
        gameStateManager.resumeGame();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        scoreBoardTexture.dispose();
        shapeRenderer.dispose();
        nextBlockBoard.dispose();
        if (grid instanceof Disposable)
        	((Disposable) grid).dispose();
    }
}