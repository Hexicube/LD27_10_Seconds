package org.tilegames.hexicube.ludumdare27.entity;

import org.tilegames.hexicube.ludumdare27.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Entity
{
	private double x, y, prevX, prevY;
	private boolean alive;
	
	private int shootTimer;
	
	private static final Texture tex = Game.loadImage("player");
	
	public Player(double x, double y)
	{
		this.x = x;
		this.y = y;
		prevX = x;
		prevY = y;
		alive = true;
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
		if(Game.timeLeft <= 0) alive = false;
		prevX = x;
		prevY = y;
		if(alive)
		{
			shootTimer--;
			if(Gdx.input.isKeyPressed(Keys.LEFT)) x-=3;
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) x+=3;
			if(Gdx.input.isKeyPressed(Keys.UP)) y+=3;
			if(Gdx.input.isKeyPressed(Keys.DOWN)) y-=3;
			if(x < 16) x = 16;
			if(y < 16) y = 16;
			if(x > 584) x = 584;
			if(y > 584) y = 584;
		}
		if(Gdx.input.isButtonPressed(0))
		{
			if(shootTimer <= 0)
			{
				shootTimer = 4;
				Game.bullets.add(new Bullet(x, y, 5, Math.atan2(y-(599-Gdx.input.getY()), x-Gdx.input.getX())+Math.PI, true, Game.homing>0));
			}
		}
	}
	
	@Override
	public void collidedWith(Entity e)
	{
		if(Game.shield > 0) return;
		if(e instanceof Enemy) Game.timeLeft--;
		else if(e instanceof Bullet) Game.timeLeft--;
	}
	
	@Override
	public boolean isAlive()
	{
		return alive;
	}
	
	@Override
	public void render(SpriteBatch batch, boolean trail)
	{
		if(Gdx.input.isButtonPressed(0) && !trail) batch.setColor(1, 0, 0, 1);
		else batch.setColor(1, 1, 1, 1);
		batch.draw(tex, (float)x-16, (float)y-16);
		if(trail) batch.draw(tex, (float)prevX-16, (float)prevY-16);
	}
}