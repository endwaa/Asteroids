package org.hsw.gamestates;

import java.util.ArrayList;

import org.hsw.entities.Asteroid;
import org.hsw.entities.Bullet;
import org.hsw.entities.Particle;
import org.hsw.entities.ParticleAsteroidBoom;
import org.hsw.entities.ParticleShipBoom;
import org.hsw.entities.Player;
import org.hsw.main.Game;
import org.hsw.managers.GameKeys;
import org.hsw.managers.GameStateManager;
import org.hsw.managers.Save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class PlayState extends GameState {

	private ShapeRenderer sr;
	private SpriteBatch sb;
	private BitmapFont font;

	private Player hudPlayer;
	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Particle> particles;
	private ArrayList<ParticleAsteroidBoom> booms;
	private ArrayList<ParticleShipBoom> shipbooms;

	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	private int scalefactor = 2;
	private boolean DEBUG = true;

	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/alienleagueiibold.ttf"));

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 40;
		font = gen.generateFont(param);

		bullets = new ArrayList<Bullet>();

		player = new Player(bullets);

		particles = new ArrayList<Particle>();

		booms = new ArrayList<ParticleAsteroidBoom>();
		shipbooms = new ArrayList<ParticleShipBoom>();

		asteroids = new ArrayList<Asteroid>();
		asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
		asteroids.add(new Asteroid(200, 100, Asteroid.MEDIUM));
		asteroids.add(new Asteroid(300, 100, Asteroid.SMALL));

		level = 1;

		spawnAsteroids();
		hudPlayer = new Player(null);

	}

	private void createParticles(float x, float y) {
		int numberOfPArticles = MathUtils.random(3, 8);
		for (int i = 0; i < numberOfPArticles; i++) {
			particles.add(new Particle(x, y));
		}
	}

	private void splitAsteroid(Asteroid a) {

		createParticles(a.getx(), a.gety());

		setNumAsteroidsLeft(getNumAsteroidsLeft() - 1);
		if (a.getType() == Asteroid.LARGE) {
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
		}
		if (a.getType() == Asteroid.MEDIUM) {
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
		}
	}

	private void spawnAsteroids() {
		asteroids.clear();
		System.out.println("Level: " + level);
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		setNumAsteroidsLeft(totalAsteroids);
		for (int i = 0; i < numToSpawn; i++) {
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);

			float dx = x - player.getx();
			float dy = y - player.gety();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);

			while (dist < 100) {
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				dx = x - player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt(dx * dx + dy * dy);

			}
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}
	}

	@Override
	public void update(float dt) {
		// get user input
		handleInput();

		// spawn next level
		if (asteroids.size() == 0) {
			level++;
			spawnAsteroids();
		}

		// update player
		player.update(dt);
		if (player.isDead()) {
			player.loseLife();
			if (player.getExtraLives() >= 0) {
				player.reset();
			} else {
				Save.load();
				Save.gd.setTempscore(player.getScore());
				gsm.setState(GameStateManager.GAMEOVER);
				return;
			}
			return;
		}

		// update bullets
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update(dt);
			if (bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}

		// update asteroids
		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
			if (asteroids.get(i).shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
		}

		// update particles
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update(dt);
			if (particles.get(i).shouldRemove()) {
				particles.remove(i);
				i--;
			}
		}

		// update booms
		for (int i = 0; i < booms.size(); i++) {
			booms.get(i).update(dt);
			if (booms.get(i).shouldRemove()) {
				booms.remove(i);
				i--;
			}
		}

		// update shipbooms
		for (int i = 0; i < shipbooms.size(); i++) {
			shipbooms.get(i).update(dt);
			if (shipbooms.get(i).shouldRemove()) {
				shipbooms.remove(i);
				i--;
			}
		}

		// check collision
		checkCollisions();

	}

	private void checkCollisions() {
		// player asteroid
		for (int i = 0; i < asteroids.size(); i++) {
			Asteroid a = asteroids.get(i);
			if (a.intersects(player)) {
				player.hit();
				if (player.getExtraLives() > 1) {
					asteroids.remove(i);
					splitAsteroid(a);
					i--;
					// player.boom(a.getx(), a.gety());
					// shipbooms.add(new ParticleShipBoom(a.getx(), a.gety()));
				}
			}
		}
		// bullet asteroid
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			for (int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				if (a.contains(b.getx(), b.gety())) {
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroid(a);
					player.incrementScore(a.getScore());
					booms.add(new ParticleAsteroidBoom(a.getx(), a.gety()));
					break;

				}
			}
		}
	}

	@Override
	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		// Draw player
		player.draw(sr);
		// Draw bullets
		for (Bullet bullet : bullets) {
			bullet.draw(sr);
		}

		// Draw asteroids
		for (Asteroid asteroid : asteroids) {
			asteroid.draw(sr);
		}

		// Draw particles
		for (Particle particle : particles) {
			particle.draw(sr);
		}

		// Draw booms
		for (ParticleAsteroidBoom boom : booms) {
			boom.draw(sb);
		}

		// Draw shipbooms
		for (ParticleShipBoom boom : shipbooms) {
			boom.draw(sb);
		}

		// Draw score

		sb.setColor(1, 1, 1, 1);
		sb.begin();
		font.draw(sb, "Score: " + Long.toString(player.getScore()), 10,
				Game.HEIGHT - 10);
		font.draw(sb, "Level: " + level, Game.WIDTH - 110, Game.HEIGHT - 10);

		// Draw fps if in debug mode
		if (DEBUG) {
			font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(),
					Game.WIDTH - 120, 40);
		}

		sb.end();

		// Draw lives
		for (int i = 0; i < player.getExtraLives(); i++) {
			hudPlayer.setPosition(20 + i * 12 * scalefactor, 20);
			hudPlayer.draw(sr);
		}
	}

	@Override
	public void handleInput() {

		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));

		if (GameKeys.isPressed(GameKeys.SPACE) && !player.isDead()) {
			player.shoot();
		}
		if (GameKeys.isPressed(GameKeys.ESCAPE)) {
			gsm.setState(GameStateManager.MENU);
		}

	}

	@Override
	public void dispose() {
	}

	public int getNumAsteroidsLeft() {
		return numAsteroidsLeft;
	}

	public void setNumAsteroidsLeft(int numAsteroidsLeft) {
		this.numAsteroidsLeft = numAsteroidsLeft;
	}

}
