#version 330

in vec2 TextCords;

uniform sampler2D texture_sampler;
uniform vec3 color;
uniform int useColor;

out vec4 fragColor;

void main()
{
    if (useColor == 1) {
        fragColor = vec4(color, 1);
    } else {
        fragColor = texture(texture_sampler, TextCords);
    }
}
