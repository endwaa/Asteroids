package org.hsw.entities;

import org.hsw.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleAsteroidBoom extends SpaceObject {

	private float dtCum;
	private boolean started = false;

	public ParticleEffect particleEffect;

	public ParticleAsteroidBoom(float x, float y) {
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("effects/asteroidboom.p"),
				Gdx.files.internal("effects"));

		particleEffect.setPosition(Game.WIDTH / 2, Game.HEIGHT / 2);
		particleEffect.setPosition(x, y);

	}

	public boolean shouldRemove() {
		return particleEffect.isComplete() ? true : false;
	}

	public void update(float dt) {
		this.dtCum = dt;
	}

	public void draw(SpriteBatch sb) {
		sb.begin();
		particleEffect.draw(sb, dtCum);
		sb.end();
		start();
	}

	public void start() {
		if (!started) {
			particleEffect.start();
			started = true;
		}
	}
}
