package com.lukestadem.rendgine.graphics;

import com.lukestadem.rendgine.graphics.opengl.ShaderProgram;
import com.lukestadem.rendgine.graphics.opengl.Texture;
import com.lukestadem.rendgine.graphics.opengl.VertexAttributes;
import com.lukestadem.rendgine.util.Disposable;
import com.lukestadem.rendgine.util.Utils;
import org.joml.Matrix4f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL46.*;

public class TextureRenderer implements Disposable { // AKA SpriteBatch aka thing that holds a mesh
	
	private ShaderProgram shaderProgram;
	
	private Mesh mesh;
	private float[] nextVertex;
	private int pushes;
	
	private boolean isOurOwnShader;
	
	private Matrix4f tmpCombined;
	
	private Texture lastTexture;
	
	public TextureRenderer(){
		this(null);
	}
	
	public TextureRenderer(ShaderProgram program){
		if(program == null){
			try {
				shaderProgram = new ShaderProgram();
				shaderProgram.createVertexShader(Utils.loadResource("/shaders/texture-vertex.glsl"));
				shaderProgram.createFragmentShader(Utils.loadResource("/shaders/texture-fragment.glsl"));
				shaderProgram.link();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isOurOwnShader = true;
		} else {
			if(!program.isLinked()){
				try {
					program.link();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			shaderProgram = program;
			isOurOwnShader = false;
		}
		
		mesh = new Mesh(6000, 0, true, false, true);
		nextVertex = new float[mesh.getVertexSize()];
		pushes = 0;
	}
	
	public void begin(Matrix4f combined){
		glDepthMask(false);
		
		tmpCombined = combined;
		
		shaderProgram.bind();
	}
	
	public TextureRenderer vertex(float x, float y){
		return vertex(x, y, 0);
	}
	
	/**
	 * Only use if you know what you're doing! This method is meant for internal use, but is available
	 * for advanced uses.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 * @return this renderer for chaining
	 */
	public TextureRenderer vertex(float x, float y, float z){
		nextVertex[0] = x;
		nextVertex[1] = y;
		nextVertex[2] = z;
		
		mesh.addVertex(nextVertex);
		Arrays.fill(nextVertex, 0);
		
		pushes++;
		
		return this;
	}
	
	public TextureRenderer color(float r, float g, float b, float a){
		final int offset = mesh.getVertexOffset(VertexAttributes.Usage.COLORS);
		nextVertex[offset] = r;
		nextVertex[offset + 1] = g;
		nextVertex[offset + 2] = b;
		nextVertex[offset + 3] = a;
		
		return this;
	}
	
	/**
	 * Only use if you know what you're doing! This method is meant for internal use, but is available
	 * for advanced uses.
	 * 
	 * @param u u-texture-coordinate
	 * @param v v-texture-coordinate
	 * @return this renderer for chaining
	 */
	public TextureRenderer texCoord(float u, float v){
		final int offset = mesh.getVertexOffset(VertexAttributes.Usage.TEXCOORDS);
		nextVertex[offset] = u;
		nextVertex[offset + 1] = v;
		
		return this;
	}
	
	public TextureRenderer texture(Texture tex, float x, float y, float width, float height){
		if(lastTexture == null || !lastTexture.equals(tex)){
			flush();
			lastTexture = tex;
		} else if(pushes >= mesh.getMaxVertices()){
			flush();
		}
		
		texCoord(0, 0);
		vertex(x, y, 0);
		
		texCoord(0, 1);
		vertex(x, y + height, 0);
		
		texCoord(1, 1);
		vertex(x + width, y + height, 0);
		
		texCoord(1, 1);
		vertex(x + width, y + height, 0);
		
		texCoord(1, 0);
		vertex(x + width, y, 0);
		
		texCoord(0, 0);
		vertex(x, y, 0);
		
		return this;
	}
	
	private void flush(){
		if(pushes == 0){
			return;
		}
		
		lastTexture.bind();
		
		shaderProgram.setUniform("textureSampler", 0);
		mesh.render(shaderProgram, true, GL_TRIANGLES, tmpCombined);
		mesh.clearAllData();
		pushes = 0;
	}
	
	public void end(){
		if(pushes > 0){
			flush();
		}
		
		shaderProgram.unbind();
	}
	
	@Override
	public void dispose(){
		if(isOurOwnShader){
			shaderProgram.dispose();
		}
	}
}
