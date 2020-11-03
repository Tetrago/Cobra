package tetrago.cobra.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tetrago.cobra.core.IO;

public class Renderer2D
{
    private static class Storage
    {
        private static VertexArray vertexArray_;
        private static Buffer[] buffers_;
        private static Shader shader_;
        
        public static Matrix4f scene_ = new Matrix4f().identity();
    }

    public static void init()
    {
        Storage.buffers_ = new Buffer[3];
        Storage.vertexArray_  = new VertexArray();

        Storage.vertexArray_.setIndexBuffer(
                Storage.buffers_[0] = new Buffer(new int[]{ 0, 1, 2, 0, 2, 3 }, Buffer.Usage.STATIC));

        Storage.vertexArray_.attachVertexBuffer(
                Storage.buffers_[1] = new Buffer(new float[]{ -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f }, Buffer.Usage.STATIC),
                new VertexArray.Layout(0, 2, VertexArray.ValueType.FLOAT));

        Storage.vertexArray_.attachVertexBuffer(
                Storage.buffers_[2] = new Buffer(new float[]{ 0, 0, 1, 0, 1, 1, 0, 1 }, Buffer.Usage.STATIC),
                new VertexArray.Layout(1, 2, VertexArray.ValueType.FLOAT));

        Storage.shader_ = new Shader(IO.toString(Renderer2D.class.getResourceAsStream("renderer_2d.shader")));
    }

    public static void free()
    {
        Storage.shader_.close();
        Storage.vertexArray_.close();

        for(Buffer buffer : Storage.buffers_)
        {
            buffer.close();
        }
    }

    public static void begin(Matrix4f view)
    {
        Storage.scene_ = view;
    }

    public static void end()
    {
        
    }

    public static void drawQuad(Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        Graphics g = RenderStack.current();

        g.setShader(Storage.shader_);
        g.setVertexArray(Storage.vertexArray_);

        Storage.shader_.upload("u_matrix", buildModelMatrix(pos, radians, scale).mul(Storage.scene_));
        Storage.shader_.upload("u_color", color);

        g.drawIndexed(6);
    }

    @Deprecated
    private static Matrix4f buildModelMatrix(Vector2f pos, float radians, Vector2f scale)
    {
        return new Matrix4f()
                .translate(pos.x, pos.y, 0)
                .rotate(radians, 0, 0, 1)
                .scale(scale.x, scale.y, 0);
    }
}