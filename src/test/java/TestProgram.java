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
    private Matrix4f view_;
    private float angle_;

    public TestProgram()
    {
        super("Test");

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
    public void setup()
    {
        final float scale = 10.0f / window().width();

        float we = window().width() * scale * 0.5f;
        float he = window().height() * scale * 0.5f;

        view_ = new Matrix4f().translate(0, 0, 1).ortho(-we, we, -he, he, 0.1f, 1000);
    }

    @Override
    public void update()
    {
        angle_ += 0.1f;

        Renderer.clear();
        Renderer2D.prepare(view_);

        Renderer2D.drawQuad(Color.BLUE, new Vector2f(1, 0), Math.toRadians(angle_), new Vector2f(1));
        Renderer2D.drawQuad(Color.RED, new Vector2f(-1, 0), Math.toRadians(0), new Vector2f(1));

        Renderer2D.flush();
    }
}
