package org.hsw.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends SpaceObject {
	private int type;
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;

	private int score;

	private int numPoints;
	private float[] dists;

	private boolean remove;

	public Asteroid(float x, float y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;

		if (type == SMALL) {
			numPoints = 8;
			width = height = 24;
			speed = MathUtils.random(70, 80);
			score = 100;
		} else if (type == MEDIUM) {
			numPoints = 10;
			width = height = 40;
			speed = MathUtils.random(50, 60);
			score = 50;
		} else if (type == LARGE) {
			numPoints = 12;
			width = height = 80;
			speed = MathUtils.random(20, 30);
			score = 20;
		}

		rotationSpeed = MathUtils.random(-2, 2);

		int cwOrCcw = MathUtils.randomSign();

		radians = MathUtils.random(cwOrCcw * (float) (2 * Math.PI));

		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;

		shapex = new float[numPoints];
		shapey = new float[numPoints];
		dists = new float[numPoints];
		int radius = width / 2;
		for (int i = 0; i < numPoints; i++) {
			dists[i] = MathUtils.random(radius / 2, radius);
		}

		setShape();

	}

	private void setShape() {
		float angle = 0;
		for (int i = 0; i < numPoints; i++) {
			shapex[i] = x + MathUtils.cos(angle + radians) * dists[i];
			shapey[i] = y + MathUtils.sin(angle + radians) * dists[i];
			angle += 2 * (float) Math.PI / numPoints;
		}
	}

	public int getType() {
		return type;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public int getScore() {
		return score;
	}

	public void update(float dt) {
		x += dx * dt;
		y += dy * dt;

		radians += rotationSpeed * dt;

		setShape();

		wrap();
	}

	public void draw(ShapeRenderer sr) {
		sr.setColor(Color.CYAN);

		sr.begin(ShapeType.Line);
		for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++) {
			sr.rectLine(shapex[i], shapey[i], shapex[j], shapey[j], 1);
		}
		sr.end();
	}

}
