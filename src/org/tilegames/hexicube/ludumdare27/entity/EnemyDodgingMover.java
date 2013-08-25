package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyDodgingMover extends Enemy
{
	private static final Texture tex = Game.loadImage("enemydodge");
	
	private int spawnTimer;
	
	public EnemyDodgingMover(double x, double y)
	{
		this.x = x;
		this.y = y;
		prevX = x;
		prevY = y;
		alive = true;
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
		boolean bulletNear = false;
		int size = Game.bullets.size();
		for(int a = 0; a < size; a++)
		{
			Bullet b = Game.bullets.get(a);
			double distX = x-b.getX();
			if(distX > 50) continue;
			double distY = y-b.getY();
			if(distY > 50) continue;
			if(distX*distX+distY*distY <= 2500)
			{
				bulletNear = true;
				break;
			}
		}
		double angle = Math.atan2(y-Game.player.getY(), Game.player.getX()-x)+Math.PI/2;
		if(bulletNear) angle += Math.PI/3;
		prevX = x;
		prevY = y;
		x += Math.sin(angle)*2;
		y += Math.cos(angle)*2;
		Game.checkCollision(this, Game.player);
	}
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	@Override
	public void render(SpriteBatch batch, boolean trail)
	{
		if(spawnTimer > 0) batch.setColor(0, 1, 1, 1);
		else batch.setColor(1, 1, 1, 1);
		if(trail)
		{
			batch.draw(EnemyMover.tex, (float)x-8, (float)y-8);
			batch.draw(EnemyMover.tex, (float)prevX-8, (float)prevY-8);
		}
		else batch.draw(tex, (float)x-8, (float)y-8);
	}
}