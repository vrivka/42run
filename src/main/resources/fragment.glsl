#version 330

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation attenuation;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight {
    vec3 color;
    vec3 direction;
    float intensity;
};

in vec2 TextCords;
in vec3 Normal;
in vec3 Position;

uniform sampler2D texture_sampler;
uniform vec3 ambient_light;
uniform float specular_power;
uniform Material material;
uniform PointLight point_light;
uniform DirectionalLight directional_light;
uniform vec3 camera_pos;

out vec4 fragColor;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColours(Material material, vec2 TextCords)
{
    if (material.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, TextCords);
        diffuseC = ambientC;
        speculrC = ambientC;
    }
    else
    {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        speculrC = material.specular;
    }
}

vec4 calcLightColor(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specular_power);
    specColour = speculrC * light_intensity * specularFactor * material.reflectance * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.attenuation.constant + light.attenuation.linear * distance + light.attenuation.exponent * distance * distance;

    return light_color / attenuationInv;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
    setupColours(material, TextCords);

    vec4 diffuseSpecularComp = calcDirectionalLight(directional_light, Position, Normal);
    diffuseSpecularComp += calcPointLight(point_light, Position, Normal);

    fragColor = ambientC * vec4(ambient_light, 1) + diffuseSpecularComp;
}
