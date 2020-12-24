package com.lukestadem.rendgine.graphics;

public class Color {
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color(){}
	
	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		clamp();
	}
	
	public Color(byte r, byte g, byte b, byte a){
		this.r = r / 255f;
		this.g = g / 255f;
		this.b = b / 255f;
		this.a = a / 255f;
		clamp();
	}
	
	public Color clamp(){
		r = r < 0 ? 0 : (r > 1 ? 1 : r);
		g = g < 0 ? 0 : (g > 1 ? 1 : g);
		b = b < 0 ? 0 : (b > 1 ? 1 : b);
		a = a < 0 ? 0 : (a > 1 ? 1 : a);
		return this;
	}
	
	public int toIntPack(){
		return Color.toIntPack(r, g, b, a);
	}
	
	public Color setColor(int rgba8888){
		r = ((rgba8888 & 0xff000000) >>> 24) / 255f;
		g = ((rgba8888 & 0x00ff0000) >>> 16) / 255f;
		b = ((rgba8888 & 0x0000ff00) >>> 8) / 255f;
		a = ((rgba8888 & 0x000000ff)) / 255f;
		
		return this;
	}
	
	
	public static Color toColor(int rgba8888){
		final Color color = new Color();
		color.r = ((rgba8888 & 0xff000000) >>> 24) / 255f;
		color.g = ((rgba8888 & 0x00ff0000) >>> 16) / 255f;
		color.b = ((rgba8888 & 0x0000ff00) >>> 8) / 255f;
		color.a = ((rgba8888 & 0x000000ff)) / 255f;
		
		return color;
	}
	
	@Override
	public String toString(){
		String str = Integer.toHexString(((int)(255 * r) << 24) | ((int)(255 * g) << 16) | ((int)(255 * b) << 8) | ((int)(255 * a)));
		while(str.length() < 8){
			str = "0" + str;
		}
		return str;
	}
	
	public static String toString(int rgba8888){
		final int r = ((rgba8888 & 0xff000000) >>> 24);
		final int g = ((rgba8888 & 0x00ff0000) >>> 16);
		final int b = ((rgba8888 & 0x0000ff00) >>> 8);
		final int a = ((rgba8888 & 0x000000ff));
		String str = Integer.toHexString((r << 24) | (g << 16) | (b << 8) | a);
		while(str.length() < 8){
			str = "0" + str;
		}
		
		return str;
	}
	
	public static int toIntPack(float r, float g, float b, float a){
		return ((int)(255 * r) << 24) | ((int)(255 * g) << 16) | ((int)(255 * b) << 8) | ((int)(255 * a));
	}
	
	public static int toIntPack(byte r, byte g, byte b, byte a){
		return r | g | b | a;
	}
}
