package tetrago.cobra.graphics;

import tetrago.cobra.core.IClosable;

import static org.lwjgl.opengl.GL15.*;

public class Buffer implements IClosable
{
    public enum Usage
    {
        STATIC(GL_STATIC_DRAW),
        DYNAMIC(GL_DYNAMIC_DRAW);

        private final int usage_;

        Usage(int usage)
        {
            usage_ = usage;
        }

        public int usage() { return usage_; }
    }

    private final int usage_;
    private final int handle_;

    public Buffer(long size, Usage usage)
    {
        usage_ = usage.usage();

        handle_ = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, handle_);
        glBufferData(GL_ARRAY_BUFFER, size, usage_);
    }

    public Buffer(int[] data, Usage usage)
    {
        this(data.length, usage);
        load(data);
    }

    public Buffer(float[] data, Usage usage)
    {
        this(data.length, usage);
        load(data);
    }

    public void load(int[] data)
    {
        glBindBuffer(GL_ARRAY_BUFFER, handle_);
        glBufferData(GL_ARRAY_BUFFER, data, usage_);
    }

    public void load(float[] data)
    {
        glBindBuffer(GL_ARRAY_BUFFER, handle_);
        glBufferData(GL_ARRAY_BUFFER, data, usage_);
    }

    @Override
    public void close()
    {
        glDeleteBuffers(handle_);
    }

    public int handle() { return handle_; }
}
