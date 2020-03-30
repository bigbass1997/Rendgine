package com.lukestadem.rendgine.graphics;

public class ModernImmediateRenderer2D extends ModernImmediateRenderer {
	
	public ModernImmediateRenderer2D(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(hasColors, hasNormals, hasTexCoords);
	}
	
	public ModernImmediateRenderer2D(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(maxVertices, maxIndices, hasColors, hasNormals, hasTexCoords);
	}
	
	public ModernImmediateRenderer2D vertex(float x, float y){
		vertex(x, y, 0);
		return this;
	}
	
	public ModernImmediateRenderer2D rect(float x, float y, float width, float height, int color){
		color(color);
		return rect(x, y, width, height);
	}
	
	public ModernImmediateRenderer2D rect(float x, float y, float width, float height, Color color){
		color(color);
		return rect(x, y, width, height);
	}
	
	public ModernImmediateRenderer2D rect(float x, float y, float width, float height){
		vertex(x, y);
		vertex(x, y + height);
		vertex(x + width, y);
		
		vertex(x + width, y);
		vertex(x, y + height);
		vertex(x + width, y + height);
		
		return this;
	}
}
