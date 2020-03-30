package com.lukestadem.rendgine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
	
	private final Window window;
	
	private final List<Consumer<Engine>> updateTasks;
	private final List<Consumer<Engine>> renderTasks;
	
	private double delta;
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable){
		window = new Window(title, width, height, vSync, isResizable);
		
		updateTasks = new ArrayList<Consumer<Engine>>();
		renderTasks = new ArrayList<Consumer<Engine>>();
	}
	
	public void start(){
		try {
			loop();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		window.close();
		glfwTerminate();
	}
	
	protected void loop(){
		double lastFrameStartTime = getTime();
		while(!window.windowShouldClose()){
			delta = getTime() - lastFrameStartTime;
			lastFrameStartTime = getTime();
			
			update();
			render();
		}
	}
	
	protected void update(){
		window.update();
		
		updateTasks.forEach(task -> {
			task.accept(this);
		});
	}
	
	protected void render(){
		renderTasks.forEach(task -> {
			task.accept(this);
		});
	}
	
	private double getTime(){
		return System.nanoTime() / 1000000000d;
	}
	
	public double getDelta(){
		return delta;
	}
	
	public double getFPS(){
		return 1d / getDelta();
	}
	
	public void addUpdateTask(Consumer<Engine> task){
		updateTasks.add(task);
	}
	
	public void addRenderTask(Consumer<Engine> task){
		renderTasks.add(task);
	}
	
	public Window getWindow(){
		return window;
	}
}
