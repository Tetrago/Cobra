package tetrago.cobra.graphics;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import tetrago.cobra.core.IClosable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class Texture implements IClosable
{
    private final int handle_;

    public Texture()
    {
        handle_ = glGenTextures();
    }

    @Override
    public void close()
    {
        glDeleteTextures(handle_);
    }

    public void load(ByteBuffer image)
    {
        int[] w = new int[1];
        int[] h = new int[1];
        int[] c = new int[1];

        ByteBuffer buffer = stbi_load_from_memory(image, w, h, c, 4);

        create(w[0], h[0], c[0], buffer);
    }

    public void create(int w, int h, int c, ByteBuffer buffer)
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
