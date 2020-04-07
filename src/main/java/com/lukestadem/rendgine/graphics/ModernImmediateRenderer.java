package com.lukestadem.rendgine.graphics;

import com.lukestadem.rendgine.graphics.camera.Camera;
import com.lukestadem.rendgine.graphics.opengl.ShaderProgram;
import com.lukestadem.rendgine.graphics.opengl.VertexAttributes.Usage;
import com.lukestadem.rendgine.util.Disposable;
import com.lukestadem.rendgine.util.Utils;
import org.joml.Matrix4f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL46.*;

public class ModernImmediateRenderer implements Disposable {
	
	private ShaderProgram shaderProgram;
	
	private Mesh mesh;
	private float[] nextVertex;
	
	private Color tmpColor;
	
	protected boolean resetColorFlag;
	
	public ModernImmediateRenderer(boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		this(50000, 50000, hasColors, hasNormals, hasTexCoords);
	}
	public ModernImmediateRenderer(int maxVertices, int maxIndices, boolean hasColors, boolean hasNormals, boolean hasTexCoords){
		try {
			shaderProgram = new ShaderProgram();
			shaderProgram.createVertexShader(Utils.loadResource("/shaders/immediate-vertex.glsl"));
			shaderProgram.createFragmentShader(Utils.loadResource("/shaders/immediate-fragment.glsl"));
			shaderProgram.link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mesh = new Mesh(maxVertices, maxIndices, hasColors, hasNormals, hasTexCoords);
		nextVertex = new float[mesh.getVertexSize()];
		
		tmpColor = new Color();
		
		resetColorFlag = false;
	}
	
	public void render(Camera cam){
		render(cam.combined);
	}
	
	public void render(Matrix4f combined){
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		mesh.render(shaderProgram, false, GL_TRIANGLES, combined);
		
		glDisable(GL_BLEND);
	}
	
	public ModernImmediateRenderer color(int colorPack){
		return color(tmpColor.setColor(colorPack));
	}
	
	public ModernImmediateRenderer color(Color color){
		return color(color.r, color.g, color.b, color.a);
	}
	
	public ModernImmediateRenderer color(float r, float g, float b, float a){
		if(mesh.hasUsage(Usage.COLORS)){
			final int offset = mesh.getVertexOffset(Usage.COLORS);
			nextVertex[offset] = r;
			nextVertex[offset+1] = g;
			nextVertex[offset+2] = b;
			nextVertex[offset+3] = a;
		}
		return this;
	}
	
	public ModernImmediateRenderer normal(float x, float y, float z){
		if(mesh.hasUsage(Usage.NORMALS)){
			final int offset = mesh.getVertexOffset(Usage.NORMALS);
			nextVertex[offset] = x;
			nextVertex[offset + 1] = y;
			nextVertex[offset + 2] = z;
		}
		return this;
	}
	
	public ModernImmediateRenderer texCoord(float u, float v){
		if(mesh.hasUsage(Usage.TEXCOORDS)){
			final int offset = mesh.getVertexOffset(Usage.TEXCOORDS);
			nextVertex[offset] = u;
			nextVertex[offset + 1] = v;
		}
		return this;
	}
	
	/**
	 * <p>Pushes a new vertex to the renderer's internal mesh.</p>
	 *
	 * <p>If {@code color()}, {@code texCoord()}, and/or {@code normal()} are to be used
	 * for this vertex, they <b>must</b> be called before this method!</p>
	 *
	 * @param x x-coord of vertex
	 * @param y y-coord of vertex
	 * @param z z-coord of vertex
	 * @return renderer for chaining
	 */
	public ModernImmediateRenderer vertex(float x, float y, float z){
		nextVertex[0] = x;
		nextVertex[1] = y;
		nextVertex[2] = z;
		mesh.addVertex(nextVertex);
		
		if(!resetColorFlag && mesh.hasUsage(Usage.COLORS)){
			test();
		} else {
			Arrays.fill(nextVertex, 0);
		}
		
		return this;
	}
	
	private void test(){
		final int offset = mesh.getVertexOffset(Usage.COLORS);
		final float r = nextVertex[offset];
		final float g = nextVertex[offset+1];
		final float b = nextVertex[offset+2];
		final float a = nextVertex[offset+3];
		Arrays.fill(nextVertex, 0);
		nextVertex[offset] = r;
		nextVertex[offset+1] = g;
		nextVertex[offset+2] = b;
		nextVertex[offset+3] = a;
	}
	
	public ModernImmediateRenderer indices(int[] indices){
		mesh.addIndices(indices);
		
		return this;
	}
	
	/**
	 * Clears all vertex and index data from renderer's internal mesh. This will often be
	 * used at the start of each frame, but it is <i>not</i> performed automatically by
	 * this renderer. It is up for the class using this object to decide when to clear.
	 *
	 * @return renderer for chaining
	 */
	public ModernImmediateRenderer clear(){
		mesh.clearAllData();
		Arrays.fill(nextVertex, 0);
		
		return this;
	}
	
	public ModernImmediateRenderer resetOffsets(){
		mesh.resetOffsets();
		return this;
	}
	
	/**
	 * When set to true, {@link #vertex(float, float, float)} will clear the color data.
	 * When false (default), the method clears all vertex data except for the color.
	 *
	 * @param flag should color be cleared when drawing a vertex
	 */
	public void setResetColorFlag(boolean flag){
		resetColorFlag = flag;
	}
	
	@Override
	public void dispose(){
		mesh.dispose();
	}
}
