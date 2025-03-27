package io.github.inf1009.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import io.github.inf1009.ScoreEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {
    private static final String PREFS_NAME = "MyGamePrefs";
    private static final String SCORES_KEY = "scoreList";
    private static final int MAX_SCORES = 3;

    private final Preferences prefs;
    private final Json json;

    public ScoreManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        json = new Json();
    }

    /**
     * Adds a new score and saves the updated high score list.
     */
    public void addScore(String name, int score) {
        List<ScoreEntry> scores = getHighScores();
        scores.add(new ScoreEntry(name, score));
        sortDescending(scores);
        trimToMax(scores);
        saveScores(scores);
    }

    /**
     * Returns the current list of high scores (sorted descending).
     */
    public List<ScoreEntry> getHighScores() {
        String data = prefs.getString(SCORES_KEY, "");
        if (data.isEmpty()) return new ArrayList<>();
        return json.fromJson(ArrayList.class, ScoreEntry.class, data);
    }

    /**
     * Clears all saved scores — use with caution (e.g. for testing).
     */
    public void clearScores() {
        prefs.remove(SCORES_KEY);
        prefs.flush();
    }

    // --- Internal Helpers ---

    private void saveScores(List<ScoreEntry> scores) {
        prefs.putString(SCORES_KEY, json.toJson(scores));
        prefs.flush();
    }

    private void sortDescending(List<ScoreEntry> scores) {
        scores.sort(Comparator.comparingInt((ScoreEntry e) -> e.score).reversed());
    }

    private void trimToMax(List<ScoreEntry> scores) {
        if (scores.size() > MAX_SCORES) {
            scores.subList(MAX_SCORES, scores.size()).clear();
        }
    }
}
