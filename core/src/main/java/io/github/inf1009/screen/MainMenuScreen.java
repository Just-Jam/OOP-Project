package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.inf1009.Tetris;
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.SceneManager;
import io.github.inf1009.manager.ViewportManager;

public class MainMenuScreen implements Screen {

    private static final float BUTTON_WIDTH = 4f;
    private static final float BUTTON_HEIGHT = 1f;
    private static final float PLAY_Y = 7f;
    private static final float CREDITS_Y = 5.5f;
    private static final float HOW_TO_PLAY_Y = 4f;

    private final Tetris game;
    private final SceneManager sceneManager;
    private final ViewportManager viewportManager;

    private final int worldWidth;
    private final int worldHeight;

    private final Texture backgroundTexture;
    private final Stage stage;

    private final TextureButton playButton;
    private final TextureButton creditButton;
    private final TextureButton howToPlayButton;
    
    float centerX;
    TextField nameField;
    private String playerName;

    public MainMenuScreen(final Tetris game) {
        this.game = game;
        this.sceneManager = game.sceneManager;
        this.viewportManager = game.viewportManager;

        this.worldWidth = game.TOTAL_COLUMNS;
        this.worldHeight = game.GRID_ROWS;
        centerX = worldWidth / 2f;
        this.backgroundTexture = new Texture("screen/title_art.png");
        this.stage = new Stage(viewportManager.getFitViewport());
        Gdx.input.setInputProcessor(stage);
        sceneManager.menuMusic.play();
      
        this.playButton = createButton("buttons/play_button.png", PLAY_Y, () ->    
        sceneManager.setScreen(new GameScreen(game, (playerName = nameField.getText().trim())))
        );

        this.creditButton = createButton("buttons/credits_button.png", CREDITS_Y, () ->
            sceneManager.setScreen(new CreditsScreen(game))
        );

        this.howToPlayButton = createButton("buttons/howtoplay_button.png", HOW_TO_PLAY_Y, () ->
            sceneManager.setScreen(new InstructionScreen(game))
        );
        nametext();

        stage.addActor(playButton.getButton());
        stage.addActor(creditButton.getButton());
        stage.addActor(howToPlayButton.getButton());
    }

    private TextureButton createButton(String texturePath, float y, Runnable onClick) {
        

        return new TextureButton(texturePath, BUTTON_WIDTH, BUTTON_HEIGHT, centerX, y, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
                if(texturePath=="buttons/play_button.png") {
                	sceneManager.menuMusic.stop();
                    sceneManager.backgroundMusic.play();
                }
                
            }
        });
    }
    private void nametext(){
        //Create the Name field text box thing below credit

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = game.font;
        style.fontColor = Color.BLACK;

        nameField = new TextField("", style);  // custom style with smaller font
        nameField.setMessageText("Enter your name");
        nameField.setSize(4f, 0.5f);
        nameField.setPosition(centerX-1, 6f);
        stage.addActor(nameField);
        
    }
 

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        viewportManager.draw();
        game.batch.setProjectionMatrix(viewportManager.getFitViewport().getCamera().combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        game.batch.end();

        stage.act(delta);
        stage.draw();

        // Debug music controls
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            sceneManager.menuMusic.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            sceneManager.menuMusic.stop();
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
        backgroundTexture.dispose();
        playButton.dispose();
        creditButton.dispose();
        howToPlayButton.dispose();
    }
}
