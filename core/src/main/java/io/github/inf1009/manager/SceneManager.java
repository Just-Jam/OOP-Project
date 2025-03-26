package io.github.inf1009.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import java.util.Stack;

public class SceneManager {
    private  Game game;
    private Screen currentScreen;
    public Music backgroundMusic;
    public Music menuMusic;
    private Stack<Screen> screenStack = new Stack<>();

    public SceneManager(Game game) {
        this.game = game;
        
     // Load background and menu music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("HypeTune.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("NormalTune.mp3"));

        backgroundMusic.setLooping(true);
        menuMusic.setLooping(true);
    }

    // Push a new screen on top without disposing the current one
    public void pushScreen(Screen newScreen) {
        if (currentScreen != null) {
            screenStack.push(currentScreen);
        }
        currentScreen = newScreen;
        game.setScreen(currentScreen);
    }

    // Pop the current screen and resume the previous one
    public void popScreen() {
        if (!screenStack.isEmpty()) {
            currentScreen.hide();
            currentScreen.dispose();
            currentScreen = screenStack.pop();
            game.setScreen(currentScreen);
        }
    }

    // Use this method for transitions that require replacing everything
    public void setScreen(Screen newScreen) {
        while (!screenStack.isEmpty()) {
            Screen s = screenStack.pop();
            s.hide();
            s.dispose();
        }
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }
        currentScreen = newScreen;
        game.setScreen(currentScreen);
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
