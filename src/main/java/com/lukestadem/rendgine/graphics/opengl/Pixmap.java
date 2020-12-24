package com.lukestadem.rendgine.graphics.opengl;

import com.lukestadem.rendgine.graphics.Color;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Pixmap {
	
	public final int[] map;
	
	public final int width;
	public final int height;
	
	/**
	 * Creates a new blank map of pixels. All pixels are initially set to black with full alpha.
	 * 
	 * @param width width of the image map in pixels
	 * @param height height of the image map in pixels
	 */
	public Pixmap(int width, int height){
		this(width, height, false);
	}
	
	/**
	 * Creates a new blank map of pixels. All pixels are initially set to black with either zero or full alpha.
	 * 
	 * @param width width of the image map in pixels
	 * @param height height of the image map in pixels
	 * @param fullAlpha if true, the alpha channel of every pixel will be initially set to 0xFF
	 */
	public Pixmap(int width, int height, boolean fullAlpha){
		this.width = width;
		this.height = height;
		
		map = new int[width * height];
		
		final int cA = Color.toIntPack(0, 0, 0, 1);
		if(fullAlpha){
			Arrays.fill(map, cA);
		}
	}
	
	/**
	 * Copies the raw data of a texture into a new pixel map of the same dimensions.
	 * 
	 * @param tex texture instance to copy data from
	 */
	public Pixmap(Texture tex){
		width = tex.width;
		height = tex.height;
		
		map = new int[width * height];
		
		for(int i = 0; i < map.length; i += 4){
			map[i] = Color.toIntPack(tex.texPixelData.get(i), tex.texPixelData.get(i + 1), tex.texPixelData.get(i + 2), tex.texPixelData.get(i + 3));
		}
	}
	
	/**
	 * Creates and returns a byte buffer filled with this map's pixel data. Each group of four bytes represents one pixel, using RGBA format.
	 * 
	 * @return a new byte buffer of pixel data
	 */
	public ByteBuffer getByteBuffer(){
		final ByteBuffer buf = ByteBuffer.allocateDirect(map.length * 4);
		
		for(int in : map){
			buf.putInt(in);
		}
		buf.position(0);
		
		return buf;
	}
	
	/**
	 * Sets the color of a pixel in the pixmap.
	 * 
	 * @param x x-position from the left
	 * @param y y-position from the top
	 * @param color the color in RGBA8888 format
	 */
	public void drawPixel(int x, int y, int color){
		drawPixel(getIndex(x, y), color);
	}
	
	/**
	 * Sets the color of a pixel in the pixmap.
	 * 
	 * @param index index of the pixel such that <code>index = ((y * width) + x)</code>
	 * @param color the color in RGBA8888 format
	 */
	public void drawPixel(int index, int color){
		map[index] = color;
	}
	
	/**
	 * Draws a filled rectangle onto the pixmap. Any extra data outside the bounds of the map will be ignored.
	 * 
	 * @param x x-position from the left
	 * @param y y-position from the top
	 * @param w width of rectangle
	 * @param h height of rectangle
	 * @param color the color in RGBA8888 format
	 */
	public void drawRect(int x, int y, int w, int h, int color){
		if(x + w > width){
			w = x + w - width;
		}
		if(y + h > height){
			h = y + h - height;
		}
		
		for(int i = x; i < x + w; i++){
			for(int j = y; j < y + h; j++){
				map[getIndex(i, j)] = color;
			}
		}
	}
	
	/**
	 * Multiplies all pixels of this map by the provided color.
	 * 
	 * @param color RGBA8888 formatted color to multiply with
	 */
	public void multiply(int color){
		for(int i = 0; i < map.length; i++){
			map[i] *= color;
		}
	}
	
	/**
	 * Multiplies all pixels within a rectangular area by the provided color.
	 * 
	 * @param x x-position from the left
	 * @param y y-position from the top
	 * @param w width of rectangle
	 * @param h height of rectangle
	 * @param color the color to multiply with in RGBA8888 format
	 */
	public void multiply(int x, int y, int w, int h, int color){
		if(x + w > width){
			w = x + w - width;
		}
		if(y + h > height){
			h = y + h - height;
		}
		
		for(int i = x; i < x + w; i++){
			for(int j = y; j < y + h; j++){
				map[getIndex(i, j)] *= color;
			}
		}
	}
	
	/**
	 * Converts a map index to an x-coordinate.
	 * 
	 * @param index index of pixel in map
	 * @return x-coordinate represented by index
	 */
	protected int getX(int index){
		return index % width;
	}
	
	/**
	 * Converts a map index to a y-coordinate.
	 * 
	 * @param index index of pixel in map
	 * @return y-coordinate represented by index
	 */
	protected int getY(int index){
		return index / width;
	}
	
	/**
	 * Converts an (x, y) position to a map index.
	 * 
	 * @param x x-coordinate from the left
	 * @param y y-coordinate from the top
	 * @return index represented by provided (x, y) coordinate
	 */
	protected int getIndex(int x, int y){
		return (y * width) + x;
	}
}
