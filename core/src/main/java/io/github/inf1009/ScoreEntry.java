package io.github.inf1009;

public class ScoreEntry {
    public String name;
    public int score;

    public ScoreEntry() {
        // Required for LibGDX Json deserialization
    }

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
}