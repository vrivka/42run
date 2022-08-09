#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textCords;

uniform mat4 projection;
uniform mat4 model_view;

out vec2 TextCords;

void main() {
    gl_Position = projection * model_view * vec4(position, 1.0);
    TextCords = textCords;
}
