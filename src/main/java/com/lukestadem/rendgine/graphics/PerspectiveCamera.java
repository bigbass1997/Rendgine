package com.lukestadem.rendgine.graphics;

public class PerspectiveCamera extends Camera {
	
	private float fov;
	private float aspect;
	
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
		
		far = 100;
		
		update();
	}
	
	@Override
	public Camera update(){
		projection.setPerspective((float) Math.toRadians(fov), aspect, near, far);
		view.setLookAt(position, tmpVec.set(position).add(direction), up);
		combined.set(projection).mul(view);
		
		return this;
	}
}
