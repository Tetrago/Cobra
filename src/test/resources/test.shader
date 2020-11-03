$vert

layout(location = 0) in vec2 position_;
layout(location = 1) in vec2 uv_;

layout(location = 0) out vec2 _uv;

void main()
{
    gl_Position = vec4(position_, 0, 1);
    _uv = uv_;
}

$frag

layout(location = 0) in vec2 _uv;

layout(location = 0) out vec4 color_;

void main()
{
    color_ = vec4(_uv, 0, 1);
}