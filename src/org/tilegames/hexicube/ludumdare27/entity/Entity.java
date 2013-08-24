package org.tilegames.hexicube.ludumdare27.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity
{
	public abstract double getRadius();
	public abstract double getX();
	public abstract double getY();
	public abstract void tick();
	public abstract void collidedWith(Entity e);
	
	public abstract boolean isAlive();
	
	public abstract void render(SpriteBatch batch);
}