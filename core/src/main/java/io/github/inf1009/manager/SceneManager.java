package io.github.inf1009.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import io.github.inf1009.screen.GameScreen;
import io.github.inf1009.screen.MainMenuScreen;

public class SceneManager {
    private  Game game;
    private Screen currentScreen;
    public Music backgroundMusic;
    public Music menuMusic;

    public SceneManager(Game game) {
        this.game = game;
    }

    public void setScreen(Screen newScreen) {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }

        if (backgroundMusic == null) {
        	menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Normaltune.mp3"));
        	menuMusic.setLooping(true);
        	menuMusic.setVolume(0.03f);

            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Hypetune.mp3"));
        	backgroundMusic.setLooping(true);
        	backgroundMusic.setVolume(0.03f);
        }

        currentScreen = newScreen;
        if (currentScreen instanceof MainMenuScreen) {
            menuMusic.play();
        }
        if (currentScreen instanceof GameScreen) {
            backgroundMusic.play();
        }

        game.setScreen(currentScreen);
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        backgroundMusic.dispose();
        menuMusic.dispose();
    }
}
