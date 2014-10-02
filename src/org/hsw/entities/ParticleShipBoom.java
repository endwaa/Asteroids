package org.hsw.entities;

import org.hsw.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleShipBoom extends SpaceObject {

	private float dtCum;
	private boolean started = false;

	public ParticleEffect particleEffect;

	public ParticleShipBoom(float x, float y) {
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("effects/spaceshipboom.p"),
				Gdx.files.internal("effects"));

		particleEffect.setPosition(Game.WIDTH / 2, Game.HEIGHT / 2);
		particleEffect.setPosition(150, 150);

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
	}

	public void start() {
		if (!started) {
			particleEffect.start();
			started = true;
		}
	}

	public void reset() {
		particleEffect.reset();
	}

}
