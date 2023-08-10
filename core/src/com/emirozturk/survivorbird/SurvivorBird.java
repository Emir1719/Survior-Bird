package com.emirozturk.survivorbird;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, bird, enemy1, enemy2, enemy3;
	float birdX = 0, birdY = 0, velocity = 0, heroWidth, heroHeight, distance;
	int width, height, score=0, scoredEnemy = 0;
	byte numberOfEnemies = 4, enemyVelocity = 9;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffset1 = new float[numberOfEnemies];
	float[] enemyOffset2 = new float[numberOfEnemies];
	float[] enemyOffset3 = new float[numberOfEnemies];
	Random random;
	Circle birdCircle;
	Circle[] enemyCircle1, enemyCircle2, enemyCircle3;
	BitmapFont scoreFont, gameOverFont;
	GameState gameState = GameState.restart;
	boolean loadingFinished;
	AssetManager assetManager;
	String[] backgrounds = {"background1.png", "background2.png","background3.png","background4.png","background5.png","background6.png"};

	enum GameState {start, restart, end}

	//Oyun açıldığında olacak olaylar.
	@Override public void create() {
		loadingFinished = false;
		assetManager = new AssetManager();
		for (String item : backgrounds) {
			assetManager.load(item, Texture.class);
		}

		batch = new SpriteBatch();
		bird = new Texture("bird1.png");
		enemy1 = new Texture("enemy1.png");
		enemy2 = new Texture("enemy1.png");
		enemy3 = new Texture("enemy1.png");

		//Ekran genişliği ve yüksekliği alınır.
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		//Ekranın yarısı kadar uzaklı olsun kuş ve düşman arasında.
		distance = Gdx.graphics.getWidth() / 2.0f;

		//Tüm karakterlerin boyutları belirlenir.
		heroWidth = width / 12.0f;
		heroHeight = height / 10.0f;

		birdCircle = new Circle();
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(7);
		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.RED);
		gameOverFont.getData().setScale(8);
		setFirstValue();

		assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
		assetManager.finishLoading(); // This blocks until all assets are loaded
		loadingFinished = true;
	}

	//Oyun devam ettiği sürece devamlı çağırılan bir metod.
	@Override public void render() {
		if (!loadingFinished) {
			// Render a loading screen, progress bar, etc.
			// Optionally, you can update a progress bar based on assetManager.getProgress()
			return;
		}
		batch.begin();
		//Arka planı ve kuşu ekrana çizer.
		chanceBackground();
		batch.draw(bird, birdX, birdY, heroWidth, heroHeight);

		if (gameState == GameState.start) {
			//Oyun başladıysa
			scoreIncrease();
			for (short i = 0; i < numberOfEnemies; i++) {
				if (enemyX[i] < 0) {
					//Düsmanın x'i sıfırın altına indiyse başa alınır.
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;
					enemyOffset1[i] = (random.nextFloat()-0.5f) * (height - 150);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (height - 150);
					enemyOffset3[i] = (random.nextFloat()-0.5f) * (height - 150);
				}
				else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}
				batch.draw(enemy1, enemyX[i], height/2.0f + enemyOffset1[i], heroWidth, heroHeight);
				batch.draw(enemy2, enemyX[i], height/2.0f + enemyOffset2[i], heroWidth, heroHeight);
				batch.draw(enemy3, enemyX[i], height/2.0f + enemyOffset3[i], heroWidth, heroHeight);
				enemyCircle1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset1[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
				enemyCircle2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset2[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
				enemyCircle3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset3[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
			}

			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (birdY > 60) {
				velocity += 0.4f;
				//velocity yukarıda - değer olunca kuş uçacak.
				birdY -= velocity;
			}
			else if (birdY > Gdx.graphics.getHeight()) {
				gameState = GameState.end;
			}
			else {
				gameState = GameState.end;
			}
		}
		else if (gameState == GameState.restart) {
			//Oyun bittiyse ve Kullanıcı ekrana tıklarsa:
			if (Gdx.input.justTouched()) gameState = GameState.start;
		}
		else if (gameState == GameState.end) {
			gameOverFont.draw(batch, "Game over! Tap to play again.", width/5f,height/2f);
			if (Gdx.input.justTouched()) {
				gameState = GameState.start;
				setFirstValue();
			}
		}
		scoreFont.draw(batch, String.valueOf(score), 100, 110);
		batch.end();
		birdCircle.set(birdX+width/28f, birdY+Gdx.graphics.getHeight() / 16.0f, heroWidth/3);
		for ( int i = 0; i < numberOfEnemies; i++) {
			if (Intersector.overlaps(birdCircle,enemyCircle1[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])) {
				gameState = GameState.end;
			}
		}
	}

	//Oyun kapatıldığında olacak olaylar.
	@Override public void dispose() {
		batch.dispose();
		bird.dispose();
		background.dispose();
		assetManager.dispose();
		enemy1.dispose();
		enemy2.dispose();
		enemy3.dispose();
		scoreFont.dispose();
		gameOverFont.dispose();
	}

	private void setFirstValue() {
		//Kuşun başlangıc konumu belirlnir.
		birdX = width / 4.0f;
		birdY = height / 2.0f;

		//Düşmanların başlangıc konumu belirlnir.
		random = new Random();
		for (short i = 0; i < numberOfEnemies; i++) {
			enemyOffset1[i] = (random.nextFloat()-0.55f) * (height - 120);
			enemyOffset2[i] = (random.nextFloat()-0.55f) * (height - 120);
			enemyOffset3[i] = (random.nextFloat()-0.55f) * (height - 120);
			enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2.0f + i * distance;
			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}
		velocity = 0;
		scoredEnemy = 0;
		score = 0;
	}

	private void scoreIncrease() {
		if (enemyX[scoredEnemy] < birdX) {
			score++;
			if (scoredEnemy < numberOfEnemies-1) {
				scoredEnemy++;
			}
			else {
				scoredEnemy=0;
			}
		}
	}

	private void chanceBackground() {
		if (score >= 0 && score < 10) {
			background = assetManager.get("background1.png", Texture.class);
		}
		else if (score >= 10 && score < 20) {
			background = assetManager.get("background2.png", Texture.class);
		}
		else if (score >= 20 && score < 30) {
			background = assetManager.get("background3.png", Texture.class);
		}
		else if (score >= 30 && score < 40) {
			background = assetManager.get("background4.png", Texture.class);
		}
		else if (score >= 40 && score < 50) {
			background = assetManager.get("background5.png", Texture.class);
		}
		else if (score >= 50) {
			background = assetManager.get("background6.png", Texture.class);
		}
		batch.draw(background, 0,0, width, height);
	}
}