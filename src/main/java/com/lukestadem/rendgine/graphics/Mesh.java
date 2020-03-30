package com.lukestadem.rendgine.graphics;

import com.lukestadem.rendgine.graphics.opengl.ShaderProgram;
import com.lukestadem.rendgine.graphics.opengl.VertexArrayObject;
import com.lukestadem.rendgine.graphics.opengl.VertexAttributes;
import com.lukestadem.rendgine.util.Disposable;

public class Mesh implements Disposable {
	
	private final VertexArrayObject vao;
	
	public final VertexAttributes attribs;
	
	public Mesh(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		attribs = new VertexAttributes(hasColors, hasNormals, hasTexCoords);
		
		vao = new VertexArrayObject(attribs, maxVertices, maxIndices);
	}
	
	public Mesh addVertex(float[] data){
		vao.addVertex(data);
		return this;
	}
	
	public Mesh addIndices(int... indices){
		vao.addIndices(indices);
		return this;
	}
	
	public void clearAllData(){
		vao.clearAllData();
	}
	
	public void resetOffsets(){
		vao.resetOffsets();
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
	
	/**
	 * A {@link ShaderProgram} must be bound before calling this method!
	 * See {@link #render(ShaderProgram, int)} for more details.
	 *
	 * @param primitive OpenGL render primitive type GL_*
	 */
	public void render(int primitive){
		render(null, primitive);
	}
	
	/**
	 * Renders this mesh. If {@code shader} is null, this method assumes that the
	 * shader will be managed externally (bound and unbound as necessary). Doing
	 * this can be useful when using multiple meshes or using the shader for other
	 * operations, in order to reduce multiple bind/unbind cycles.
	 *
	 * @param shader shader to be bound/unbound, can be null
	 * @param primitive OpenGL render primitive type GL_*
	 */
	public void render(ShaderProgram shader, int primitive){
		if(shader != null){
			shader.bind();
		}
		
		vao.bind();
		vao.render(primitive);
		vao.unbind();
		
		if(shader != null){
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
