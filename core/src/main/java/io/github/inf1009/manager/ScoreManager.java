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
    private static final int MAX_SCORES = 4;

    private Preferences prefs;
    private Json json;

    public ScoreManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        json = new Json();
    }

    public void addScore(String name, int score) {
        List<ScoreEntry> scores = getHighScores();
        scores.add(new ScoreEntry(name, score));

        // Sort descending by score
        scores.sort(Comparator.comparingInt(e -> -e.score));

        // Keep only top 5
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        // Save back to Preferences
        String jsonData = json.toJson(scores);
        prefs.putString(SCORES_KEY, jsonData);
        prefs.flush();
    }

    public List<ScoreEntry> getHighScores() {
        String data = prefs.getString(SCORES_KEY, "");
        if (data.isEmpty()) return new ArrayList<>();
        return json.fromJson(ArrayList.class, ScoreEntry.class, data);
    }
    public void clearScores() {
        prefs.remove(SCORES_KEY);
        prefs.flush();
    }
}