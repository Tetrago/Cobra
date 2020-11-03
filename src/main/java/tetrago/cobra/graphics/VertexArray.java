package tetrago.cobra.graphics;

import tetrago.cobra.core.IClosable;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray implements IClosable
{
    public enum ValueType
    {
        FLOAT,
        INT
    }

    public static class Layout
    {
        private final int index_;
        private final int count_;
        private final ValueType type_;

        public Layout(int index, int count, ValueType type)
        {
            index_ = index;
            count_ = count;
            type_ = type;
        }

        private int findType()
        {
            switch(type_)
            {
            default:
            case FLOAT: return GL_FLOAT;
            case INT: return GL_INT;
            }
        }
    }

    private final int handle_;

    public VertexArray()
    {
        handle_ = glGenVertexArrays();
    }

    @Override
    public void close()
    {
        glDeleteVertexArrays(handle_);
    }

    public void setIndexBuffer(Buffer buffer)
    {
        glBindVertexArray(handle_);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.handle());

        glBindVertexArray(0);
    }

    public void attachVertexBuffer(Buffer buffer, Layout layout)
    {
        glBindVertexArray(handle_);
        glBindBuffer(GL_ARRAY_BUFFER, buffer.handle());

        glEnableVertexAttribArray(layout.index_);
        glVertexAttribPointer(layout.index_, layout.count_, layout.findType(), false, 0, 0);

        glBindVertexArray(0);
    }

    public int handle() { return handle_; }
}
