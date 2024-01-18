

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    private final String nickname;
    private int bestScore;
    private int currentScore;
    private final Map<String, Integer> bestScoresByModeAndDifficulty;
    public Player(String nickname) {
        this.nickname = nickname;
        this.bestScore = 0;
        this.bestScoresByModeAndDifficulty = new HashMap<>();
    }

    public String getNickname() {
        return nickname;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
    public int getCurrentScore() {
        return currentScore;
    }


    public void setBestScoreForModeAndDifficulty(String mode, String difficulty, int score) {
        bestScoresByModeAndDifficulty.put(mode + "_" + difficulty, score);
    }

    public int getBestScoreForModeAndDifficulty(String mode, String difficulty) {
        return bestScoresByModeAndDifficulty.getOrDefault(mode + "_" + difficulty, Integer.MAX_VALUE);
    }

    public Map<String, Integer> getBestScoresByModeAndDifficulty() {
        return bestScoresByModeAndDifficulty;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
}
