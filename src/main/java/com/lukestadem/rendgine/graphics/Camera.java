package com.lukestadem.rendgine.graphics;

import org.joml.*;
import org.joml.Math;

public class Camera {
	
	private final Vector3f tmpVec;
	private final Matrix3f tmpMat3;
	
	public final Vector3f position;
	public final Vector3f direction;
	public final Vector3f up;
	
	public final Matrix4f projection;
	public final Matrix4f view;
	public final Matrix4f combined;
	
	public float near;
	public float far;
	
	public float viewportWidth;
	public float viewportHeight;
	
	public Camera(){
		this(0, 0);
	}
	
	public Camera(float viewportWidth, float viewportHeight){
		tmpVec = new Vector3f();
		tmpMat3 = new Matrix3f();
		
		position = new Vector3f();
		direction = new Vector3f(0, 0, -1);
		up = new Vector3f(0, 1, 0);
		
		projection = new Matrix4f();
		view = new Matrix4f();
		combined = new Matrix4f();
		
		near = -1;
		far = 1;
		
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		
		update();
	}
	
	public Camera update(){
		projection.setOrtho(-(viewportWidth / 2), (viewportWidth / 2), -(viewportHeight / 2), (viewportHeight / 2), near, far);
		view.setLookAt(position, tmpVec.set(position).add(direction), up);
		combined.set(projection).mul(view);
		
		return this;
	}
	
	public Camera lookAt(Vector3f target){
		return lookAt(target.x, target.y, target.z);
	}
	
	public Camera lookAt(float x, float y, float z){
		tmpVec.set(x, y, z).sub(position).normalize();
		if(!tmpVec.equals(0, 0, 0)){
			final float dot = tmpVec.dot(up);
			if(Math.abs(dot - 1) < 0.000000001f){
				up.set(direction).mul(-1);
			} else if(Math.abs(dot + 1) < 0.000000001f){
				up.set(direction);
			}
			direction.set(tmpVec);
			normalizeUp();
		}
		
		return this;
	}
	
	public Camera normalizeUp(){
		tmpVec.set(direction).cross(up).normalize();
		up.set(tmpVec).cross(direction).normalize();
		
		return this;
	}
	
	public Camera rotate(Matrix4f transform){
		direction.mul(transform.get3x3(tmpMat3));
		up.mul(transform.get3x3(tmpMat3));
		
		return this;
	}
	
	public Camera rotate(float angle, Vector3f axis){
		return rotate(angle, axis.x, axis.y, axis.z);
	}
	
	public Camera rotate(float angle, float x, float y, float z){
		direction.rotateAxis(Math.toRadians(angle), x, y, z);
		up.rotateAxis(Math.toRadians(angle), x, y, z);
		
		return this;
	}
	
	public Camera rotate(Quaternionf quaternion){
		quaternion.transform(direction);
		quaternion.transform(up);
		
		return this;
	}
	
	public Camera transform(Matrix4f transform){
		position.mul(transform.get3x3(tmpMat3));
		rotate(transform);
		
		return this;
	}
	
	public Camera translate(Vector3f vec){
		return translate(vec.x, vec.y, vec.z);
	}
	
	public Camera translate(float x, float y, float z){
		position.add(x, y, z);
		
		return this;
	}
}
