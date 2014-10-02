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

public class HighScoreState extends GameState {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private long[] highscores;
	private String[] names;

	public HighScoreState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/alienleagueiibold.ttf"));

		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 90;
		titleFont = gen.generateFont(titleParam);
		titleFont.setColor(Color.WHITE);

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 50;
		font = gen.generateFont(param);
		font.setColor(Color.WHITE);
		Save.load();
		highscores = Save.gd.getHighscores();
		names = Save.gd.getNames();

	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		sb.begin();

		String s = "Highscores";
		float w = titleFont.getBounds(s).width;
		titleFont.draw(sb, s, (Game.WIDTH - w) / 2, 730);
		for (int i = 0; i < highscores.length; i++) {
			s = String.format("%2d.\t%7s\t%7s", i + 1, highscores[i], names[i]);
			w = font.getBounds(s).width;
			font.draw(sb, s, (Game.WIDTH - w) / 2, 630 - 45 * i);

		}

		sb.end();
	}

	@Override
	public void handleInput() {
		if (GameKeys.isPressed(GameKeys.ENTER)
				|| GameKeys.isPressed(GameKeys.ESCAPE)) {
			gsm.setState(GameStateManager.MENU);
		}
	}

	@Override
	public void dispose() {

	}

}
