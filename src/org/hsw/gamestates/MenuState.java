package org.hsw.gamestates;

import org.hsw.main.Game;
import org.hsw.managers.GameKeys;
import org.hsw.managers.GameStateManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MenuState extends PlayState {

	private SpriteBatch sb;

	private BitmapFont titleFont;
	private BitmapFont font;

	private String title = "HiST-Droids";
	private int currentItem;
	private String[] menuItems;
	private int scalefactor = 2;

	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/alienleagueiibold.ttf"));

		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 80;
		titleFont = gen.generateFont(titleParam);
		titleFont.setColor(Color.WHITE);

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 50;
		font = gen.generateFont(param);

		menuItems = new String[] { "Play", "Highscores", "Quit" };

	}

	@Override
	public void update(float dt) {
		handleInput();

	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		sb.begin();
		// draw title
		float width = titleFont.getBounds(title).width;
		titleFont.draw(sb, title, (Game.WIDTH - width) / 2, 350 * scalefactor);
		// draw menu
		for (int i = 0; i < menuItems.length; i++) {
			width = font.getBounds(menuItems[i]).width;
			if (currentItem == i) {
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			font.draw(sb, menuItems[i], (Game.WIDTH - width) / 2, 250
					* scalefactor - 60 * i * scalefactor);
		}

		sb.end();
	}

	@Override
	public void handleInput() {
		if (GameKeys.isPressed(GameKeys.UP)) {
			if (currentItem > 0) {
				currentItem--;
			}
		}

		if (GameKeys.isPressed(GameKeys.DOWN)) {
			if (currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}

		if (GameKeys.isPressed(GameKeys.ENTER)
				|| GameKeys.isPressed(GameKeys.SPACE)) {
			select();
		}

	}

	private void select() {
		if (currentItem == 0) {
			gsm.setState(GameStateManager.PLAY);
		} else if (currentItem == 1) {
			gsm.setState(GameStateManager.HIGHSCORE);
		} else if (currentItem == 2) {
			Gdx.app.exit();
		}
	}

	@Override
	public void dispose() {
	}

}
