#version 330 core

in vec4 col;
in vec3 texcoord;

uniform sampler2DArray textures;

out vec4 color;

void main() {
	color = texture(textures, texcoord) * col;
}