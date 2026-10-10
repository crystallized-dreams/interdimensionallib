#version 150


uniform sampler2D DiffuseSampler;
uniform float Time;
uniform vec2 ScreenSize;

in vec2 texCoord;
out vec4 fragColor;

float luminance(vec3 color) {
    return dot(color, vec3(0.2126, 0.7152, 0.0722));
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float gray = luminance(color.rgb);

    vec2 uv = texCoord * 2.0 - 1.0;
    float vignette = 1.0 - dot(uv, uv) * 0.3;

    fragColor = vec4(vec3(gray) * vignette, color.a);
}