package tetrago.cobra.graphics;

import tetrago.cobra.core.Cell;
import tetrago.cobra.core.IClosable;

import java.util.ArrayList;
import java.util.List;

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
    private Cell<Buffer> index_ = new Cell<>();
    private final List<Cell<Buffer>> buffers_ = new ArrayList<>();

    public VertexArray()
    {
        handle_ = glGenVertexArrays();
    }

    @Override
    public void close()
    {
        glDeleteVertexArrays(handle_);

        index_.close();
        buffers_.forEach(Cell::close);
    }

    public void setIndexBuffer(Cell<Buffer> buffer)
    {
        glBindVertexArray(handle_);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.get().handle());

        glBindVertexArray(0);

        index_ = buffer.ref();
    }

    public void attachVertexBuffer(Cell<Buffer> buffer, Layout layout)
    {
        glBindVertexArray(handle_);
        glBindBuffer(GL_ARRAY_BUFFER, buffer.get().handle());

        glEnableVertexAttribArray(layout.index_);
        glVertexAttribPointer(layout.index_, layout.count_, layout.findType(), false, 0, 0);

        glBindVertexArray(0);

        buffers_.add(buffer.ref());
    }

    public int handle() { return handle_; }
}
