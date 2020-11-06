package tetrago.cobra.io;

import tetrago.cobra.event.Events;
import tetrago.cobra.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard
{
    private static final boolean[] keys_ = new boolean[GLFW_KEY_LAST];

    public static void init()
    {
        Events.WINDOW.listen(e ->
        {
            switch(e.type())
            {
            case KEY:
            {
                KeyEvent event = (KeyEvent)e;
                keys_[event.key] = event.status;
            } break;
            }
        });
    }

    public static boolean isKeyDown(Key key)
    {
        return keys_[key.key()];
    }
}
