package org.hsw.gamestates;

import org.hsw.main.Game;
import org.hsw.managers.GameKeys;
import org.hsw.managers.GameStateManager;
import org.hsw.managers.Save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameOverState extends GameState {

	private SpriteBatch sb;
	private ShapeRenderer sr;

	private boolean newHighScore;
	private char[] newName;
	private int currentChar;

	private BitmapFont titleFont;
	private BitmapFont font;
	private int scalefactor = 2;

	public GameOverState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		newHighScore = Save.gd.isHighscore(Save.gd.getTempscore());
		if (newHighScore) {
			newName = new char[] { 'A', 'A', 'A' };
			currentChar = 0;
		}

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/alienleagueiibold.ttf"));

		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 70;
		titleFont = gen.generateFont(titleParam);
		titleFont.setColor(Color.WHITE);

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 30;
		font = gen.generateFont(param);
		font.setColor(Color.WHITE);
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		sb.begin();

		String s = "Game Over";
		float w = titleFont.getBounds(s).width;
		titleFont.draw(sb, s, (Game.WIDTH - w) / 2, 370 * scalefactor);
		if (!newHighScore) {
			sb.end();
			return;
		}

		s = "New highscore: " + Save.gd.getTempscore();
		w = font.getBounds(s).width;
		font.draw(sb, s, (Game.WIDTH - w) / 2, 200 * scalefactor);
		for (int i = 0; i < newName.length; i++) {
			font.draw(sb, Character.toString(newName[i]), 230 * scalefactor
					+ 20 * i, 120 * scalefactor);
		}
		sb.end();

		sr.begin(ShapeType.Line);
		sr.line(460 + 20 * currentChar, 210, 470 + 20 * currentChar, 210);
		sr.end();
	}

	@Override
	public void handleInput() {
		if (GameKeys.isPressed(GameKeys.ENTER)) {
			if (newHighScore) {
				Save.gd.addHighscore(Save.gd.getTempscore(),
						new String(newName));
				Save.save();
			}
			gsm.setState(GameStateManager.MENU);
		}
		if (GameKeys.isPressed(GameKeys.UP)) {
			if (newName[currentChar] == ' ') {
				newName[currentChar] = 'Z';
			} else {
				newName[currentChar]--;
				if (newName[currentChar] < 'A') {
					newName[currentChar] = ' ';
				}
			}
		}

		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (newName[currentChar] == ' ') {
				newName[currentChar] = 'A';
			} else {
				newName[currentChar]++;
				if (newName[currentChar] > 'Z') {
					newName[currentChar] = ' ';
				}
			}

		}
		if (GameKeys.isPressed(GameKeys.RIGHT)) {
			if (currentChar < newName.length - 1) {
				currentChar++;
			}
		}

		if (GameKeys.isPressed(GameKeys.LEFT)) {
			if (currentChar > 0) {
				currentChar--;
			}
		}

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
