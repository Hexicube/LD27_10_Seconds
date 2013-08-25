package org.tilegames.hexicube.ludumdare27;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.tilegames.hexicube.ludumdare27.entity.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	
	private static boolean tracer, tracerKeyPress, spawnKeyPress, lastRender;
	private static int spawnMode;
	
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
		timeLeft = 0;
		
		tracer = true;
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
		batch.begin();
		if(!player.isAlive())
		{
			if(Gdx.input.isKeyPressed(Keys.SPACE))
			{
				shield = 0;
				pierce = 0;
				regen = 0;
				homing = 0;
				wipeout = 0;
				enemies.clear();
				bullets.clear();
				powerups.clear();
				player = new Player(256, 256);
				waveTimer = 100;
				timeAlive = 0;
				timeLeft = 500;
			}
			if(Gdx.input.isKeyPressed(Keys.T))
			{
				if(!tracerKeyPress) tracer = !tracer;
				tracerKeyPress = true;
			}
			else tracerKeyPress = false;
			if(Gdx.input.isKeyPressed(Keys.S))
			{
				if(!spawnKeyPress)
				{
					spawnMode++;
					if(spawnMode > 4) spawnMode = 0;
				}
				spawnKeyPress = true;
			}
			else spawnKeyPress = false;
			batch.setColor(0, 0, 0, 1);
			batch.draw(solidwhite, 512, 0, 1, 1, 200, 512);
			batch.setColor(0, 1, 0, 1);
			FontHolder.render(batch, FontHolder.getCharList("[T]racers: "+(tracer?"On":"Off")), 516, 80, true);
			FontHolder.render(batch, FontHolder.getCharList("[S]pawn mode: "), 516, 60, true);
			if(spawnMode == 0) FontHolder.render(batch, FontHolder.getCharList("Normal"), 526, 40, true);
			else if(spawnMode == 1) FontHolder.render(batch, FontHolder.getCharList("Swarm (Easy)"), 526, 40, true);
			else if(spawnMode == 2) FontHolder.render(batch, FontHolder.getCharList("Lungers (Easy)"), 526, 40, true);
			else if(spawnMode == 3) FontHolder.render(batch, FontHolder.getCharList("Bullet Hell (Hard)"), 526, 40, true);
			else if(spawnMode == 4) FontHolder.render(batch, FontHolder.getCharList("Bullet Hell (Insane)"), 526, 40, true);
			else FontHolder.render(batch, FontHolder.getCharList("???"), 526, 40, true);
			FontHolder.render(batch, FontHolder.getCharList("Space to play"), 516, 20, true);
		}
		if(!player.isAlive() && lastRender)
		{
			batch.end();
			return;
		}
		lastRender = !player.isAlive();
		if(tracer)
		{
			int size = enemies.size();
			for(int a = 0; a < size; a++)
			{
				enemies.get(a).render(batch, true);
			}
			size = bullets.size();
			for(int a = 0; a < size; a++)
			{
				bullets.get(a).render(batch, true);
			}
			size = powerups.size();
			for(int a = 0; a < size; a++)
			{
				powerups.get(a).render(batch, true);
			}
			player.render(batch, true);
		}
		delta += Gdx.graphics.getDeltaTime();
		if(delta > 0.25) delta = 0.25;
		while(delta >= 0.02)
		{
			delta -= 0.02;
			tick();
		}
		batch.setColor(0.2f, 0.1f, 0.3f, tracer?0.15f:1);
		batch.draw(solidwhite, 0, 0, 1, 1, 512, 512);
		batch.setColor(1, 1, 1, 1);
		int size = enemies.size();
		for(int a = 0; a < size; a++)
		{
			enemies.get(a).render(batch, false);
		}
		size = bullets.size();
		for(int a = 0; a < size; a++)
		{
			bullets.get(a).render(batch, false);
		}
		size = powerups.size();
		for(int a = 0; a < size; a++)
		{
			powerups.get(a).render(batch, false);
		}
		player.render(batch, false);
		batch.setColor(0, 0, 0, 1);
		batch.draw(solidwhite, 512, 0, 1, 1, 200, 512);
		batch.setColor(0, 1, 0, 1);
		FontHolder.render(batch, FontHolder.getCharList("Time alive:"), 516, 508, true);
		FontHolder.render(batch, FontHolder.getCharList(numberToDuration(timeAlive)), 516, 488, true);
		FontHolder.render(batch, FontHolder.getCharList("Remaining life:"), 516, 468, true);
		FontHolder.render(batch, FontHolder.getCharList(numberToDuration(timeLeft)), 516, 448, true);
		FontHolder.render(batch, FontHolder.getCharList("Active powers:"), 516, 428, true);
		int yPos = 408;
		if(shield > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Shield: "+numberToDuration(shield)), 526, 408, true);
			yPos -= 20;
		}
		if(pierce > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Pierce: "+numberToDuration(pierce)), 526, yPos, true);
			yPos -= 20;
		}
		if(regen > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Regen: "+numberToDuration(regen)), 526, yPos, true);
			yPos -= 20;
		}
		if(homing > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Homing: "+numberToDuration(homing)), 526, yPos, true);
			yPos -= 20;
		}
		if(wipeout > 0)
		{
			FontHolder.render(batch, FontHolder.getCharList("Wipe-out: "+numberToDuration(wipeout)), 526, yPos, true);
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
					if(spawnMode == 1) enemies.add(new EnemyMover(rand.nextInt(513), rand.nextInt(513)));
					else if(spawnMode == 2) enemies.add(new EnemySlingshot(rand.nextInt(513), rand.nextInt(513)));
					else if(spawnMode == 3) enemies.add(new EnemyTurret(rand.nextInt(513), rand.nextInt(513)));
					else if(spawnMode == 4) enemies.add(new EnemyMine(rand.nextInt(513), rand.nextInt(513)));
					else
					{
						double randVal = rand.nextDouble();
						if(randVal < 0.6) enemies.add(new EnemyMover(rand.nextInt(513), rand.nextInt(513)));
						else if(randVal < 0.7) enemies.add(new EnemyDodgingMover(rand.nextInt(513), rand.nextInt(513)));
						else if(randVal < 0.8) enemies.add(new EnemySlingshot(rand.nextInt(513), rand.nextInt(513)));
						else if(randVal < 0.9) enemies.add(new EnemyMine(rand.nextInt(513), rand.nextInt(513)));
						else enemies.add(new EnemyTurret(rand.nextInt(513), rand.nextInt(513)));
					}
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
	
	public static String numberToDuration(int val)
	{
		if(val < 0) return "00.00s";
		int mins = 0, secs = 0, mils = val*2;
		while(mils >= 100)
		{
			mils -= 100;
			secs++;
		}
		while(secs >= 60)
		{
			secs -= 60;
			mins++;
		}
		String M = ""+mins, S = ""+secs, ML = ""+mils;
		if(secs < 10) S = "0"+S;
		if(mils < 10) ML = "0"+ML;
		if(mins > 0) return M+"m "+S+"."+ML+"s";
		else return S+"."+ML+"s";
	}
}