package com.lukestadem.rendgine.util;

import org.lwjgl.glfw.GLFW;

public class Keyboard {
	
	public static boolean isKeyPressed(long window, int key){
		return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
	}
	
	public static boolean isKeyReleased(long window, int key){
		return GLFW.glfwGetKey(window, key) == GLFW.GLFW_RELEASE;
	}
}
