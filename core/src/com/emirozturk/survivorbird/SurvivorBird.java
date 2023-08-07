package com.emirozturk.survivorbird;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
	byte gameState = 0;
	byte numberOfEnemies = 4, enemyVelocity = 9;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffset1 = new float[numberOfEnemies];
	float[] enemyOffset2 = new float[numberOfEnemies];
	float[] enemyOffset3 = new float[numberOfEnemies];
	Random random;
	Circle birdCircle;
	Circle[] enemyCircle1, enemyCircle2, enemyCircle3;
	BitmapFont font, font2;

	@Override
	//Oyun açıldığında olacak olaylar.
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("background1.png");
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

		//Kuşun başlangıc konumu belirlnir.
		birdX = width / 4.0f;
		birdY = height / 2.0f;

		birdCircle = new Circle();
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(7);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(9);

		//shapeRenderer = new ShapeRenderer();

		random = new Random();
		for (short i = 0; i < numberOfEnemies; i++) {
			enemyOffset1[i] = (random.nextFloat()-0.5f) * (height - 150);
			enemyOffset2[i] = (random.nextFloat()-0.5f) * (height - 150);
			enemyOffset3[i] = (random.nextFloat()-0.5f) * (height - 150);
			enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2.0f + i * distance;
			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}
	}

	@Override
	//Oyun devam ettiği sürece devamlı çağırılan bir metod.
	public void render() {
		batch.begin();
		//Arka planı ve kuşu ekrana çizer.
		batch.draw(background, 0,0, width, height);
		batch.draw(bird, birdX, birdY, heroWidth, heroHeight);

		if (gameState == 1) {
			//Oyun başladıysa
			if (enemyX[scoredEnemy] < birdX) {
				score++;
				if (scoredEnemy < numberOfEnemies-1) {
					scoredEnemy++;
				}
				else {
					scoredEnemy=0;
				}
			}
			if (score >= 10) {
				background = new Texture("background2.png");
			}
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
				/*enemyCircle1[i] = new Circle(enemyX[i] + width/2, enemyOffset1[i] +height/2 +height/20, width/30);
				enemyCircle2[i] = new Circle(enemyX[i] + width/2, enemyOffset2[i] +height/2 +height/20, width/30);
				enemyCircle3[i] = new Circle(enemyX[i] + width/2, enemyOffset3[i] +height/2 +height/20, width/30);*/
				enemyCircle1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset1[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
				enemyCircle2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset2[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
				enemyCircle3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30f,  Gdx.graphics.getHeight()/2f + enemyOffset3[i] + Gdx.graphics.getHeight() / 20f,Gdx.graphics.getWidth() / 35f);
			}

			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (birdY > 100) {
				velocity += 0.4f;
				//velocity yukarıda - değer olunca kuş uçacak.
				birdY -= velocity;
			}
			else  if (birdY > Gdx.graphics.getHeight()) {
				gameState = 2;
			}
			else {
				gameState = 2;
			}
		}
		else if (gameState == 0) {
			//Oyun bittiyse ve Kullanıcı ekrana tıklarsa:
			if (Gdx.input.justTouched()) gameState = 1;
		}
		else if (gameState == 2) {
			font2.draw(batch, "Game over! Tap to play again.", width/5,height/2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				birdY = height / 2.0f;
				for (short i = 0; i < numberOfEnemies; i++) {
					enemyOffset1[i] = (random.nextFloat()-0.5f) * (height - 150);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (height - 150);
					enemyOffset3[i] = (random.nextFloat()-0.5f) * (height - 150);
					enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2.0f + i * distance;
					enemyCircle1[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();
				}
				velocity = 0;
				scoredEnemy = 0;
				score = 0;
			}
		}
		font.draw(batch, String.valueOf(score), 100, 110);
		batch.end();
		birdCircle.set(birdX+width/28f, birdY+Gdx.graphics.getHeight() / 16.0f, heroWidth/3);
		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/
		for ( int i = 0; i < numberOfEnemies; i++) {
			/*shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffset1[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 35);
			shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 35);
			shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,  Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 35);*/
			if (Intersector.overlaps(birdCircle,enemyCircle1[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])) {
				gameState = 2;
			}
		}
		//shapeRenderer.end();
	}

	//Oyun kapatıldığında olacak olaylar.
	@Override public void dispose() {
		batch.dispose();
		bird.dispose();
	}
}