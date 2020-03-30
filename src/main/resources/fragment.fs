#version 460

in vec4 outputColor;
out vec4 fragColor;

void main(){
	fragColor = outputColor;
	//fragColor = vec4(1.0,1.0,1.0,1.0);
}