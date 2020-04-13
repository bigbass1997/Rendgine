package com.lukestadem.rendgine.graphics.opengl;

public class VertexAttributes {
	
	public enum Usage {
		POSITIONS(0, 3), COLORS(1, 4), NORMALS(2, 3), TEXCOORDS(3, 2), INDICES(4, 3);
		
		private final int position;
		private final int offset;
		
		Usage(int position, int offset){
			this.position = position;
			this.offset = offset;
		}
		
		public int getShaderPosition(){
			return position;
		}
		
		public int getOffset(){
			return offset;
		}
	}
	
	private boolean hasPositions;
	private boolean hasColors;
	private boolean hasNormals;
	private boolean hasTexCoords;
	
	private int vertexSize;
	
	public VertexAttributes(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		this(true, hasColors, hasNormals, hasTexCoords);
	}
	
	public VertexAttributes(boolean hasPositions, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		vertexSize = 0;
		if(hasPositions){
			this.hasPositions = true;
			vertexSize += Usage.POSITIONS.getOffset();
		}
		if(hasColors){
			this.hasColors = true;
			vertexSize += Usage.COLORS.getOffset();
		}
		if(hasNormals){
			this.hasNormals = true;
			vertexSize += Usage.NORMALS.getOffset();
		}
		if(hasTexCoords){
			this.hasTexCoords = true;
			vertexSize += Usage.TEXCOORDS.getOffset();
		}
	}
	
	public int getOffset(Usage usage){
		return getOffset(usage, 0);
	}
	
	public int getOffset(Usage usage, int defaultNotFound){
		int offset = 0;
		if(hasPositions){
			if(usage == Usage.POSITIONS){
				return offset;
			}
			offset += usage.getOffset();
		}
		if(hasColors){
			if(usage == Usage.COLORS){
				return offset;
			}
			offset += usage.getOffset();
		}
		if(hasNormals){
			if(usage == Usage.NORMALS){
				return offset;
			}
			offset += usage.getOffset();
		}
		if(hasTexCoords){
			if(usage == Usage.TEXCOORDS){
				return offset;
			}
		}
		
		return defaultNotFound;
	}
	
	public boolean hasUsage(Usage use){
		if(use == Usage.POSITIONS && hasPositions){
			return true;
		}
		if(use == Usage.COLORS && hasColors){
			return true;
		}
		if(use == Usage.NORMALS && hasNormals){
			return true;
		}
		if(use == Usage.TEXCOORDS && hasTexCoords){
			return true;
		}
		
		return false;
	}
	
	public int getVertexSize(){
		return vertexSize;
	}
	
	public int length(){
		int len = 0;
		if(hasPositions){
			len++;
		}
		if(hasColors){
			len++;
		}
		if(hasNormals){
			len++;
		}
		if(hasTexCoords){
			len++;
		}
		
		return len;
	}
}
