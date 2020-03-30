package com.lukestadem.rendgine.graphics.opengl;

import org.apache.commons.lang3.ArrayUtils;

public class VertexAttributes {
	
	public enum Usage {
		POSITIONS(3), COLORS(4), NORMALS(3), TEXCOORDS(2), INDICES(3);
		
		private final int offset;
		
		Usage(int offset){
			this.offset = offset;
		}
		
		public int getOffset(){
			return offset;
		}
	}
	
	private Usage[] usages;
	
	private int vertexSize;
	
	public VertexAttributes(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		this(true, hasColors, hasNormals, hasTexCoords);
	}
	
	public VertexAttributes(boolean hasPositions, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		usages = new Usage[0];
		if(hasPositions) usages = ArrayUtils.add(usages, Usage.POSITIONS);
		if(hasColors) usages = ArrayUtils.add(usages, Usage.COLORS);
		if(hasNormals) usages = ArrayUtils.add(usages, Usage.NORMALS);
		if(hasTexCoords) usages = ArrayUtils.add(usages, Usage.TEXCOORDS);
		
		vertexSize = 0;
		for(Usage usage : usages){
			vertexSize += usage.offset;
		}
	}
	
	public VertexAttributes(Usage... usages){
		this.usages = usages;
	}
	
	public int getOffset(Usage usage){
		return getOffset(usage, -1);
	}
	public int getOffset(Usage usage, int defaultNotFound){
		int offset = 0;
		for(Usage use : usages){
			if(use.equals(usage)){
				return offset;
			}
			offset += use.offset;
		}
		
		if(ArrayUtils.contains(usages, usage)){
			return usage.getOffset();
		}
		
		return defaultNotFound;
	}
	
	public boolean hasUsage(Usage use){
		return ArrayUtils.contains(usages, use);
	}
	
	public int getVertexSize(){
		return vertexSize;
	}
	
	public Usage[] getUsages(){
		return usages;
	}
	
	public int length(){
		return usages.length;
	}
}
