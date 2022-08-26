#version 330

in vec2 TextCords;
in vec3 Normal;

uniform sampler2D texture_sampler;
uniform vec3 color;
uniform int useColor;

out vec4 fragColor;

void main()
{
    if (useColor == 1) {
        vec3 an = abs(Normal);
        if (an.x >= 0.5 || (an.y < 0.5 && an.z < 0.5)) {
            fragColor = vec4(color - 0.1, 1);
        } else if (an.y >= 0.5 || (an.x < 0.5 && an.z < 0.5)) {
            fragColor = vec4(color - 0.2, 1);
        } else if (an.z >= 0.5 || (an.x < 0.5 && an.y < 0.5)) {
            fragColor = vec4(color - 0.3, 1);
        }
    } else {
        fragColor = texture(texture_sampler, TextCords);
    }
}
