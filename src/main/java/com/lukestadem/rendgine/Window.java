package com.lukestadem.rendgine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private long windowIndex;
	
	private int width;
	private int height;
	
	private boolean hasResized;
	
	public Window(String title, final int width, final int height, final boolean vSync, final boolean isResizable){
		this.width = width;
		this.height = height;
		
		hasResized = false;
		
		if(title == null){
			title = "";
		}
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit()){
			throw new IllegalStateException("Unable to initialize GLFW!");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, isResizable ? 1 : 0);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		windowIndex = glfwCreateWindow(width, height, title, NULL, NULL);
		if(windowIndex == NULL){
			throw new RuntimeException("Failed to create the GLFW window!");
		}
		
		// If window resized, run this
		glfwSetFramebufferSizeCallback(windowIndex, (window, cbWidth, cbHeight) -> {
			this.width = cbWidth;
			this.height = cbHeight;
			
			this.hasResized = true;
		});
		
		glfwSetKeyCallback(windowIndex, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
				close();
            }
		});
		
		final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if(vidMode != null){
			glfwSetWindowPos(windowIndex,(vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
		}
		
		glfwMakeContextCurrent(windowIndex);
		
		if(vSync){
			glfwSwapInterval(1);
		}
		
		GL.createCapabilities();
		
		glClearColor(0, 0, 0, 1);
		
		glfwShowWindow(windowIndex);
		
		glViewport(0, 0, width, height);
	}
	
	public void setClearColor(float r, float g, float b, float a){
		glClearColor(r, g, b, a);
	}
	
	public boolean isKeyPressed(int keyCode){
		return glfwGetKey(windowIndex, keyCode) == GLFW_PRESS;
	}
	
	public void update(){
		glfwSwapBuffers(windowIndex);
		glfwPollEvents();
	}
	
	public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowIndex);
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public long getWindowIndex(){
		return windowIndex;
	}
	
	public boolean hasResized(){
		return hasResized;
	}
	
	public void hasResized(boolean hasResized){
		this.hasResized = hasResized;
	}
	
	public void close(){
		glfwDestroyWindow(windowIndex);
	}
}
