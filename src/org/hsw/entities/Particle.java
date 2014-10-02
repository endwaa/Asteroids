package org.hsw.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Particle extends SpaceObject {
	private float timer;
	private float time;
	private boolean remove;

	public Particle(float x, float y) {
		this.x = x;
		this.y = y;
		width = height = 3;
		speed = 700;
		radians = MathUtils.random((float) (2 * Math.PI));
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;

		timer = 0;
		time = 3;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void draw(ShapeRenderer sr) {
		sr.setColor(1, 1, 1, 1);
		sr.begin(ShapeType.Line);
		sr.circle(x - width / 2, y - width / 2, width / 2);
		sr.end();
	}

	public void update(float dt) {
		x += dx * dt;
		y += dy * dt;

		timer += dt;
		if (timer > time) {
			remove = true;
		}
	}
}
