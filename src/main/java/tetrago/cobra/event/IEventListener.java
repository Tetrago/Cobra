package tetrago.cobra.event;

public interface IEventListener<T>
{
    void signal(T e);
}
