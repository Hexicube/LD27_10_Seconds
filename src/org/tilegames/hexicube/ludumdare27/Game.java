package org.tilegames.hexicube.ludumdare27;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.tilegames.hexicube.ludumdare27.entity.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener
{
	private static SpriteBatch batch;
	
	private static double delta;
	
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Powerup> powerups;
	public static Player player;
	
	public static int shield, pierce, regen, homing, wipeout;
	
	public static int waveTimer, timeAlive, timeLeft;
	
	public static Random rand;
	
	public static Texture solidwhite;
	
	@Override
	public void create()
	{
		FontHolder.prep();
		
		solidwhite = loadImage("solidwhite");
		
		rand = new Random();
		
		delta = 0;
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		powerups = new ArrayList<Powerup>();
		player = new Player(256, 256);
		batch = new SpriteBatch();
		
		waveTimer = 100;
		timeAlive = 0;
		timeLeft = 500;
	}
	@Override
	public void dispose()
	{
	}
	@Override
	public void pause()
	{
	}
	@Override
	public void render()
	{
		delta += Gdx.graphics.getDeltaTime();
		if(delta > 0.25) delta = 0.25;
		while(delta >= 0.02)
		{
			delta -= 0.02;
			tick();
		}
		batch.begin();
		batch.setColor(0.2f, 0.1f, 0.3f, 0.15f);
		batch.draw(solidwhite, 0, 0, 1, 1, 512, 512);
		batch.setColor(1, 1, 1, 1);
		int size = enemies.size();
		for(int a = 0; a < size; a++)
		{
			enemies.get(a).render(batch);
		}
		size = bullets.size();
		for(int a = 0; a < size; a++)
		{
			bullets.get(a).render(batch);
		}
		size = powerups.size();
		for(int a = 0; a < size; a++)
		{
			powerups.get(a).render(batch);
		}
		player.render(batch);
		batch.setColor(0, 0, 0, 1);
		batch.draw(solidwhite, 512, 0, 1, 1, 200, 512);
		batch.setColor(0, 1, 0, 1);
		FontHolder.render(batch, FontHolder.getCharList("Time alive:"), 516, 508, true);
		FontHolder.render(batch, FontHolder.getCharList(""+((double)timeAlive/50)+"s"), 516, 488, true);
		FontHolder.render(batch, FontHolder.getCharList("Remaining life:"), 516, 468, true);
		FontHolder.render(batch, FontHolder.getCharList(""+((double)timeLeft/50)+"s"), 516, 448, true);
		FontHolder.render(batch, FontHolder.getCharList("Active powers:"), 516, 428, true);
		int yPos = 408;
		if(shield > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Shield: "+((double)shield/50)+"s"), 526, 408, true);
			yPos -= 20;
		}
		if(pierce > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Pierce: "+((double)pierce/50)+"s"), 526, yPos, true);
			yPos -= 20;
		}
		if(regen > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Regen: "+((double)regen/50)+"s"), 526, yPos, true);
			yPos -= 20;
		}
		if(homing > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Homing: "+((double)homing/50)+"s"), 526, yPos, true);
			yPos -= 20;
		}
		if(wipeout > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Wipe-out: "+((double)wipeout/50)+"s"), 526, yPos, true);
		}
		batch.end();
	}
	@Override
	public void resize(int width, int height)
	{
	}
	@Override
	public void resume()
	{
	}
	
	private void tick()
	{
		if(player.isAlive())
		{
			if(regen > 0) timeLeft++;
			timeAlive++;
			waveTimer--;
			if(waveTimer <= 0)
			{
				int val = 4+rand.nextInt(7);
				waveTimer += val*10;
				for(int a = 0; a < val; a++)
				{
					double randVal = rand.nextDouble();
					if(randVal < 0.75) enemies.add(new EnemyMover(rand.nextInt(513), rand.nextInt(513)));
					else if(randVal < 0.95) enemies.add(new EnemySlingshot(rand.nextInt(513), rand.nextInt(513)));
					else enemies.add(new EnemyTurret(rand.nextInt(513), rand.nextInt(513)));
				}
			}
			int size = enemies.size();
			for(int a = 0; a < size; a++)
			{
				enemies.get(a).tick();
			}
			size = bullets.size();
			for(int a = 0; a < size; a++)
			{
				bullets.get(a).tick();
			}
			size = powerups.size();
			for(int a = 0; a < size; a++)
			{
				powerups.get(a).tick();
			}
			player.tick();
			size = enemies.size();
			for(int a = 0; a < size; a++)
			{
				if(!enemies.get(a).isAlive())
				{
					enemies.remove(a);
					a--;
					size--;
				}
			}
			size = bullets.size();
			for(int a = 0; a < size; a++)
			{
				if(!bullets.get(a).isAlive())
				{
					bullets.remove(a);
					a--;
					size--;
				}
			}
			size = powerups.size();
			for(int a = 0; a < size; a++)
			{
				if(!powerups.get(a).isAlive())
				{
					powerups.remove(a);
					a--;
					size--;
				}
			}
			if(shield > 0) shield--;
			if(pierce > 0) pierce--;
			if(regen > 0) regen--;
			if(homing > 0) homing--;
			if(wipeout > 0) wipeout--;
		}
	}
	
	public static void checkCollision(Entity e1, Entity e2)
	{
		double radSum = e1.getRadius()+e2.getRadius();
		double distX = e1.getX()-e2.getX();
		if(Math.abs(distX) > radSum) return;
		double distY = e1.getY()-e2.getY();
		if(Math.abs(distY) > radSum) return;
		double distSqrd = distX*distX+distY*distY;
		if(distSqrd <= radSum*radSum)
		{
			e1.collidedWith(e2);
			e2.collidedWith(e1);
		}
	}
	
	public static Texture loadImage(String name)
	{
		name = "images/" + name + ".png";
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return new Texture(Gdx.files.internal(name));
	}
	
	public static Sound loadSound(String name)
	{
		name = "sounds/" + name + ".ogg";
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return Gdx.audio.newSound(Gdx.files.internal(name));
	}
	
	public static Music loadMusic(String name)
	{
		name = "sounds/" + name + ".ogg";
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return Gdx.audio.newMusic(Gdx.files.internal(name));
	}
}