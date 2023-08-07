package com.emirozturk.survivorbird;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, bird, enemy1, enemy2, enemy3;
	float birdX = 0, birdY = 0, velocity = 0, heroWidth, heroHeight, distance;
	int width, height;
	boolean gameState = false;
	byte numberOfEnemies = 4, enemyVelocity = 10;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffset1 = new float[numberOfEnemies];
	float[] enemyOffset2 = new float[numberOfEnemies];
	float[] enemyOffset3 = new float[numberOfEnemies];
	Random random;
	Circle birdCircle;
	Circle[] enemyCircle1, enemyCircle2, enemyCircle3;

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
		birdY = height / 3.0f;

		birdCircle = new Circle();
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

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

		if (gameState) {
			//Oyun başladıysa
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
				//enemyCircle1[i] = new Circle(enemyX[i] + width/2, enemyOffset1[i] +height/2 +height/20, );
			}

			if (Gdx.input.justTouched()) {
				velocity = -14;
			}
			if (birdY > 0) {
				velocity += 0.5f;
				//velocity yukarıda - değer olunca kuş uçacak.
				birdY -= velocity;
			}
		}
		else {
			//Oyun bittiyse ve Kullanıcı ekrana tıklarsa:
			if (Gdx.input.justTouched()) gameState = true;
		}
		batch.end();
		birdCircle.set(birdX+width/2.0f, birdY+height/2.0f, heroWidth/2);
	}
	
	@Override
	//Oyun kapatıldığında olacak olaylar.
	public void dispose() {

	}
}