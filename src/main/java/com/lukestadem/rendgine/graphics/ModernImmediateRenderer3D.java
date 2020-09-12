package com.lukestadem.rendgine.graphics;

import org.joml.Vector3f;

public class ModernImmediateRenderer3D extends ModernImmediateRenderer2D {
	
	protected Vector3f tmpVec3f;
	
	public ModernImmediateRenderer3D(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(hasColors, hasNormals, hasTexCoords);
		
		tmpVec3f = new Vector3f();
	}
	
	public ModernImmediateRenderer3D(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(maxVertices, maxIndices, hasColors, hasNormals, hasTexCoords);
		
		tmpVec3f = new Vector3f();
	}
	
	public ModernImmediateRenderer3D triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3){
		vertex(x1, y1, z1);
		vertex(x2, y2, z2);
		vertex(x3, y3, z3);
		
		return this;
	}
	
	public ModernImmediateRenderer3D cube(float x, float y, float z, float len, int color){
		color(color);
		
		// front
		triangle(x, y, z,  x + len, y, z,  x, y + len, z);
		triangle(x + len, y, z,  x + len, y + len, z,  x, y + len, z);
		
		// right
		triangle(x + len, y, z,  x + len, y, z + len,  x + len, y + len, z);
		triangle(x + len, y, z + len,  x + len, y + len, z + len,  x + len, y + len, z);
		
		// back
		triangle(x + len, y, z + len,  x, y, z + len,  x + len, y + len, z + len);
		triangle(x, y, z + len,  x, y + len, z + len,  x + len, y + len, z + len);
		
		// left
		triangle(x, y, z + len,  x, y, z,  x, y + len, z + len);
		triangle(x, y, z,  x, y + len, z,  x, y + len, z + len);
		
		// top
		triangle(x, y + len, z,  x + len, y + len, z,  x, y + len, z + len);
		triangle(x + len, y + len, z,  x + len, y + len, z + len,  x, y + len,  z + len);
		
		// bottom
		triangle(x, y, z + len,  x + len, y, z + len,  x, y, z);
		triangle(x + len, y, z + len,  x + len, y, z,  x, y, z);
		
		return this;
	}
	
	public ModernImmediateRenderer3D cube(float x, float y, float z, float len, int fColor, int rColor, int bColor, int lColor, int tColor, int botColor){
		// front
		color(fColor);
		triangle(x, y, z,  x + len, y, z,  x, y + len, z);
		triangle(x + len, y, z,  x + len, y + len, z,  x, y + len, z);
		
		// right
		color(rColor);
		triangle(x + len, y, z,  x + len, y, z + len,  x + len, y + len, z);
		triangle(x + len, y, z + len,  x + len, y + len, z + len,  x + len, y + len, z);
		
		// back
		color(bColor);
		triangle(x + len, y, z + len,  x, y, z + len,  x + len, y + len, z + len);
		triangle(x, y, z + len,  x, y + len, z + len,  x + len, y + len, z + len);
		
		// left
		color(lColor);
		triangle(x, y, z + len,  x, y, z,  x, y + len, z + len);
		triangle(x, y, z,  x, y + len, z,  x, y + len, z + len);
		
		// top
		color(tColor);
		triangle(x, y + len, z,  x + len, y + len, z,  x, y + len, z + len);
		triangle(x + len, y + len, z,  x + len, y + len, z + len,  x, y + len,  z + len);
		
		// bottom
		color(botColor);
		triangle(x, y, z + len,  x + len, y, z + len,  x, y, z);
		triangle(x + len, y, z + len,  x + len, y, z,  x, y, z);
		
		return this;
	}
}
