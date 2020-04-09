package com.lukestadem.rendgine.graphics;

import com.lukestadem.rendgine.graphics.opengl.ShaderProgram;
import com.lukestadem.rendgine.graphics.opengl.Texture;
import com.lukestadem.rendgine.graphics.opengl.TextureRegion;
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
	private boolean isDrawing;
	
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
		
		isDrawing = false;
		
		mesh = new Mesh(6000, 0, true, false, true);
		nextVertex = new float[mesh.getVertexSize()];
		pushes = 0;
	}
	
	public void begin(Matrix4f combined){
		isDrawing = true;
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
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
	
	public TextureRenderer texture(Texture tex, float x, float y){
		return texture(tex, x, y, tex.width, tex.height);
	}
	
	public TextureRenderer texture(Texture tex, float x, float y, float width, float height){
		return texture(tex, x, y, width, height, 0, 0, 1, 1);
	}
	
	public TextureRenderer texture(Texture tex, float x, float y, float width, float height, float u, float v, float u2, float v2){
		if(lastTexture == null || !lastTexture.equals(tex)){
			flush();
			lastTexture = tex;
		} else if(pushes >= mesh.getMaxVertices()){
			flush();
		}
		
		texCoord(u, v);
		vertex(x, y, 0);
		
		texCoord(u, v2);
		vertex(x, y + height, 0);
		
		texCoord(u2, v2);
		vertex(x + width, y + height, 0);
		
		texCoord(u2, v2);
		vertex(x + width, y + height, 0);
		
		texCoord(u2, v);
		vertex(x + width, y, 0);
		
		texCoord(u, v);
		vertex(x, y, 0);
		
		return this;
	}
	
	public TextureRenderer texture(TextureRegion tex, float x, float y){
		return texture(tex, x, y, tex.width, tex.height);
	}
	
	public TextureRenderer texture(TextureRegion tex, float x, float y, float width, float height){
		return texture(tex.texture, x, y, width, height, tex.u, tex.v, tex.u2, tex.v2);
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
		
		glDisable(GL_BLEND);
		
		isDrawing = false;
	}
	
	public boolean isDrawing(){
		return isDrawing;
	}
	
	@Override
	public void dispose(){
		if(isOurOwnShader){
			shaderProgram.dispose();
		}
	}
}
