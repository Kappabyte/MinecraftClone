#version 130

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 colour;

void main() {
  fragColor = colour * texture(texture_sampler, outTexCoord);
  fragColor = vec4(0,1,1,1);
}
