package tetrago.cobra.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tetrago.cobra.core.Cell;
import tetrago.cobra.io.Resource;

public class Renderer2D
{
    private static class Storage
    {
        private static VertexArray vao;
        private static Shader shader;

        public static final int MAX_QUADS = 2000;

        public static final float[] vertices = new float[MAX_QUADS * 4 * 2];
        public static final float[] uvs = new float[MAX_QUADS * 4 * 2];
        public static final float[] colors = new float[MAX_QUADS * 4 * 4];

        public static final Buffer vertexBuffer = new Buffer(vertices.length, Buffer.Usage.DYNAMIC);
        public static final Buffer uvBuffer = new Buffer(uvs.length, Buffer.Usage.DYNAMIC);
        public static final Buffer colorBuffer = new Buffer(colors.length, Buffer.Usage.DYNAMIC);

        public static int quadDrawCount = 0;
    }

    public static void init()
    {
        final VertexArray vao = Storage.vao = new VertexArray();
        vao.attachVertexBuffer(new Cell<>(Storage.vertexBuffer), new VertexArray.Layout(0, 2, VertexArray.ValueType.FLOAT));
        vao.attachVertexBuffer(new Cell<>(Storage.uvBuffer), new VertexArray.Layout(1, 2, VertexArray.ValueType.FLOAT));
        vao.attachVertexBuffer(new Cell<>(Storage.colorBuffer), new VertexArray.Layout(2, 4, VertexArray.ValueType.FLOAT));

        Storage.shader = new Shader(Resource.toString(Renderer2D.class.getResourceAsStream("renderer_2d.shader")));

        loadIndices();
    }

    private static void loadIndices()
    {
        int[] indices = new int[Storage.MAX_QUADS * 6];

        for(int i = 0; i < Storage.MAX_QUADS; ++i)
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

        Storage.vao.setIndexBuffer(new Cell<>(new Buffer(indices, Buffer.Usage.STATIC)));
    }

    private static void loadDynamics()
    {
        Storage.vertexBuffer.load(Storage.vertices);
        Storage.uvBuffer.load(Storage.uvs);
        Storage.colorBuffer.load(Storage.colors);
    }

    public static void free()
    {
        Storage.shader.close();
        Storage.vao.close();
    }

    public static void prepare(Matrix4f matrix)
    {
        RenderStack.current().setShader(Storage.shader);
        Storage.shader.upload("u_matrix", matrix);
    }

    public static void flush()
    {
        final Graphics g = RenderStack.current();

        g.setShader(Storage.shader);
        g.setVertexArray(Storage.vao);

        loadDynamics();
        g.drawIndexed(Storage.quadDrawCount * 6);

        Storage.quadDrawCount = 0;
    }

    public static void drawQuad(Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        if(Storage.quadDrawCount >= Storage.MAX_QUADS)
        {
            flush();
        }

        int index = Storage.quadDrawCount * 4 * 2;
        insertPosition(index, pos, radians, scale);

        index = Storage.quadDrawCount * 4 * 2;
        Storage.uvs[index] = 0;
        Storage.uvs[index + 1] = 0;
        Storage.uvs[index + 2] = 1;
        Storage.uvs[index + 3] = 0;
        Storage.uvs[index + 4] = 1;
        Storage.uvs[index + 5] = 1;
        Storage.uvs[index + 6] = 0;
        Storage.uvs[index + 7] = 1;

        index = Storage.quadDrawCount * 4 * 4;
        copyColor(index, color);
        copyColor(index + 4, color);
        copyColor(index + 4 * 2, color);
        copyColor(index + 4 * 3, color);

        ++Storage.quadDrawCount;
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

            Storage.vertices[index + i * 2] = out.x;
            Storage.vertices[index + i * 2 + 1] = out.y;
        }
    }

    private static void copyColor(int index, Vector4f color)
    {
        Storage.colors[index] = color.x;
        Storage.colors[index + 1] = color.y;
        Storage.colors[index + 2] = color.z;
        Storage.colors[index + 3] = color.w;
    }
}
