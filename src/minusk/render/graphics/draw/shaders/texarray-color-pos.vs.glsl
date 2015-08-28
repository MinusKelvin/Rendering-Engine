#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 tex;
layout(location = 2) in vec4 color;

uniform mat4 proj;

out vec4 col;
out vec3 texcoord;

void main() {
	gl_Position = proj * vec4(pos, 1.0);
	col = color;
	texcoord = tex;
}