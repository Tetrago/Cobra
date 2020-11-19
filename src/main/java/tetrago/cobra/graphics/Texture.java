package tetrago.cobra.graphics;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.core.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements IClosable
{
    public enum Format
    {
        DEPTH(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT),
        RGB8(GL_RGB8, GL_RGB),
        RGBA8(GL_RGBA, GL_RGBA);

        private final int internal_, format_;

        Format(int internal, int format)
        {
            internal_ = internal;
            format_ = format;
        }

        private static Format find(int channels)
        {
            switch(channels)
            {
            default:
            case 3: return RGB8;
            case 4: return RGBA8;
            }
        }
    }

    private final int handle_;

    static
    {
        stbi_set_flip_vertically_on_load(true);
    }

    public Texture()
    {
        handle_ = glGenTextures();
    }

    @Override
    public void close()
    {
        glDeleteTextures(handle_);
    }

    public Texture load(byte[] bytes)
    {
        stbi_set_flip_vertically_on_load(true);

        int[] w = new int[1];
        int[] h = new int[1];
        int[] c = new int[1];

        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
        ByteBuffer stbi = stbi_load_from_memory(buffer, w, h, c, 0);

        String err = stbi_failure_reason();
        if(err != null)
        {
            Logger.COBRA.error("Failed to load image: %s", err);
        }

        create(w[0], h[0], Format.find(c[0]), stbi);
        stbi_image_free(stbi);

        return this;
    }

    public Texture create(int w, int h, Format format, ByteBuffer buffer)
    {
        glBindTexture(GL_TEXTURE_2D, handle_);

        glTexImage2D(GL_TEXTURE_2D, 0, format.internal_, w, h, 0, format.format_, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        return this;
    }

    public Texture setPixels(int w, int h, Vector4f[] colors)
    {
        ByteBuffer bytes = BufferUtils.createByteBuffer(colors.length * 4 * Integer.BYTES);

        for(Vector4f color : colors)
        {
            bytes.put((byte) (color.x * 255))
                    .put((byte)(color.y * 255))
                    .put((byte)(color.z * 255))
                    .put((byte)(color.w * 255));
        }

        return create(w, h, Format.RGBA8, bytes.flip());
    }

    public TextureClip clip(Vector2f uv0, Vector2f uv1)
    {
        return new TextureClip(this, uv0, uv1);
    }

    public TextureClip full()
    {
        return new TextureClip(this, new Vector2f(0, 0), new Vector2f(1, 1));
    }

    public int handle() { return handle_; }
}
