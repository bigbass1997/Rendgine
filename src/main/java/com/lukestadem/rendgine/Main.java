package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.Color;
import com.lukestadem.rendgine.graphics.ModernImmediateRenderer2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	
	//public static float count;
	public static int count = 0;
	public static float time = 0;
	
	public static void main(String[] args) {
		
		final int width = 500;
		final int height = 500;
		final Engine engine = new Engine("Test Title", width, height, false, false);
		
		final int maxVertices = 300000;
		final ModernImmediateRenderer2D mir = new ModernImmediateRenderer2D(maxVertices, 100000, true, false, false);
		
		final Random rand = new Random();
		List<Particle> particles = new ArrayList<Particle>();
		
		engine.addRenderTask(p -> {
			mir.clear();
			
			particles.forEach(part -> {
				part.pos.add((rand.nextFloat() * 2) - 1, (rand.nextFloat() * 2) - 1);
				
				if(p.camera.isInView(part.pos.x, part.pos.y, 0, 3)){
					part.render(mir);
				}
			});
			
			//mir.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
			//mir.polygonTriangulated(new float[]{100, 100, 100, 200, 200, 200, 200, 100, 150, 50, 155, 165});
			
			mir.render(p.camera.combined);
		});
		
		engine.addUpdateTask(p -> {
			p.runDefaultCameraMovement();
			
			final float INTERVAL = 0.0005f;
			if(time > INTERVAL){
				final int n = (int) (time / INTERVAL);
				for(int i = 0; i < n; i++){
					if(particles.size() < maxVertices / 6){
						particles.add(new Particle((rand.nextFloat() * width), (rand.nextFloat() * height), 4, 4, Color.toIntPack(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1)));
					}
				}
				time = time % INTERVAL;
			}
			time += engine.getDelta();
			
			System.out.println(particles.size() + ", " + p.getDelta() + ", " + p.getFPS());
		});
		
		engine.start();
		
		mir.dispose();
	}
}
