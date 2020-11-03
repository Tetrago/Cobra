$vert

layout(location = 0) in vec2 position_;
layout(location = 1) in vec2 uv_;

layout(location = 0) out vec2 _uv;

uniform mat4 u_matrix;

void main()
{
    gl_Position = u_matrix * vec4(position_, 0, 1);
    _uv = uv_;
}

$frag

layout(location = 0) in vec2 _uv;

layout(location = 0) out vec4 color_;

uniform vec4 u_color;

void main()
{
    color_ = vec4(_uv, 0, 1);
    color_ = u_color;
}