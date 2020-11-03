package tetrago.cobra.event;

import tetrago.cobra.graphics.Window;

public class WindowResizeEvent extends WindowEvent
{
    private final int width_, height_;

    public WindowResizeEvent(Window window, Type type, int width, int height)
    {
        super(window, type);

        width_ = width;
        height_ = height;
    }

    public int width() { return width_; }
    public int height() { return height_; }
}
