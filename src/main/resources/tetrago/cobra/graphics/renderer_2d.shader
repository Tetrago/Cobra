$vert

layout(location = 0) in vec2 position_;
layout(location = 1) in vec2 uv_;
layout(location = 2) in vec4 color_;

layout(location = 0) out vec2 _uv;
layout(location = 1) out vec4 _color;

uniform mat4 u_matrix;

void main()
{
    gl_Position = u_matrix * vec4(position_, 0, 1);

    _uv = uv_;
    _color = color_;
}

$frag

layout(location = 0) in vec2 _uv;
layout(location = 1) in vec4 _color;

layout(location = 0) out vec4 color_;

void main()
{
    color_ = _color;
}