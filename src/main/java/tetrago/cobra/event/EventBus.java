package tetrago.cobra.event;

import java.util.ArrayList;
import java.util.List;

public class EventBus<T>
{
    private final List<IEventListener<T>> listeners_ = new ArrayList<>();

    /**
     * Subscribes {@code listener} to log events.
     *
     * @param   listener    Event listener.
     */
    public void listen(IEventListener<T> listener)
    {
        listeners_.add(listener);
    }

    /**
     * Emits event {@code event}.
     *
     * @param   event   Event data.
     */
    public void emit(T event)
    {
        listeners_.forEach(l -> l.signal(event));
    }
}
