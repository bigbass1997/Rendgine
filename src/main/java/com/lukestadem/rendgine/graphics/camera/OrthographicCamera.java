package com.lukestadem.rendgine.graphics.camera;

import com.lukestadem.rendgine.Engine;
import com.lukestadem.rendgine.util.Keyboard;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class OrthographicCamera extends Camera {
	
	private float viewportWidth;
	private float viewportHeight;
	
	public OrthographicCamera(float viewportWidth, float viewportHeight, float camPosX, float camPosY, float camPosZ){
		super(camPosX, camPosY, camPosZ);
		
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		
		update();
	}
	
	@Override
	public Camera update(){
		projection.setOrtho(-(viewportWidth / 2), (viewportWidth / 2), -(viewportHeight / 2), (viewportHeight / 2), near, far);
		view.setLookAt(position, tmpVec.set(position).add(direction), up);
		combined.set(projection).mul(view);
		
		return this;
	}
	
	@Override
	public void defaultCameraMovement(Engine engine){
		boolean camDirty = false;
		if(Keyboard.isKeyPressed(engine.getWindow().getWindowIndex(), GLFW_KEY_UP)){
			translate(0, (float) (200 * engine.getDelta()), 0);
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.getWindow().getWindowIndex(), GLFW_KEY_DOWN)){
			translate(0, (float) -(200 * engine.getDelta()), 0);
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.getWindow().getWindowIndex(), GLFW_KEY_LEFT)){
			translate((float) -(200 * engine.getDelta()), 0, 0);
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.getWindow().getWindowIndex(), GLFW_KEY_RIGHT)){
			translate((float) (200 * engine.getDelta()), 0, 0);
			camDirty = true;
		}
		
		if(camDirty){
			update();
		}
	}
	
	@Override
	public boolean isInView(float x, float y, float z, float edgeBuffer){
		final float maxX = position.x + (viewportWidth / 2) + edgeBuffer;
		final float minX = position.x - (viewportWidth / 2) - edgeBuffer;
		final float maxY = position.y + (viewportHeight / 2) + edgeBuffer;
		final float minY = position.y - (viewportHeight / 2) - edgeBuffer;
		
		return !(x > maxX || x < minX || y > maxY || y < minY);
	}
}
