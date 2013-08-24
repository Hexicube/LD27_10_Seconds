package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemySlingshot extends Enemy
{
	private static final Texture tex = Game.loadImage("enemysling");
	
	private int timer, spawnTimer;
	private double angle;
	
	public EnemySlingshot(double x, double y)
	{
		this.x = x;
		this.y = y;
		alive = true;
		timer = -40+Game.rand.nextInt(41);
		spawnTimer = 50;
	}
	
	@Override
	public double getRadius()
	{
		return 8;
	}
	@Override
	public double getX()
	{
		return x;
	}
	@Override
	public double getY()
	{
		return y;
	}
	@Override
	public void tick()
	{
		if(Game.wipeout > 0)
		{
			alive = false;
			return;
		}
		if(spawnTimer > 0)
		{
			spawnTimer--;
			return;
		}
		if(timer <= -50)
		{
			timer = 30;
			angle = Math.atan2(y-Game.player.getY(), Game.player.getX()-x)+Math.PI/2;
		}
		else if(timer > 0)
		{
			x += Math.sin(angle)*timer*0.2;
			y += Math.cos(angle)*timer*0.2;
		}
		Game.checkCollision(this, Game.player);
		timer--;
	}
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	@Override
	public void render(SpriteBatch batch)
	{
		if(spawnTimer > 0) batch.setColor(0, 1, 1, 1);
		else batch.setColor(1, 1, 1, 1);
		batch.draw(tex, (float)x-8, (float)y-8);
	}
}