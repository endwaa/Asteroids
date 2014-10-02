package org.hsw.main;

import org.hsw.managers.GameInputProcessor;
import org.hsw.managers.GameKeys;
import org.hsw.managers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Game implements ApplicationListener {

	public static int WIDTH;
	public static int HEIGHT;

	public static OrthographicCamera cam;

	private GameStateManager gsm;

	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH / 2, HEIGHT / 2);
		cam.update();

		Gdx.input.setInputProcessor(new GameInputProcessor());

		gsm = new GameStateManager();
	}

	@Override
	public void render() {
		// Clear screen to black every frame, set higher for weird effects
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		// fps.log();
		GameKeys.update();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
