package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends Entity
{
	private double x, y, prevX, prevY, spdX, spdY, speed, angle;
	private boolean playerFired, homing, alive;
	
	private static final Texture tex = Game.loadImage("bullet");
	
	public Bullet(double x, double y, double speed, double angle, boolean playerFired, boolean homing)
	{
		this.x = x;
		this.y = y;
		prevX = x;
		prevY = y;
		spdX = Math.cos(angle)*speed;
		spdY = Math.sin(angle)*speed;
		this.speed = speed;
		this.angle = angle;
		this.playerFired = playerFired;
		this.homing = homing;
		alive = true;
	}
	
	@Override
	public double getRadius()
	{
		return 4;
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
		if(homing)
		{
			if(playerFired)
			{
				double dist = 2500.001;
				Enemy target = null;
				int size = Game.enemies.size();
				for(int a = 0; a < size; a++)
				{
					Enemy e = Game.enemies.get(a);
					double distX = x-e.getX();
					if(Math.abs(distX) > dist) continue;
					double distY = y-e.getY();
					if(Math.abs(distY) > dist) continue;
					double dist2 = distX*distX+distY*distY;
					if(dist2 < dist)
					{
						dist = dist2;
						target = e;
					}
				}
				if(target != null)
				{
					double angle2 = Math.atan2(y-target.getY(), x-target.getX())+Math.PI;
					if(angle2-angle > Math.PI) angle2 -= Math.PI*2;
					if(angle-angle2 > Math.PI) angle2 += Math.PI*2;
					if(Math.abs(angle-angle2) < 0.15) angle = angle2;
					else if(angle > angle2) angle -= 0.15;
					else angle += 0.15;
				}
			}
			else
			{
				double angle2 = Math.atan2(y-Game.player.getY(), x-Game.player.getX())+Math.PI;
				if(angle2-angle > Math.PI) angle2 -= Math.PI*2;
				if(angle-angle2 > Math.PI) angle2 += Math.PI*2;
				if(Math.abs(angle-angle2) < 0.025) angle = angle2;
				else if(angle > angle2) angle -= 0.025;
				else angle += 0.025;
			}
			spdX = Math.cos(angle)*speed;
			spdY = Math.sin(angle)*speed;
		}
		prevX = x;
		prevY = y;
		x += spdX;
		y += spdY;
		if(playerFired)
		{
			int size = Game.enemies.size();
			for(int a = 0; a < size; a++)
			{
				Game.checkCollision(this, Game.enemies.get(a));
			}
		}
		else Game.checkCollision(this, Game.player);
		if(x < -4 || x > 516 || y < -4 || y > 516) alive = false;
	}
	@Override
	public void collidedWith(Entity e)
	{
		if(playerFired && Game.pierce == 0) alive = false;
	}
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	@Override
	public void render(SpriteBatch batch, boolean trail)
	{
		if(trail) batch.setColor(1, 0, 0, 1);
		else if(playerFired) batch.setColor(0, 1, 0, 1);
		else batch.setColor(1, 0, 0, 1);
		batch.draw(tex, (float)x-4, (float)y-4);
		if(trail) batch.draw(tex, (float)prevX-4, (float)prevY-4);
	}
}