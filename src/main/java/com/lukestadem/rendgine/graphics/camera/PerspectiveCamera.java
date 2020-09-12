package com.lukestadem.rendgine.graphics.camera;

import com.lukestadem.rendgine.Engine;
import com.lukestadem.rendgine.util.Keyboard;
import org.joml.Math;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PerspectiveCamera extends Camera {
	
	private float fov;
	private float aspect;
	
	private Vector3f tmpVec;
	
	public PerspectiveCamera(float fov, float width, float height){
		this(fov, width / height);
	}
	
	public PerspectiveCamera(float fov, float aspect){
		this(fov, aspect, 0, 0, 0);
	}
	
	public PerspectiveCamera(float fov, float width, float height, float camPosX, float camPosY, float camPosZ){
		this(fov, width / height, camPosX, camPosY, camPosZ);
	}
	
	public PerspectiveCamera(float fov, float aspect, float camPosX, float camPosY, float camPosZ){
		super(camPosX, camPosY, camPosZ);
		
		this.fov = fov;
		this.aspect = aspect;
		
		near = 1;
		far = 100;
		
		tmpVec = new Vector3f();
		
		update();
	}
	
	@Override
	public Camera update(){
		projection.setPerspective(Math.toRadians(fov), aspect, near, far);
		view.setLookAt(position, tmpVec.set(position).add(direction), up);
		combined.set(projection).mul(view);
		
		return this;
	}
	
	@Override
	public void defaultCameraMovement(Engine engine){
		boolean camDirty = false;
		
		final float speed = (float) (200 * engine.getDelta());
		final float angSpeed = Math.toRadians( (float) (80 * engine.getDelta()) );
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_W)){
			tmpVec.set(direction);
			position.add(tmpVec.normalize(speed));
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_S)){
			tmpVec.set(direction);
			position.add(tmpVec.normalize(speed).negate());
			camDirty = true;
		}
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_A)){
			tmpVec.set(up);
			position.add(tmpVec.cross(direction).normalize(speed));
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_D)){
			tmpVec.set(up);
			position.add(tmpVec.cross(direction).normalize(speed).negate());
			camDirty = true;
		}
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_SPACE)){
			tmpVec.set(up);
			position.add(tmpVec.normalize(speed));
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_LEFT_SHIFT)){
			tmpVec.set(up);
			position.add(tmpVec.normalize(speed).negate());
			camDirty = true;
		}
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_UP)){
			tmpVec.set(up).cross(direction);
			direction.rotateAxis(-angSpeed, tmpVec.x, tmpVec.y, tmpVec.z);
			normalizeUp();
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_DOWN)){
			tmpVec.set(up).cross(direction);
			direction.rotateAxis(angSpeed, tmpVec.x, tmpVec.y, tmpVec.z);
			normalizeUp();
			camDirty = true;
		}
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_LEFT)){
			direction.rotateAxis(angSpeed, up.x, up.y, up.z);
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_RIGHT)){
			direction.rotateAxis(-angSpeed, up.x, up.y, up.z);
			camDirty = true;
		}
		
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_Q)){
			up.rotateAxis(-angSpeed, direction.x, direction.y, direction.z);
			camDirty = true;
		}
		if(Keyboard.isKeyPressed(engine.window.getWindowIndex(), GLFW_KEY_E)){
			up.rotateAxis(angSpeed, direction.x, direction.y, direction.z);
			camDirty = true;
		}
		
		if(camDirty){
			update();
		}
	}
	
	@Override
	public boolean isInView(float x, float y, float z, float edgeBuffer){
		return true; //TODO implement method
	}
}
