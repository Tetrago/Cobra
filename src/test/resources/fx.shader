$vert
#version 330 core

layout(location = 0) in vec2 position_;
layout(location = 1) in vec2 uv_;

layout(location = 0) out vec2 _uv;

void main()
{
    gl_Position = vec4(position_, 0, 1);
    _uv = uv_;
}

$frag
#version 330 core

layout(location = 0) out vec4 color_;

layout(location = 0) in vec2 _uv;

uniform sampler2D u_sampler;

void main()
{
    vec4 col = texture(u_sampler, _uv);
    color_ = col.grba;
}