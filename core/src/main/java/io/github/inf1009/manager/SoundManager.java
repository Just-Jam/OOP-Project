package io.github.inf1009.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    // Declare the sound effects
    private Sound rotateSound;
    private Sound placeBlockSound;
    private Sound gameOverSound;
    private Sound crushSound;
    private Sound burningSound;

    public SoundManager() {
        // Load the sound effects from the assets folder
        rotateSound = Gdx.audio.newSound(Gdx.files.internal("audio/rotating.mp3"));
        placeBlockSound = Gdx.audio.newSound(Gdx.files.internal("audio/place.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_over.mp3"));
        crushSound = Gdx.audio.newSound(Gdx.files.internal("audio/crush.mp3"));
        burningSound = Gdx.audio.newSound(Gdx.files.internal("audio/burning.mp3"));
    }

    // Method to play the rotation sound
    public void playRotateSound() {
        rotateSound.play(1.0f);  // You can adjust the volume if needed (1.0f is default)
    }

    // Method to play the block placement sound
    public void playPlaceBlockSound() {
        placeBlockSound.play(0.5f);
    }
    
    public void playGameOverSound() {
        gameOverSound.play(1.0f);
    }
    
    public void playCrushSound() {
        crushSound.play(1.0f);
    }
    
    public void playBurningSound() {
        burningSound.play(1.0f);
    }
    

    // Dispose of the sounds to release resources when no longer needed
    public void dispose() {
        rotateSound.dispose();
        placeBlockSound.dispose();
        gameOverSound.dispose();
        crushSound.dispose();
        burningSound.dispose();
    }
}
