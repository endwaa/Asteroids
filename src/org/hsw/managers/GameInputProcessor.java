package org.hsw.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter{

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.UP) {
			GameKeys.setKey(GameKeys.UP, true);
		}
		if (keycode == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, true);
		}
		if (keycode == Keys.LEFT) {
			GameKeys.setKey(GameKeys.LEFT, true);
		}
		if (keycode == Keys.RIGHT) {
			GameKeys.setKey(GameKeys.RIGHT, true);
		}
		if (keycode == Keys.ENTER) {
			GameKeys.setKey(GameKeys.ENTER, true);
		}
		if (keycode == Keys.ESCAPE) {
			GameKeys.setKey(GameKeys.ESCAPE, true);
		}
		if (keycode == Keys.SPACE) {
			GameKeys.setKey(GameKeys.SPACE, true);
		}
		if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
			GameKeys.setKey(GameKeys.SHIFT, true);
		}
		

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.UP) {
			GameKeys.setKey(GameKeys.UP, false);
		}
		if (keycode == Keys.DOWN) {
			GameKeys.setKey(GameKeys.DOWN, false);
		}
		if (keycode == Keys.LEFT) {
			GameKeys.setKey(GameKeys.LEFT, false);
		}
		if (keycode == Keys.RIGHT) {
			GameKeys.setKey(GameKeys.RIGHT, false);
		}
		if (keycode == Keys.ENTER) {
			GameKeys.setKey(GameKeys.ENTER, false);
		}
		if (keycode == Keys.ESCAPE) {
			GameKeys.setKey(GameKeys.ESCAPE, false);
		}
		if (keycode == Keys.SPACE) {
			GameKeys.setKey(GameKeys.SPACE, false);
		}
		if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
			GameKeys.setKey(GameKeys.SHIFT, false);
		}
		return true;
	}

	
}
