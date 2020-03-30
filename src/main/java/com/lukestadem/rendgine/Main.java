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
		
		final int width = 1400;
		final int height = 700;
		final Engine engine = new Engine("Test Title", width, height, false, false);
		
		final ModernImmediateRenderer2D mir = new ModernImmediateRenderer2D(3000000, 100000, true, false, false);
		
		/*final float[] positions = new float[]{
			-0.5f,  0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f,
		};
		final float[] colors = new float[]{
			0.5f, 0.0f, 0.0f, 1.0f,
			0.0f, 0.5f, 0.0f, 1.0f,
			0.0f, 0.0f, 0.5f, 1.0f,
			0.0f, 0.5f, 0.5f, 1.0f,
		};
		final int[] indices = new int[20000 * 3];
		for(int i = 0; i < indices.length; i += 6){
			indices[i] = 0;
			indices[i+1] = 1;
			indices[i+2] = 3;
			indices[i+3] = 3;
			indices[i+4] = 1;
			indices[i+5] = 2;
		}*/
		
		final Random rand = new Random(59182495L);
		/*for(int n = 0; n < 100000; n++){
			for(int i = 0; i < positions.length / 3; i++){
				mir.color(colors[i*4], colors[(i*4)+1], colors[(i*4)+2], colors[(i*4)+3]);
				
				final float x = (rand.nextFloat() * 200) - 100;
				final float y = (rand.nextFloat() * 200) - 100;
				//mir.vertex(positions[i*3], positions[(i*3)+1], positions[(i*3)+2]);
				mir.vertex(x, y, 0.0f);
			}
		}*/
		
		
		/*for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				mir.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
				mir.vertex(i+0.1f, j, 0);
				mir.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
				mir.vertex(i-0.1f, j, 0);
				mir.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
				mir.vertex(i, j-0.1f, 0);
				count++;
			}
		}*/
		
		/*for(int i = 0; i < positions.length / 3; i++){
			mir.color(colors[i*4], colors[(i*4)+1], colors[(i*4)+2], colors[(i*4)+3]);
			mir.vertex(positions[i*3], positions[(i*3)+1], positions[(i*3)+2]);
		}
		mir.indices(indices);*/
		
		List<Particle> particles = new ArrayList<Particle>();
		
		engine.addRenderTask(p -> {
			mir.clear();
			
			particles.forEach(part -> {
				part.render(mir);
			});
			
			mir.render();
		});
		
		engine.addUpdateTask(p -> {
			/*if(count > p.getFPS() * 1.5f){
				mir.clear();
				for(int n = 0; n < 100000; n++){
					for(int i = 0; i < positions.length / 3; i++){
						mir.color(colors[i*4], colors[(i*4)+1], colors[(i*4)+2], colors[(i*4)+3]);
						
						final float x = (rand.nextFloat() * 2) - 1;
						final float y = (rand.nextFloat() * 2) - 1;
						//mir.vertex(positions[i*3], positions[(i*3)+1], positions[(i*3)+2]);
						mir.vertex(x, y, 0.0f);
					}
				}
				count = 0;
			}
			count += 1000 * p.getDelta();*/
			
			final float INTERVAL = 0.01f;
			if(time > INTERVAL){
				final int n = (int) (time / INTERVAL);
				for(int i = 0; i < n; i++){
					particles.add(new Particle((rand.nextFloat() * 2) - 1, (rand.nextFloat() * 2) - 1, 0.015f, 0.015f, Color.toIntPack(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1)));
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
