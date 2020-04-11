package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.Color;
import com.lukestadem.rendgine.graphics.ModernImmediateRenderer2D;
import com.lukestadem.rendgine.graphics.font.BitmapFont;
import com.lukestadem.rendgine.graphics.font.FontManager;
import com.lukestadem.rendgine.graphics.opengl.TextureRegion;
import com.lukestadem.rendgine.graphics.TextureRenderer;
import com.lukestadem.rendgine.graphics.opengl.Texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Main {
	
	//public static float count;
	public static int count = 0;
	public static float time = 0;
	
	public static void main(String[] args) {
		
		final int width = 500;
		final int height = 500;
		final Engine engine = new Engine("Test Title", width, height, false, false);
		
		final int maxVertices = 100000;
		final ModernImmediateRenderer2D mir = new ModernImmediateRenderer2D(maxVertices, 20000, true, false, true);
		
		final TextureRenderer tr = new TextureRenderer();
		final Texture tex = new Texture("test.png");
		final TextureRegion reg1 = new TextureRegion(tex, 0.5f, 0.5f, 1f, 1f);
		
		final FontManager fm = new FontManager();
		final BitmapFont font = fm.loadFont("consolas12", "fonts/consolas-12-native.png", "fonts/consolas-12-native.fnt");
		font.letterSpacing = 1;
		
		final Color testColor = new Color(0.5f, 1, 1, 0.7f);
		engine.addRenderTask(p -> {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			/*mir.clear();
			
			mir.color(1, 1, 0, 0.5f);
			mir.rect(50, 50, 500, 100);
			
			mir.render(p.camera.combined);*/
			
			tr.begin(p.camera.combined);
			tr.texture(tex, 10, 100, tex.width, tex.height, testColor);
			tr.texture(reg1, 142, 100, reg1.width, reg1.height);
			tr.texture(reg1, 274, 100, tex.width, tex.height);
			
			font.render(tr, String.format("%.15f", p.getDelta()), 5, height - 20);
			font.render(tr, String.format("%.2f", p.getFPS()), 5, height - 32);
			
			font.render(tr, String.format("%.15f", p.getDelta()), p.camera.position.x - (width * 0.5f) + 5, p.camera.position.y + (height * 0.5f) - 20, 0.67843137f, 0.49019607f, 1, 1);
			font.render(tr, String.format("%.2f", p.getFPS()), p.camera.position.x - (width * 0.5f) + 5, p.camera.position.y + (height * 0.5f) - 32, 0.67843137f, 0.49019607f, 1, 1);
			
			tr.end();
			
			/*mir.clear();
			
			mir.color(0, 1, 0, 0.5f);
			mir.rect(50, 110, 500, 30);
			mir.color(0, 1, 0, 0.5f);
			mir.rect(50, 95, 500, 30);
			
			mir.render(p.camera.combined);*/
		});
		
		engine.addUpdateTask(p -> {
			p.runDefaultCameraMovement();
			
			
			//System.out.println(p.getDelta() + ", " + p.getFPS());
		});
		
		engine.start();
		
		mir.dispose();
		tr.dispose();
		tex.dispose();
	}
}
