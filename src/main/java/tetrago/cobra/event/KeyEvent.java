package tetrago.cobra.event;

import tetrago.cobra.graphics.Window;

public class KeyEvent extends WindowEvent
{
    public int key;
    public boolean status;

    public KeyEvent(Window window, int key, boolean status)
    {
        super(window, Type.KEY);

        this.key = key;
        this.status = status;
    }
}
