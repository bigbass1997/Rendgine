#version 460

vec4 enc;
int bits;

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;

out vec4 outputColor;

void main(){
	gl_Position = vec4(position, 1.0);
	outputColor = color;
}