import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import tetrago.cobra.core.LogLevel;
import tetrago.cobra.core.Program;
import tetrago.cobra.event.Events;
import tetrago.cobra.graphics.Color;
import tetrago.cobra.graphics.Renderer;
import tetrago.cobra.graphics.Renderer2D;

import java.io.PrintStream;

public class TestProgram extends Program
{
    public TestProgram()
    {
        super("Test");
    }

    @Override
    public void setup()
    {
        Events.LOG.listen(e ->
        {
            PrintStream stream = e.level == LogLevel.ERROR ? System.err : System.out;

            if(e.level == LogLevel.FATAL)
            {
                throw new IllegalStateException(String.format("[%s] %s", e.name, e.message));
            }
            else
            {
                stream.format("[%s] (%s) %s%n", e.name, e.level.toString(), e.message);
            }
        });
    }

    @Override
    public void update()
    {
        Renderer.clear();
        Renderer2D.begin(new Matrix4f());

        Renderer2D.drawQuad(Color.BLUE, new Vector2f(0.5f, 0.5f), Math.toRadians(45), new Vector2f(1));

        Renderer2D.end();
    }
}
