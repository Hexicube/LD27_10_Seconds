package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyMine extends Enemy
{
	private static final Texture tex = Game.loadImage("enemymine");
	
	private int spawnTimer;
	
	public EnemyMine(double x, double y)
	{
		this.x = x;
		this.y = y;
		alive = true;
		spawnTimer = 50;
	}
	
	@Override
	public double getRadius()
	{
		return 16;
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
		if(trail) return;
		if(spawnTimer > 0) batch.setColor(0, 1, 1, 1);
		else batch.setColor(1, 1, 1, 1);
		batch.draw(tex, (float)x-16, (float)y-16);
	}
	
	@Override
	public void collidedWith(Entity e)
	{
		if(alive)
		{
			if(spawnTimer > 0) return;
			super.collidedWith(e);
			if(!alive)
			{
				for(int a = 0; a < 32; a++)
				{
					Game.bullets.add(new Bullet(x, y, 1.5, Math.PI/16*a, false, false));
				}
			}
		}
	}
}