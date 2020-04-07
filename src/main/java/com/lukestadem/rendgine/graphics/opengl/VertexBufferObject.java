package com.lukestadem.rendgine.graphics.opengl;

import com.lukestadem.rendgine.util.Disposable;

import java.util.Arrays;

import static org.lwjgl.opengl.GL46.*;

public class VertexBufferObject implements Disposable {
	
	public final int vboIndex;
	
	private final int name;
	private final VertexAttributes.Usage usage;
	
	private float[] data;
	
	private int offset;
	
	private boolean dirtyData;
	
	public VertexBufferObject(final VertexAttributes.Usage usage, final int maxVertices){
		this.usage = usage;
		vboIndex = usage.getShaderPosition();
		
		name = glGenBuffers();
		data = new float[maxVertices * usage.getOffset()];
		
		if(usage.equals(VertexAttributes.Usage.INDICES)){
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, name);
			glEnableVertexAttribArray(vboIndex);
			glVertexAttribPointer(vboIndex, usage.getOffset(), GL_INT, false, 0, 0);
		} else {
			glBindBuffer(GL_ARRAY_BUFFER, name);
			glEnableVertexAttribArray(vboIndex);
			glVertexAttribPointer(vboIndex, usage.getOffset(), GL_FLOAT, false, 0, 0);
		}
		
		offset = 0;
		
		dirtyData = true;
	}
	
	public int getName(){
		return name;
	}
	
	public VertexBufferObject setData(float[] data){
		this.data = data;
		offset = data.length;
		dirtyData = true;
		return this;
	}
	
	public VertexBufferObject addData(float... data){
		for(int i = 0; i < data.length; i++){
			this.data[offset + i] = data[i];
		}
		offset += data.length;
		dirtyData = true;
		return this;
	}
	
	public VertexBufferObject addData(int... data){
		for(int i = 0; i < data.length; i++){
			this.data[offset + i] = data[i];
		}
		offset += data.length;
		dirtyData = true;
		return this;
	}
	
	public VertexBufferObject clearData(){
		Arrays.fill(data, 0);
		offset = 0;
		dirtyData = true;
		return this;
	}
	
	public void resetOffset(){
		offset = 0;
	}
	
	public VertexAttributes.Usage getUsage(){
		return usage;
	}
	
	public void bind(){
		if(usage.equals(VertexAttributes.Usage.INDICES)){
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, name);
		} else {
			glBindBuffer(GL_ARRAY_BUFFER, name);
		}
		
		if(dirtyData){
			if(usage.equals(VertexAttributes.Usage.INDICES)){
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataToInt(), GL_DYNAMIC_DRAW);
			} else {
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
				//glBufferData(GL_ARRAY_BUFFER, ArrayUtils.subarray(data, 0, offset), GL_DYNAMIC_DRAW); // Increases RAM usage, but increases performance when the array is not being used to capacity.
				glVertexAttribPointer(vboIndex, usage.getOffset(), GL_FLOAT, false, 0, 0);
			}
			
			dirtyData = false;
		}
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int length(){
		return data.length;
	}
	
	public int getVertices(){
		return offset / usage.getOffset();
	}
	
	private int[] dataToInt(){
		final int[] arr = new int[data.length];
		for(int i = 0; i < data.length; i++){
			arr[i] = (int) data[i];
		}
		return arr;
	}
	
	@Override
	public void dispose(){
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(name);
	}
}
