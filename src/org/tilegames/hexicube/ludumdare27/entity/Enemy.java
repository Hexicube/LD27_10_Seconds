package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

public abstract class Enemy extends Entity
{
	protected double x, y;
	protected boolean alive;
	
	@Override
	public void collidedWith(Entity e)
	{
		if(e instanceof Bullet)
		{
			if(alive && Game.rand.nextInt(100) < 3)
			{
				int randVal = Game.rand.nextInt(100);
				PowerupType type;
				if(randVal < 70) type = PowerupType.SHIELD;
				else if(randVal < 80) type = PowerupType.HOMING;
				else if(randVal < 90) type = PowerupType.PIERCE;
				else if(randVal < 97) type = PowerupType.REGEN;
				else type = PowerupType.WIPEOUT;
				Game.powerups.add(new Powerup(x, y, type));
			}
			alive = false;
		}
	}
}