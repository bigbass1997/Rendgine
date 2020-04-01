package com.lukestadem.rendgine.graphics;

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
}
