import tetrago.cobra.core.Time;
import tetrago.cobra.io.Key;
import tetrago.cobra.io.Keyboard;
import tetrago.cobra.node.Node2D;

public class PlayerController extends Node2D
{
    public float speed = 5f;

    @Override
    public void update()
    {
        float s = speed * (float)Time.deltaTime();

        if(Keyboard.isKeyDown(Key.UP))
        {
            position.add(0, s);
        }
        else if(Keyboard.isKeyDown(Key.DOWN))
        {
            position.add(0, -s);
        }

        if(Keyboard.isKeyDown(Key.RIGHT))
        {
            position.add(s, 0);
        }
        else if(Keyboard.isKeyDown(Key.LEFT))
        {
            position.add(-s, 0);
        }
    }
}
