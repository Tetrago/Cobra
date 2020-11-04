package tetrago.cobra.core;

public class Cell<T extends IClosable> implements IClosable
{
    private final T v_;
    private int count_ = 1;

    public Cell()
    {
        v_ = null;
    }

    public Cell(T v)
    {
        v_ = v;
    }

    @Override
    public void close()
    {
        --count_;

        if(count_ == 0 && v_ != null)
        {
            v_.close();
        }
    }

    public Cell<T> ref()
    {
        ++count_;
        return this;
    }

    public T get() { return v_; }
}
