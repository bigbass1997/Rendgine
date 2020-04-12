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
		return circle(x, y, radius, 1, 1, 1, 1);
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius, Color color){
		return circle(x, y, radius, Math.max(1, (int)(6 * (float) java.lang.Math.cbrt(radius))), color);
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius, float r, float g, float b, float a){
		return circle(x, y, radius, Math.max(1, (int)(6 * (float) java.lang.Math.cbrt(radius))), r, g, b, a);
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius, float segments, Color color){
		return circle(x, y, radius, segments, color.r, color.g, color.b, color.a);
	}
	
	public ModernImmediateRenderer2D circle(float x, float y, float radius, float segments, float r, float g, float b, float a){
		final float dTheta = (float) (2 * Math.PI / segments);
		final float cos = Math.cos(dTheta);
		final float sin = Math.sin(dTheta);
		
		float cx = radius;
		float cy = 0;
		segments--;
		for(int i = 0; i < segments; i++){
			color(r, g, b, a);
			vertex(x, y);
			color(r, g, b, a);
			vertex(x + cx, y + cy);
			
			final float tmp = cx;
			cx = (cos * cx) - (sin * cy);
			cy = (sin * tmp) + (cos * cy);
			
			color(r, g, b, a);
			vertex(x + cx, y + cy);
		}
		
		color(r, g, b, a);
		vertex(x, y);
		
		color(r, g, b, a);
		vertex(x + cx, y + cy);
		
		color(r, g, b, a);
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
		return polygonTriangulated(vertices, 1, 1, 1, 1);
	}
	
	/**
	 * Renders a filled polygon. The provided float array should contain (x,y) coordinates in pairs, and should have at
	 * least 3 vertices (6 floats). The vertices mark the edge line of the polygon, and will be triangulated. Thus,
	 * this method supports concave polygons.
	 *
	 * @param vertices array of vertex pairs
	 * @param colors array of rgba8888 colors
	 * @return renderer for chaining
	 */
	public ModernImmediateRenderer2D polygonTriangulated(float[] vertices, float[] colors){
		if(vertices.length < 6 || vertices.length % 2 != 0 || colors.length % 4 != 0){
			return this;
		}
		
		if(vertices.length == 6){
			for(int i = 0; i < 3; i++){
				color(colors[(i * 4)], colors[(i * 4) + 1], colors[(i * 4) + 2], colors[(i * 4) + 3]);
				vertex(vertices[(i * 2)], vertices[(i * 2) + 1]);
			}
			return this;
		}
		
		for(Integer index : Earcut.earcut(vertices)){
			color(colors[(index * 4)], colors[(index * 4) + 1], colors[(index * 4) + 2], colors[(index * 4) + 3]);
			vertex(vertices[(index * 2)], vertices[(index * 2) + 1]);
		}
		
		return this;
	}
	
	/**
	 * Renders a filled polygon. The provided float array should contain (x,y) coordinates in pairs, and should have at
	 * least 3 vertices (6 floats). The vertices mark the edge line of the polygon, and will be triangulated. Thus,
	 * this method supports concave polygons.
	 *
	 * @param vertices array of vertex pairs
	 * @param r red color component
	 * @param g green color component
	 * @param b blue color component
	 * @param a alpha color component
	 * @return renderer for chaining
	 */
	public ModernImmediateRenderer2D polygonTriangulated(float[] vertices, float r, float g, float b, float a){
		if(vertices.length < 6 || vertices.length % 2 != 0){
			return this;
		}
		
		if(vertices.length == 6){
			for(int i = 0; i < 3; i++){
				color(r, g, b, a);
				vertex(vertices[(i * 2)], vertices[(i * 2) + 1]);
			}
			return this;
		}
		
		for(Integer index : Earcut.earcut(vertices)){
			color(r, g, b, a);
			vertex(vertices[(index * 2)], vertices[(index * 2) + 1]);
		}
		
		return this;
	}
}
