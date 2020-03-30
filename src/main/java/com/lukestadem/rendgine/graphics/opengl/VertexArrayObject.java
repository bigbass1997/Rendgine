package com.lukestadem.rendgine.graphics.opengl;

import static org.lwjgl.opengl.GL46.*;

import com.lukestadem.rendgine.graphics.opengl.VertexAttributes.Usage;
import com.lukestadem.rendgine.util.Disposable;

public class VertexArrayObject implements Disposable {
	
	private final int name;
	
	private final VertexAttributes attribs;
	private final VertexBufferObject[] vbos;
	
	private final VertexBufferObject vboIndices;
	
	private boolean isBound;
	
	public VertexArrayObject(VertexAttributes attribs, int maxVertices, int maxIndices){
		this.attribs = attribs;
		
		vbos = new VertexBufferObject[attribs.length()];
		
		name = glGenVertexArrays();
		glBindVertexArray(name);
		
		int vboIndex = 0;
		final Usage[] usageTypes = Usage.values();
		for(Usage use : usageTypes){
			if(attribs.hasUsage(use)){
				vbos[vboIndex] = new VertexBufferObject(use, maxVertices, vboIndex);
				vboIndex++;
			}
		}
		
		vboIndices = new VertexBufferObject(Usage.INDICES, maxIndices, vboIndex);
		
		isBound = false;
	}
	
	public VertexArrayObject addVertex(float[] data){
		int offset = 0;
		for(VertexBufferObject vbo : vbos){
			final int usageOffset = vbo.getUsage().getOffset();
			for(int i = 0; i < usageOffset; i++){
				vbo.addData(data[offset + i]);
			}
			offset += usageOffset;
		}
		
		return this;
	}
	
	public VertexArrayObject addIndices(int... indices){
		vboIndices.addData(indices);
		return this;
	}
	
	public void clearAllData(){
		for(VertexBufferObject vbo : vbos){
			vbo.clearData();
		}
		vboIndices.clearData();
	}
	
	public int getVertexOffset(Usage use){
		int index = 0;
		for(VertexBufferObject vbo : vbos){
			if(use.equals(vbo.getUsage())){
				return index;
			}
			index += vbo.getUsage().getOffset();
		}
		
		return index;
	}
	
	public void resetOffsets(){
		for(VertexBufferObject vbo : vbos){
			vbo.resetOffset();
		}
		vboIndices.resetOffset();
	}
	
	public void bind(){
		glBindVertexArray(name);
		for(VertexBufferObject vbo : vbos){
			vbo.bind();
		}
		vboIndices.bind();
		
		isBound = true;
	}
	
	public void render(int primitive){
		if(!isBound){
			throw new RuntimeException("VertexArrayObject must be bound before rendering!");
		}
		
		if(vbos.length == 0 || (vboIndices.getOffset() == 0 && vbos[0].getOffset() == 0)){
			return;
		}
		
		if(vboIndices.getOffset() > 0){
			glDrawElements(primitive, vboIndices.getOffset(), GL_UNSIGNED_INT, 0);
		} else {
			glDrawArrays(primitive, 0, vbos[0].getVertices());
		}
	}
	
	public void unbind(){
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		isBound = false;
	}
	
	public boolean hasIndices(){
		return vboIndices.getOffset() > 0;
	}
	
	@Override
	public void dispose(){
		for(VertexBufferObject vbo : vbos){
			vbo.dispose();
		}
		vboIndices.dispose();
		
		glBindVertexArray(0);
		glDeleteVertexArrays(name);
	}
}
