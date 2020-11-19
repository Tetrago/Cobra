package tetrago.cobra.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tetrago.cobra.core.Cell;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.io.Resource;
import tetrago.cobra.node.Camera;

import java.util.stream.IntStream;

public class Batch2D implements IClosable
{
    private static int refCount_ = 0;
    private static Shader shader_;

    private VertexArray vao_;

    private final int MAX_QUADS = 2000;
    private final int MAX_TEXTURES = 16;

    private final float[] vertices_ = new float[MAX_QUADS * 4 * 2];
    private final float[] uvs_ = new float[MAX_QUADS * 4 * 2];
    private final float[] colors_ = new float[MAX_QUADS * 4 * 4];
    private final float[] textures_ = new float[MAX_QUADS * 4];

    private final Buffer vertexBuffer_ = new Buffer(vertices_.length, Buffer.Usage.DYNAMIC);
    private final Buffer uvBuffer_ = new Buffer(uvs_.length, Buffer.Usage.DYNAMIC);
    private final Buffer colorBuffer_ = new Buffer(colors_.length, Buffer.Usage.DYNAMIC);
    private final Buffer textureBuffer_ = new Buffer(textures_.length, Buffer.Usage.DYNAMIC);

    private final Texture[] map_ = new Texture[MAX_TEXTURES];

    private int quadDrawCount_ = 0;
    private int textureIndex_ = 1;
    private Matrix4f matrix_ = new Matrix4f();

    public Batch2D()
    {
        if(++refCount_ == 1)
        {
            shader_ = new Shader(Resource.toString(Renderer2D.class.getResourceAsStream("renderer_2d.shader")));
        }

        vao_ = new VertexArray()
                .attachVertexBuffer(new Cell<>(vertexBuffer_), new VertexArray.Layout(0, 2, VertexArray.ValueType.FLOAT))
                .attachVertexBuffer(new Cell<>(uvBuffer_), new VertexArray.Layout(1, 2, VertexArray.ValueType.FLOAT))
                .attachVertexBuffer(new Cell<>(colorBuffer_), new VertexArray.Layout(2, 4, VertexArray.ValueType.FLOAT))
                .attachVertexBuffer(new Cell<>(textureBuffer_), new VertexArray.Layout(3, 1, VertexArray.ValueType.FLOAT));

        loadTextures();
        loadIndices();
    }

    @Override
    public void close()
    {
        if(--refCount_ == 0)
        {
            shader_.close();
        }

        vao_.close();
    }

    public void setMatrix(Matrix4f matrix)
    {
        matrix_ = matrix;
    }

    private void loadTextures()
    {
        RenderStack.current().setShader(shader_);
        shader_.upload("u_samplers", IntStream.range(0, MAX_TEXTURES).toArray());

        map_[0] = new Texture().setPixels(1, 1, new Vector4f[]{ Color.WHITE });
    }

    private void loadIndices()
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

    private void loadDynamics()
    {
        vertexBuffer_.load(vertices_);
        uvBuffer_.load(uvs_);
        colorBuffer_.load(colors_);
        textureBuffer_.load(textures_);

        shader_.upload("c_matrix", matrix_);
    }

    private int findTexture(Texture texture)
    {
        if(textureIndex_ == MAX_TEXTURES)
        {
            execute();
        }

        for(int i = 1; i < textureIndex_; ++i)
        {
            if(map_[i] == texture)
            {
                return i;
            }
        }

        map_[textureIndex_] = texture;
        return textureIndex_++;
    }

    public void execute()
    {
        Graphics g = RenderStack.current();

        if(quadDrawCount_ == 0)
        {
            return;
        }

        g.setShader(shader_);
        g.setVertexArray(vao_);
        g.setTextures(map_, textureIndex_);
        loadDynamics();

        g.drawIndexed(quadDrawCount_ * 6);

        quadDrawCount_ = 0;
        textureIndex_ = 1;
    }

    public void drawQuad(Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        drawQuad(0, color, pos, radians, scale, new Vector2f(0, 0), new Vector2f(1, 1));
    }

    public void drawQuad(TextureClip texture, Vector2f pos, float radians, Vector2f scale)
    {
        drawQuad(texture, Color.WHITE, pos, radians, scale);
    }

    public void drawQuad(TextureClip texture, Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        drawQuad(findTexture(texture.texture_), color, pos, radians, scale, texture.uv0_, texture.uv1_);
    }

    private void drawQuad(int tex, Vector4f color, Vector2f pos, float radians, Vector2f scale, Vector2f uv0, Vector2f uv1)
    {
        if(quadDrawCount_ >= MAX_QUADS)
        {
            execute();
        }

        int index = quadDrawCount_ * 4 * 2;
        insertPosition(index, pos, radians, scale);

        index = quadDrawCount_ * 4 * 2;
        uvs_[index] = uv0.x;
        uvs_[index + 1] = uv0.y;
        uvs_[index + 2] = uv1.x;
        uvs_[index + 3] = uv0.y;
        uvs_[index + 4] = uv1.x;
        uvs_[index + 5] = uv1.y;
        uvs_[index + 6] = uv0.x;
        uvs_[index + 7] = uv1.y;

        index = quadDrawCount_ * 4 * 4;
        copyColor(index, color);
        copyColor(index + 4, color);
        copyColor(index + 4 * 2, color);
        copyColor(index + 4 * 3, color);

        index = quadDrawCount_ * 4;
        textures_[index] = tex;
        textures_[index + 1] = tex;
        textures_[index + 2] = tex;
        textures_[index + 3] = tex;

        ++quadDrawCount_;
    }

    private void insertPosition(int index, Vector2f pos, float radians, Vector2f scale)
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

    private void copyColor(int index, Vector4f color)
    {
        colors_[index] = color.x;
        colors_[index + 1] = color.y;
        colors_[index + 2] = color.z;
        colors_[index + 3] = color.w;
    }
}
