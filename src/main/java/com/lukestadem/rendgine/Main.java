package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.Camera;
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
		
		final int width = 700;
		final int height = 700;
		final Engine engine = new Engine("Test Title", width, height, false, false);
		final Camera cam = new Camera(width, height);
		cam.position.set((width / 2), (height / 2), 0);
		cam.update();
		
		final ModernImmediateRenderer2D mir = new ModernImmediateRenderer2D(3000000, 100000, true, false, false);
		
		final Random rand = new Random(59182495L);
		List<Particle> particles = new ArrayList<Particle>();
		
		engine.addRenderTask(p -> {
			mir.clear();
			
			particles.forEach(part -> {
				part.render(mir);
			});
			
			mir.render(cam.combined);
		});
		
		engine.addUpdateTask(p -> {
			final float INTERVAL = 0.01f;
			if(time > INTERVAL){
				final int n = (int) (time / INTERVAL);
				for(int i = 0; i < n; i++){
					particles.add(new Particle((rand.nextFloat() * width), (rand.nextFloat() * height), 5, 5, Color.toIntPack(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1)));
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
