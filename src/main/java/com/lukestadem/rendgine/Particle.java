package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.ModernImmediateRenderer2D;
import org.joml.Vector2f;

public class Particle {
	
	public Vector2f pos;
	public Vector2f dim;
	public int color;
	
	public Particle(float x, float y, float width, float height, int color){
		pos = new Vector2f(x, y);
		dim = new Vector2f(width, height);
		this.color = color;
	}
	
	public void render(ModernImmediateRenderer2D mir){
		mir.rect(pos.x, pos.y, dim.x, dim.y, color);
	}
}
