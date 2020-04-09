package com.lukestadem.rendgine.graphics.opengl;

public class TextureRegion {
	
	public final Texture texture;
	public final float u;
	public final float v;
	public final float u2;
	public final float v2;
	
	/** region width in pixels */
	public final int width;
	/** region height in pixels */
	public final int height;
	
	public TextureRegion(Texture texture, float u, float v, float u2, float v2){
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.u2 = u2;
		this.v2 = v2;
		
		width = Math.round(Math.abs(u2 - u) * texture.width);
		height = Math.round(Math.abs(v2 - v) * texture.height);
	}
	
	public TextureRegion(Texture texture, int x, int y, int width, int height){
		this.texture = texture;
		
		u = texture.width / (float) x;
		v = texture.height / (float) y;
		u2 = texture.width / (float) (x + width);
		v2 = texture.width / (float) (y + height);
		
		this.width = width;
		this.height = height;
	}
	
	
}
