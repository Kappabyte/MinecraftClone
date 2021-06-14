#version 130

out vec4 fragColor;

uniform vec3 colour;

void main() {
  fragColor = vec4(colour, 1.0);
}
