package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Powerup extends Entity
{
	private double x, y;
	private boolean alive;
	private PowerupType type;
	
	private static final Texture shield = Game.loadImage("powerupShield"),
								 pierce = Game.loadImage("powerupPierce"),
								 regen = Game.loadImage("powerupRegen"),
								 homing = Game.loadImage("powerupHoming"),
								 wipeout = Game.loadImage("powerupWipeout");
	
	public Powerup(double x, double y, PowerupType type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
		alive = true;
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
		Game.checkCollision(this, Game.player);
	}
	@Override
	public void collidedWith(Entity e)
	{
		if(e instanceof Player)
		{
			if(alive)
			{
				if(type == PowerupType.SHIELD) Game.shield += 100;
				else if(type == PowerupType.PIERCE) Game.pierce += 200;
				else if(type == PowerupType.REGEN) Game.regen += 50;
				else if(type == PowerupType.HOMING) Game.homing += 150;
				else Game.wipeout += 10;
			}
			alive = false;
		}
	}
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	@Override
	public void render(SpriteBatch batch, boolean trail)
	{
		if(trail) return;
		batch.setColor(1, 1, 1, 1);
		Texture tex;
		if(type == PowerupType.SHIELD) tex = shield;
		else if(type == PowerupType.PIERCE) tex = pierce;
		else if(type == PowerupType.REGEN) tex = regen;
		else if(type == PowerupType.HOMING) tex = homing;
		else tex = wipeout;
		batch.draw(tex, (float)x-8, (float)y-8);
	}
}