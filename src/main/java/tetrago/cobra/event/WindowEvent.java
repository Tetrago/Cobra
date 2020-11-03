package tetrago.cobra.event;

import tetrago.cobra.graphics.Window;

public class WindowEvent
{
    public enum Type
    {
        CLOSE,
        RESIZE
    }

    private final Window window_;
    private final Type type_;

    public WindowEvent(Window window, Type type)
    {
        window_ = window;
        type_ = type;
    }

    public Window getWindow() { return window_; }
    public Type getType() { return type_; }
}
