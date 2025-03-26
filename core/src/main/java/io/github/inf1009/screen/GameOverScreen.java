package io.github.inf1009.screen;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.inf1009.Grid;
import io.github.inf1009.ScoreEntry;
import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.ScoreManager;
import io.github.inf1009.manager.ViewportManager;

public class GameOverScreen implements Screen {
    private final Tetris game;
    private Texture backgroundTexture;
    private Texture newGameButtonTexture;
    private TextureButton newGameButton;
    private TextureButton quitButton;
    private final int worldWidth;
    private final int gameWidth;
    private final int worldHeight;
    private String player_name;

    private SceneManager sceneManager;
    private ViewportManager viewportManager;
    private Stage stage;
    private BitmapFont font;
    private List<ScoreEntry> topScores;

    public GameOverScreen(final Tetris game, String player_name) {
    	this.player_name=player_name;
        this.game = game;
        this.sceneManager = game.sceneManager;
        viewportManager = game.viewportManager;

        backgroundTexture = new Texture("game_over.png");
        stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);

        worldWidth = game.TOTAL_COLUMNS;
        worldHeight = game.GRID_ROWS;
        gameWidth = game.GRID_COLUMNS;
        
        
        ScoreManager scoreManager = new ScoreManager();
        topScores = scoreManager.getHighScores();
        
        //worldWidth = game.GRID_COLUMNS;
        //worldHeight = game.GRID_ROWS;
        
//         Create button using reusable TextureButton class
        newGameButton = new TextureButton("buttons/new_game_button.png", 4, 1, (float) gameWidth / 2, 6, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneManager.setScreen(new GameScreen(game, player_name));
                sceneManager.backgroundMusic.play();
                sceneManager.menuMusic.stop();
            }
        });

        quitButton = new TextureButton("buttons/quit_button.png", 4, 1, (float) gameWidth / 2, 5, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(newGameButton.getButton());
        stage.addActor(quitButton.getButton());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        
        viewportManager.draw();

        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, gameWidth, worldHeight);
        //Position of the leaderboard
        float leaderboardX = 5f;
        float leaderboardY = 8.8f;
        // X5 Y8.8 Mid
        game.font.draw(game.batch, "High Scores:", leaderboardX, leaderboardY);
        for (int i = 0; i < topScores.size(); i++) {
            ScoreEntry entry = topScores.get(i);
            String line = (i + 1) + ". " + entry.name + " - " + entry.score;
            game.font.draw(game.batch, line, leaderboardX, leaderboardY - (i + 1) * 0.33f);
          }

        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScreen(new MainMenuScreen(game));
            sceneManager.menuMusic.play();
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        viewportManager.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
    

    @Override
    public void dispose() {
        stage.dispose();
        newGameButton.dispose();
        backgroundTexture.dispose();
    }
}