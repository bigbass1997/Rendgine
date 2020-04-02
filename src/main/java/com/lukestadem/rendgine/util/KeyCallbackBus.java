package com.lukestadem.rendgine.util;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * See {@link }
 */
public class KeyCallbackBus extends GLFWKeyCallback {
	
	private static KeyCallbackBus globalInstance;
	
	private List<GLFWKeyCallbackI> callbacks;
	
	public KeyCallbackBus(){
		callbacks = new ArrayList<GLFWKeyCallbackI>();
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods){
		for(GLFWKeyCallbackI callback : callbacks){
			callback.invoke(window, key, scancode, action, mods);
		}
	}
	
	/**
	 * Adds a callback object to the bus.
	 *
	 * @param callback callback to be added
	 */
	public void add(GLFWKeyCallbackI callback){
		callbacks.add(callback);
	}
	
	/**
	 * Provides a global (static) instance of this class as an alternative to creating a new object and storing it
	 * elsewhere. Please note that calling this method will <b>not</b> set it as GLFW's key callback.
	 *
	 * @return static instance of this class
	 */
	public static KeyCallbackBus getGlobalBus(){
		if(globalInstance == null){
			globalInstance = new KeyCallbackBus();
		}
		
		return globalInstance;
	}
}
