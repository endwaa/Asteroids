package org.hsw.entities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.hsw.main.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Player extends SpaceObject {

	private final int MAX_BULLETS = 5;
	private ArrayList<Bullet> bullets;

	private float[] flamey;
	private float[] flamex;

	private boolean left;
	private boolean right;
	private boolean up;

	private float maxSpeed = 300;
	private float acceleration = 200;
	private float deceleration = 10;
	private float acceleratingTimer;

	private boolean hit = false;
	private boolean dead;

	private float hitTimer = 0;
	private float hitTime = 2;
	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinesVector;

	private long score = 0;
	private int extraLives = 3;
	private long requiredScore = 10000;
	@SuppressWarnings("unused")
	private SpriteBatch sb;
	private int scalefactor = 2;

	public Player(ArrayList<Bullet> bullets) {
		sb = new SpriteBatch();
		this.bullets = bullets;

		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;

		shapex = new float[4];
		shapey = new float[4];
		flamex = new float[3];
		flamey = new float[3];

		radians = (float) Math.PI / 2;
		rotationSpeed = 3;

		// rocket = new ParticleRocketMotor(x, y, 1);
	}

	private void setShape() {
		shapex[0] = x + MathUtils.cos(radians) * 8 * scalefactor;
		shapey[0] = y + MathUtils.sin(radians) * 8 * scalefactor;

		shapex[1] = x + MathUtils.cos(radians - 4 * (float) Math.PI / 5) * 8
				* scalefactor;
		shapey[1] = y + MathUtils.sin(radians - 4 * (float) Math.PI / 5) * 8
				* scalefactor;

		shapex[2] = x + MathUtils.cos(radians + (float) Math.PI) * 3
				* scalefactor;
		shapey[2] = y + MathUtils.sin(radians + (float) Math.PI) * 3
				* scalefactor;

		shapex[3] = x + MathUtils.cos(radians + 4 * (float) Math.PI / 5) * 8
				* scalefactor;
		shapey[3] = y + MathUtils.sin(radians + 4 * (float) Math.PI / 5) * 8
				* scalefactor;
	}

	private void setFlame() {
		flamex[0] = x + MathUtils.cos(radians - 5 * (float) Math.PI / 6) * 5
				* scalefactor;
		flamey[0] = y + MathUtils.sin(radians - 5 * (float) Math.PI / 6) * 5
				* scalefactor;

		flamex[1] = x + MathUtils.cos(radians - (float) Math.PI)
				* (10 + acceleratingTimer * 50) * scalefactor;
		flamey[1] = y + MathUtils.sin(radians - (float) Math.PI)
				* (10 + acceleratingTimer * 50) * scalefactor;

		flamex[2] = x + MathUtils.cos(radians + 5 * (float) Math.PI / 6) * 5
				* scalefactor;
		flamey[2] = y + MathUtils.sin(radians + 5 * (float) Math.PI / 6) * 5
				* scalefactor;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void shoot() {
		if (bullets.size() >= MAX_BULLETS) {
			return;
		}

		// standard
		standard();

		// backfire
		// backfire();

		// Fan out
		// fanout();

		// circle
		// circle();

	}

	@SuppressWarnings("unused")
	private void circle() {

		float step = 0;

		for (int i = 0; i < 100; i++) {
			decrementScoreByOne();
			bullets.add(new Bullet(x, y, radians + step));
			step += (2 * MathUtils.PI) / 100;
		}

	}

	private void standard() {
		decrementScoreByOne();
		bullets.add(new Bullet(x, y, radians));
	}

	@SuppressWarnings("unused")
	private void fanout() {
		for (int i = 0; i < MAX_BULLETS; i++) {
			decrementScoreByOne();
			bullets.add(new Bullet(x, y, radians + 100 * i + 1));
		}

	}

	@SuppressWarnings("unused")
	private void backfire() {
		decrementScoreByOne();
		decrementScoreByOne();
		bullets.add(new Bullet(x, y, radians));
		bullets.add(new Bullet(x, y, radians - MathUtils.PI));

	}

	public long getScore() {
		return this.score;
	}

	public int getExtraLives() {
		return extraLives;
	}

	public void loseLife() {
		extraLives--;
	}

	public void incrementScore(long points) {
		score += points;
	}

	public void decrementScoreByOne() {
		if (score > 0) {
			score--;
		}
	}

	public void hit() {
		if (hit) {
			return;
		}

		hit = true;
		dx = dy = 0;
		left = right = up = false;
		hitLines = new Line2D.Float[4];

		for (int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++) {
			hitLines[i] = new Line2D.Float(shapex[i], shapey[i], shapex[j],
					shapey[j]);
		}

		hitLinesVector = new Point2D.Float[4];
		hitLinesVector[0] = new Point2D.Float(MathUtils.cos(radians + 1.5f),
				MathUtils.cos(radians + 1.5f));
		hitLinesVector[1] = new Point2D.Float(MathUtils.cos(radians - 1.5f),
				MathUtils.sin(radians - 1.5f));
		hitLinesVector[2] = new Point2D.Float(MathUtils.cos(radians - 2.8f),
				MathUtils.sin(radians - 2.8f));
		hitLinesVector[3] = new Point2D.Float(MathUtils.cos(radians + 2.8f),
				MathUtils.sin(radians + 2.8f));
	}

	public void update(float dt) {
		// rocket.update(dt);
		// check if hit
		if (hit) {
			hitTimer += dt;
			if (hitTimer > hitTime) {
				dead = true;
				hitTimer = 0;
			}

			for (int i = 0; i < hitLines.length; i++) {
				hitLines[i].setLine(hitLines[i].x1 + hitLinesVector[i].x * 10
						* dt, hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
						hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
						hitLines[i].y2 + hitLinesVector[i].y * 10 * dt);
			}
			return;
		}

		// Check extra lives

		if (score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
		}

		// turning
		if (left) {
			radians += rotationSpeed * dt;
		} else if (right) {
			radians -= rotationSpeed * dt;
		}

		// accelerating
		if (up) {
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
			acceleratingTimer += dt;
			if (acceleratingTimer > 0.1f) {
				acceleratingTimer = 0;
			}
		} else {
			acceleratingTimer = 0;
		}

		// deceleration
		float vec = (float) Math.sqrt(dx * dx + dy * dy);
		if (vec > 0) {
			dx -= (dx / vec) * deceleration * dt;
			dy -= (dy / vec) * deceleration * dt;
		}
		if (vec > maxSpeed) {
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}

		// set position
		x += dx * dt;
		y += dy * dt;
		// set shape
		setShape();

		// set flame
		if (up) {
			setFlame();
		}

		// rocket.setPosition(x, y, radians);
		// screen wrap
		wrap();

	}

	public void draw(ShapeRenderer sr) {

		sr.setColor(1, 1, 1, 1);

		sr.begin(ShapeType.Line);

		// check hit and draw "exploding" lines

		if (hit) {
			sr.setColor(Color.ORANGE);
			for (int i = 0; i < hitLines.length; i++) {
				sr.rectLine(hitLines[i].x1, hitLines[i].y1, hitLines[i].x2,
						hitLines[i].y2, 1);
			}
			sr.end();
			return;
		}

		// draw ship
		for (int i = 0, j = shapex.length - 1; i < shapex.length; j = i++) {
			sr.rectLine(shapex[i], shapey[i], shapex[j], shapey[j], 1);
		}

		// draw flames
		if (up) {
			sr.setColor(Color.ORANGE);
			for (int i = 0, j = flamex.length - 1; i < flamex.length; j = i++) {
				sr.rectLine(flamex[i], flamey[i], flamex[j], flamey[j], 1);
			}
		}

		// Draw rocket motor
		// rocket.draw(sb);
		// rocket.start();

		sr.end();
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		setShape();
	}

	public void reset() {
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
		setShape();
		hit = dead = false;
	}
}
