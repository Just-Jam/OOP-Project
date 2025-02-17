package io.github.inf1009.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class SceneManager {
    private  Game game;
    private Screen currentScreen;

    public SceneManager(Game game) {
        this.game = game;
    }

    public void setScreen(Screen newScreen) {
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
