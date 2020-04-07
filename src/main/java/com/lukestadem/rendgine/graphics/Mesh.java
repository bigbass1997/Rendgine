package com.lukestadem.rendgine.graphics;

import com.lukestadem.rendgine.graphics.opengl.ShaderProgram;
import com.lukestadem.rendgine.graphics.opengl.VertexArrayObject;
import com.lukestadem.rendgine.graphics.opengl.VertexAttributes;
import com.lukestadem.rendgine.util.Disposable;
import org.joml.Matrix4f;

public class Mesh implements Disposable {
	
	private final VertexArrayObject vao;
	
	public final VertexAttributes attribs;
	
	private final int maxVertices;
	private int verticesPushed;
	
	public Mesh(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		attribs = new VertexAttributes(hasColors, hasNormals, hasTexCoords);
		
		vao = new VertexArrayObject(attribs, maxVertices, maxIndices);
		
		this.maxVertices = maxVertices;
		verticesPushed = 0;
	}
	
	public Mesh addVertex(float[] data){
		vao.addVertex(data);
		verticesPushed++;
		return this;
	}
	
	public Mesh addIndices(int... indices){
		vao.addIndices(indices);
		return this;
	}
	
	public void clearAllData(){
		vao.clearAllData();
		verticesPushed = 0;
	}
	
	public void resetOffsets(){
		vao.resetOffsets();
		verticesPushed = 0;
	}
	
	public int getVertexSize(){
		return attribs.getVertexSize();
	}
	
	public boolean hasUsage(VertexAttributes.Usage use){
		return attribs.hasUsage(use);
	}
	
	public int getVertexOffset(VertexAttributes.Usage use){
		return vao.getVertexOffset(use);
	}
	
	public int getMaxVertices(){
		return maxVertices;
	}
	
	public int getVerticesPushed(){
		return verticesPushed;
	}
	
	/**
	 * Renders this mesh. If {@code bindExternally} is true, . Doing
	 * this can be useful when using multiple meshes or using the shader for other
	 * operations, in order to reduce multiple bind/unbind cycles.
	 *
	 * @param shader shader to be bound/unbound, can be null
	 * @param primitive OpenGL render primitive type GL_*
	 */
	public void render(ShaderProgram shader, boolean bindExternally, int primitive, Matrix4f projModelView){
		if(!bindExternally){
			shader.bind();
		}
		
		vao.bind();
		shader.setUniform("projModelView", projModelView);
		vao.render(primitive);
		vao.unbind();
		
		if(!bindExternally){
			shader.unbind();
		}
	}
	
	/**
	 * Rewrites vertices and indices based on the current vertices. This is a potentially costly function!
	 *
	 * @return this mesh for chaining
	 */
	@Deprecated
	public Mesh generateIndices(){
		//TODO Finish method
		
		//vao.addIndices()
		return this;
	}
	
	@Override
	public void dispose(){
		vao.dispose();
	}
}
