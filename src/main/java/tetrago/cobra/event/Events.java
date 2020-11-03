package tetrago.cobra.event;

public class Events
{
    public static final EventBus<LogEvent> LOG = new EventBus<>();
    public static final EventBus<WindowEvent> WINDOW = new EventBus<>();
}
