package tetrago.cobra.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tetrago.cobra.core.Cell;
import tetrago.cobra.io.Resource;

public class Renderer2D
{
    private static VertexArray vao_;
    private static Shader shader_;

    private static final int MAX_QUADS = 2000;

    private static final float[] vertices_ = new float[MAX_QUADS * 4 * 2];
    private static final float[] uvs_ = new float[MAX_QUADS * 4 * 2];
    private static final float[] colors_ = new float[MAX_QUADS * 4 * 4];

    private static final Buffer vertexBuffer_ = new Buffer(vertices_.length, Buffer.Usage.DYNAMIC);
    private static final Buffer uvBuffer_ = new Buffer(uvs_.length, Buffer.Usage.DYNAMIC);
    private static final Buffer colorBuffer_ = new Buffer(colors_.length, Buffer.Usage.DYNAMIC);

    private static int quadDrawCount_ = 0;

    public static void init()
    {
        vao_ = new VertexArray();
        vao_.attachVertexBuffer(new Cell<>(vertexBuffer_), new VertexArray.Layout(0, 2, VertexArray.ValueType.FLOAT));
        vao_.attachVertexBuffer(new Cell<>(uvBuffer_), new VertexArray.Layout(1, 2, VertexArray.ValueType.FLOAT));
        vao_.attachVertexBuffer(new Cell<>(colorBuffer_), new VertexArray.Layout(2, 4, VertexArray.ValueType.FLOAT));

        shader_ = new Shader(Resource.toString(Renderer2D.class.getResourceAsStream("renderer_2d.shader")));

        loadIndices();
    }

    private static void loadIndices()
    {
        int[] indices = new int[MAX_QUADS * 6];

        for(int i = 0; i < MAX_QUADS; ++i)
        {
            int index = i * 6;
            int base = i * 4;

            indices[index] = base;
            indices[index + 1] = base + 1;
            indices[index + 2] = base + 2;

            indices[index + 3] = base;
            indices[index + 4] = base + 2;
            indices[index + 5] = base + 3;
        }

        vao_.setIndexBuffer(new Cell<>(new Buffer(indices, Buffer.Usage.STATIC)));
    }

    private static void loadDynamics()
    {
        vertexBuffer_.load(vertices_);
        uvBuffer_.load(uvs_);
        colorBuffer_.load(colors_);
    }

    public static void free()
    {
        shader_.close();
        vao_.close();
    }

    public static void prepare(Matrix4f matrix)
    {
        RenderStack.current().setShader(shader_);
        shader_.upload("u_matrix", matrix);
    }

    public static void flush()
    {
        final Graphics g = RenderStack.current();

        g.setShader(shader_);
        g.setVertexArray(vao_);
        loadDynamics();

        g.drawIndexed(quadDrawCount_ * 6);

        quadDrawCount_ = 0;
    }

    public static void drawQuad(Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        if(quadDrawCount_ >= MAX_QUADS)
        {
            flush();
        }

        int index = quadDrawCount_ * 4 * 2;
        insertPosition(index, pos, radians, scale);

        index = quadDrawCount_ * 4 * 2;
        uvs_[index] = 0;
        uvs_[index + 1] = 0;
        uvs_[index + 2] = 1;
        uvs_[index + 3] = 0;
        uvs_[index + 4] = 1;
        uvs_[index + 5] = 1;
        uvs_[index + 6] = 0;
        uvs_[index + 7] = 1;

        index = quadDrawCount_ * 4 * 4;
        copyColor(index, color);
        copyColor(index + 4, color);
        copyColor(index + 4 * 2, color);
        copyColor(index + 4 * 3, color);

        ++quadDrawCount_;
    }

    private static void insertPosition(int index, Vector2f pos, float radians, Vector2f scale)
    {
        Matrix4f model = new Matrix4f()
                .translate(pos.x, pos.y, 0)
                .rotate(radians, 0, 0, -1)
                .scale(scale.x, scale.y, 1);

        Vector4f[] offsets =
        {
                new Vector4f(-0.5f, -0.5f, 0, 1),
                new Vector4f(0.5f, -0.5f, 0, 1),
                new Vector4f(0.5f, 0.5f, 0, 1),
                new Vector4f(-0.5f, 0.5f, 0, 1)
        };

        Vector4f temp = new Vector4f();
        for(int i = 0; i < 4; ++i)
        {
            Vector4f out = new Vector4f(pos.x, pos.y, 0, 1).add(model.transform(offsets[i]));

            vertices_[index + i * 2] = out.x;
            vertices_[index + i * 2 + 1] = out.y;
        }
    }

    private static void copyColor(int index, Vector4f color)
    {
        colors_[index] = color.x;
        colors_[index + 1] = color.y;
        colors_[index + 2] = color.z;
        colors_[index + 3] = color.w;
    }
}
