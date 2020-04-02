package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.camera.Camera;
import com.lukestadem.rendgine.graphics.camera.OrthographicCamera;
import com.lukestadem.rendgine.util.KeyCallbackBus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
	
	private final Window window;
	
	private final List<Consumer<Engine>> updateTasks;
	private final List<Consumer<Engine>> renderTasks;
	
	private double delta;
	
	public Camera camera;
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable){
		this(title, width, height, vSync, isResizable, new OrthographicCamera(width, height, (width / 2), (height / 2), 0));
	}
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable, Camera camera){
		window = new Window(title, width, height, vSync, isResizable);
		
		updateTasks = new ArrayList<Consumer<Engine>>();
		renderTasks = new ArrayList<Consumer<Engine>>();
		
		this.camera = camera;
		
		GLFW.glfwSetKeyCallback(window.getWindowIndex(), KeyCallbackBus.getGlobalBus());
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
	
	public void runDefaultCameraMovement(){
		camera.defaultCameraMovement(this);
	}
	
	public void addKeyCallback(GLFWKeyCallbackI callback){
		KeyCallbackBus.getGlobalBus().add(callback);
	}
	
	public Window getWindow(){
		return window;
	}
}
