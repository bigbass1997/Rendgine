package com.lukestadem.rendgine.graphics;

import earcut4j.Earcut;
import org.joml.Math;
import org.joml.Vector2f;

/**
 * Currently assumes primitive is GL_TRIANGLES when rendering!
 */
public class ModernImmediateRenderer2D extends ModernImmediateRenderer {
	
	private Vector2f tmpVec;
	
	public ModernImmediateRenderer2D(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(hasColors, hasNormals, hasTexCoords);
		
		tmpVec = new Vector2f();
	}
	
	public ModernImmediateRenderer2D(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		super(maxVertices, maxIndices, hasColors, hasNormals, hasTexCoords);
		
		tmpVec = new Vector2f();
	}
	
	public ModernImmediateRenderer2D vertex(float x, float y){
		vertex(x, y, 0);
		return this;
	}
	
	public ModernImmediateRenderer2D triangle(float x1, float y1, float x2, float y2, float x3, float y3){
		vertex(x1, y1);
		vertex(x2, y2);
		vertex(x3, y3);
		
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
		triangle(x, y, x, y + height, x + width, y);
		triangle(x + width, y, x, y + height, x + width, y + height);
		
		return this;
	}
	
	public ModernImmediateRenderer2D rectLine(float x1, float y1, float x2, float y2, float thickness){
		tmpVec.set(y2 - y1, x1 - x2).normalize();
		final float tx = tmpVec.x * thickness * 0.5f;
		final float ty = tmpVec.y * thickness * 0.5f;
		
		triangle(x1 + tx, y1 + ty, x1 - tx, y1 - ty, x2 + tx, y2 + ty);
		triangle(x2 - tx, y2 - ty, x2 + tx, y2 + ty, x1 - tx, y1 - ty);
		
		return this;
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius){
		return circle(x, y, radius, Math.max(1, (int)(6 * (float) java.lang.Math.cbrt(radius))));
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius, float segments){
		final float dTheta = (float) (2 * Math.PI / segments);
		final float cos = Math.cos(dTheta);
		final float sin = Math.sin(dTheta);
		
		float cx = radius;
		float cy = 0;
		segments--;
		for(int i = 0; i < segments; i++){
			vertex(x, y);
			vertex(x + cx, y + cy);
			
			final float tmp = cx;
			cx = (cos * cx) - (sin * cy);
			cy = (sin * tmp) + (cos * cy);
			
			vertex(x + cx, y + cy);
		}
		
		vertex(x, y);
		vertex(x + cx, y + cy);
		vertex(x + radius, y);
		
		return this;
	}
	
	/**
	 * Renders a filled polygon. The provided float array should contain (x,y) coordinates in pairs, and should have at
	 * least 3 vertices (6 floats). The vertices mark the edge line of the polygon, and will be triangulated. Thus,
	 * this method supports concave polygons.
	 *
	 * @param vertices array of vertex pairs
	 * @return renderer for chaining
	 */
	public ModernImmediateRenderer2D polygonTriangulated(float[] vertices){
		if(vertices.length < 6 || vertices.length % 2 != 0){
			return this;
		}
		
		if(vertices.length == 6){
			return triangle(vertices[0],vertices[1],vertices[2],vertices[3],vertices[4],vertices[5]);
		}
		
		for(Integer index : Earcut.earcut(vertices)){
			vertex(vertices[index * 2], vertices[(index * 2) + 1]);
		}
		
		return this;
	}
	
	
}
