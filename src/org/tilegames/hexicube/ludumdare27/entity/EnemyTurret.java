package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyTurret extends Enemy
{
	private static final Texture tex = Game.loadImage("enemysnipe");
	
	private int timer, spawnTimer;
	
	public EnemyTurret(double x, double y)
	{
		this.x = x;
		this.y = y;
		alive = true;
		timer = Game.rand.nextInt(76);
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
		timer--;
		if(timer <= 0)
		{
			timer = 75+Game.rand.nextInt(26);
			Game.bullets.add(new Bullet(x, y, 3+Game.rand.nextDouble()*3, Math.atan2(Game.player.getY()-y, Game.player.getX()-x), false, Game.rand.nextInt(10)<3));
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
	public void render(SpriteBatch batch, boolean trail)
	{
		if(trail) return;
		if(spawnTimer > 0) batch.setColor(0, 1, 1, 1);
		else batch.setColor(1, 1, 1, 1);
		batch.draw(tex, (float)x-8, (float)y-8);
	}
}