package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import io.github.inf1009.*;
import io.github.inf1009.manager.*;

import java.util.List;

public class GameOverScreen implements Screen {
    private static final float LEADERBOARD_X = 5f;
    private static final float LEADERBOARD_Y = 8.7f;

    private final Tetris game;
    private final SceneManager sceneManager;
    private final ViewportManager viewportManager;
    private final ScoreManager scoreManager;

    private final String playerName;
    private final int finalScore;

    private final int worldWidth;
    private final int gameWidth;
    private final int worldHeight;

    private final Stage stage;
    private final Texture backgroundTexture;
    private final TextureButton newGameButton;
    private final TextureButton quitButton;

    private final List<ScoreEntry> topScores;

    public GameOverScreen(final Tetris game, String playerName, int finalScore) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.viewportManager = game.viewportManager;
        this.scoreManager = new ScoreManager();
        this.playerName = playerName;
        this.finalScore = finalScore;

        this.worldWidth = game.TOTAL_COLUMNS;
        this.gameWidth = game.GRID_COLUMNS;
        this.worldHeight = game.GRID_ROWS;

        this.backgroundTexture = new Texture("screen/game_over.png");
        this.stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        
        scoreManager.addScore(playerName, finalScore); //add name, score to database
        this.topScores = scoreManager.getHighScores();

        this.newGameButton = createNewGameButton();
        this.quitButton = createQuitButton();

        stage.addActor(newGameButton.getButton());
        stage.addActor(quitButton.getButton());
    }

    private TextureButton createNewGameButton() {
        return new TextureButton("buttons/new_game_button.png", 4, 1,
                (float) gameWidth / 2, 6, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new GameScreen(game, playerName));
                sceneManager.backgroundMusic.play();
                sceneManager.menuMusic.stop();
            }
        });
    }

    private TextureButton createQuitButton() {
        return new TextureButton("buttons/quit_button.png", 4, 1,
                (float) gameWidth / 2, 5, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewportManager.draw();

        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, gameWidth, worldHeight);

        // Optional: display player’s score
        game.font.draw(game.batch, "Your Score: " + finalScore, LEADERBOARD_X-1, LEADERBOARD_Y+0.4f);

        drawLeaderboard();
        game.batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.menuMusic.play();
        }
    }

    private void drawLeaderboard() {
        game.font.draw(game.batch, "High Scores:", LEADERBOARD_X, LEADERBOARD_Y);
        for (int i = 0; i < topScores.size(); i++) {
            ScoreEntry entry = topScores.get(i);
            String line = (i + 1) + ". " + entry.name + " - " + entry.score;
            game.font.draw(game.batch, line, LEADERBOARD_X, LEADERBOARD_Y - (i + 1) * 0.38f);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        newGameButton.dispose();
        quitButton.dispose();
        backgroundTexture.dispose();
    }
}
