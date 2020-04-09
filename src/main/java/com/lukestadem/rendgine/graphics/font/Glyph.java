package com.lukestadem.rendgine.graphics.font;

public class Glyph {
	
	public int charid;
	
	public float u;
	public float v;
	public float u2;
	public float v2;
	
	public float width;
	public float height;
	
	public int xOffset;
	public int yOffset;
	public int xAdvance;
	
	public Glyph(){
		
	}
	
	public Glyph(int charid, float u, float v, float u2, float v2, float width, float height, int xOffset, int yOffset, int xAdvance){
		this.charid = charid;
		
		this.u = u;
		this.v = v;
		this.u2 = u2;
		this.v2 = v2;
		
		this.width = width;
		this.height = height;
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xAdvance = xAdvance;
	}
	
	@Override
	public String toString(){
		final String SEP = ", ";
		return "[" + charid + SEP
				+ u + SEP
				+ v + SEP
				+ u2 + SEP
				+ v2 + SEP
				+ width + SEP
				+ height + SEP
				+ xOffset + SEP
				+ yOffset + SEP
				+ xAdvance + "]";
	}
}
