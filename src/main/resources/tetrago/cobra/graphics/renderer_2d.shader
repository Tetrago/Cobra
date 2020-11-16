$vert
#version 330 core

layout(location = 0) in vec2 position_;
layout(location = 1) in vec2 uv_;
layout(location = 2) in vec4 color_;
layout(location = 3) in float tex_;

layout(location = 0) out vec2 _uv;
layout(location = 1) out vec4 _color;
layout(location = 2) out float _tex;

uniform mat4 u_matrix;

void main()
{
    gl_Position = u_matrix * vec4(position_, 0, 1);

    _uv = uv_;
    _color = color_;
    _tex = tex_;
}

$frag
#version 330 core

layout(location = 0) in vec2 _uv;
layout(location = 1) in vec4 _color;
layout(location = 2) in float _tex;

layout(location = 0) out vec4 color_;

uniform sampler2D u_samplers[16];

void main()
{
    color_ = texture(u_samplers[int(_tex)], _uv) * _color;
}