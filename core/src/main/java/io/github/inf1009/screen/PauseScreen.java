package io.github.inf1009.screen;

import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.Screen; 
import com.badlogic.gdx.graphics.Color; 
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.scenes.scene2d.Stage; 
import com.badlogic.gdx.scenes.scene2d.InputEvent; 
import com.badlogic.gdx.utils.ScreenUtils; 
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener; 
import io.github.inf1009.Tetris; 
import io.github.inf1009.TextureButton;
import io.github.inf1009.manager.InputManager;
import io.github.inf1009.manager.SceneManager;

public class PauseScreen implements Screen {
	private final Tetris game;
	private final SceneManager sceneManager;
	private final Stage stage;
	private final Texture backgroundTexture;
	private final TextureButton backButton;
	private final TextureButton quitButton;
	private final Screen previousScreen;
	private final InputManager inputManager;

	public PauseScreen(final Tetris game, Screen previousScreen, InputManager inputManager) {
	    this.game = game;
	    this.previousScreen = previousScreen;
	    this.inputManager = inputManager;
	    this.sceneManager = game.sceneManager;
	    
	    // Use the same viewport as other screens
	    stage = new Stage(game.viewportManager.getFitViewport());
	    Gdx.input.setInputProcessor(stage);
	    
	    // Load the pause background texture
	    backgroundTexture = new Texture("game_pause.png");
	    
	    // Use the same world dimensions as MainMenuScreen
	    int worldWidth = game.GRID_COLUMNS;
	    int worldHeight = game.GRID_ROWS;
	    
	    // Create the "Back" button to resume the game
	    backButton = new TextureButton("buttons/back_button.png", 4, 1, (float) worldWidth / 2, 4f, new ClickListener() {
	        @Override
	        public void clicked(InputEvent event, float x, float y) {
	            // Resume game by switching back to the previous (game) screen
	        	sceneManager.popScreen();
	        	inputManager.gamepause = false;
	        }
	    });
	    
	    // Create the "Quit" button to return to the Main Menu
	    quitButton = new TextureButton("buttons/quit_button.png", 4, 1, (float) worldWidth / 2, 3f, new ClickListener() {
	        @Override
	        public void clicked(InputEvent event, float x, float y) {
	            // Return to main menu
	            sceneManager.setScreen(new MainMenuScreen(game));
	        }
	    });
	    
	    stage.addActor(backButton.getButton());
	    stage.addActor(quitButton.getButton());
	}

//	public PauseScreen(Tetris game2, GameScreen previousScreen, InputManager inputManager) {
//	    // TODO Auto-generated constructor stub
//	}


	@Override
	public void render(float delta) {
	    ScreenUtils.clear(Color.BLACK);
	    game.batch.setProjectionMatrix(game.viewportManager.getFitViewport().getCamera().combined);
	    game.batch.begin();
	    game.batch.draw(backgroundTexture, 0, 0, game.GRID_COLUMNS, game.GRID_ROWS);
	    ScreenUtils.clear(Color.BLACK);
	    game.batch.end();
	    
	    stage.act(delta);
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    game.viewportManager.resize(width, height);
	    stage.getViewport().update(width, height, true);
	}

	@Override public void show() { 
		sceneManager.backgroundMusic.pause();
	}
	@Override public void hide() { }
	@Override public void pause() { }
	@Override public void resume() { 
		sceneManager.backgroundMusic.play();
	}

	@Override
	public void dispose() {
	    stage.dispose();
	    backgroundTexture.dispose();
	}

}