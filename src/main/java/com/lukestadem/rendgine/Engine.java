package com.lukestadem.rendgine;

import com.lukestadem.rendgine.graphics.camera.Camera;
import com.lukestadem.rendgine.graphics.camera.OrthographicCamera;
import com.lukestadem.rendgine.util.KeyCallbackBus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
	
	private final List<EngineTask> tasks;
	
	private double delta;
	
	public final Window window;
	public final Camera camera;
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable){
		this(title, width, height, vSync, isResizable, 0);
	}
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable, final int sampleMsaa){
		this(title, width, height, vSync, isResizable, new OrthographicCamera(width, height, (width / 2), (height / 2), 0), sampleMsaa);
	}
	
	public Engine(final String title, final int width, final int height, final boolean vSync, final boolean isResizable, Camera camera, final int sampleMsaa){
		window = new Window(title, width, height, vSync, isResizable, sampleMsaa);
		
		tasks = new ArrayList<EngineTask>();
		
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
			
			window.update();
			for(EngineTask task : tasks){
				task.loop(this);
			}
		}
	}
	
	private double getTime(){
		return System.nanoTime() / 1000000000d;
	}
	
	public double getDelta(){
		return delta;
	}
	
	/**
	 * Less accurate version than {@link #getDelta()}.
	 * 
	 * @return delta time as a float
	 */
	public float getDeltaF(){
		return (float) getDelta();
	}
	
	public double getFPS(){
		return 1d / getDelta();
	}
	
	public void addTask(EngineTask task){
		if(task == null){
			return;
		}
		
		tasks.add(task);
	}
	
	public void removeTask(EngineTask task){
		if(task == null){
			return;
		}
		
		tasks.remove(task);
	}
	
	public void runDefaultCameraMovement(){
		camera.defaultCameraMovement(this);
	}
	
	public void addKeyCallback(GLFWKeyCallbackI callback){
		KeyCallbackBus.getGlobalBus().add(callback);
	}
}
