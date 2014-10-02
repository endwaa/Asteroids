package org.hsw.managers;

import java.io.Serializable;

public class GameData implements Serializable {

	private static final long serialVersionUID = 8627081855437710982L;

	private final int MAX_SCORES = 10;
	private long[] highscores;
	private String[] names;

	private long temp;

	public GameData() {
		highscores = new long[MAX_SCORES];
		names = new String[MAX_SCORES];
	}

	public void init() {
		for (int i = 0; i < MAX_SCORES; i++) {
			highscores[i] = 0;
			names[i] = "---";
		}
	}

	public long[] getHighscores() {
		return highscores;
	}

	public String[] getNames() {
		return names;
	}

	public long getTempscore() {
		return temp;
	}

	public void setTempscore(long temp) {
		this.temp = temp;
	}

	public boolean isHighscore(long score) {
		return score > highscores[MAX_SCORES - 1];
	}

	public void addHighscore(long newScore, String name) {
		if (isHighscore(newScore)) {
			highscores[MAX_SCORES - 1] = newScore;
			names[MAX_SCORES - 1] = name;
			sortHighScores();
		}
	}

	private void sortHighScores() {
		for (int i = 0; i < MAX_SCORES; i++) {
			long score = highscores[i];
			String name = names[i];
			int j;
			for (j = i - 1; j >= 0 && highscores[j] < score; j--) {
				highscores[j + 1] = highscores[j];
				names[j + 1] = names[j];
			}
			highscores[j + 1] = score;
			names[j + 1] = name;
		}
	}
}
