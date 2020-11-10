package tetrago.cobra.graphics;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.core.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements IClosable
{
    private final int handle_;
    private ByteBuffer stbi_;

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

        if(stbi_ != null)
        {
            stbi_image_free(stbi_);
        }
    }

    public Texture load(byte[] bytes)
    {
        int[] w = new int[1];
        int[] h = new int[1];
        int[] c = new int[1];

        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
        stbi_ = stbi_load_from_memory(buffer, w, h, c, 0);

        String err = stbi_failure_reason();
        if(err != null)
        {
            Logger.COBRA.error("Failed to load image: %s", err);
        }

        return create(w[0], h[0], c[0], stbi_);
    }

    public Texture create(int w, int h, int c, ByteBuffer buffer)
    {
        glBindTexture(GL_TEXTURE_2D, handle_);

        int format, internal;
        switch(c)
        {
        default:
        case 3:
            format = GL_RGB;
            internal = GL_RGB8;
            break;
        case 4:
            format = GL_RGBA;
            internal = GL_RGBA8;
            break;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, internal, w, h, 0, format, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        return this;
    }

    public void setPixels(int w, int h, Vector4f[] colors)
    {
        ByteBuffer bytes = BufferUtils.createByteBuffer(colors.length * 4 * Float.BYTES);

        for(Vector4f color : colors)
        {
            bytes.putFloat(color.x)
                    .putFloat(color.y)
                    .putFloat(color.z)
                    .putFloat(color.w);
        }

        create(w, h, 4, bytes);
    }

    public int handle() { return handle_; }
}
