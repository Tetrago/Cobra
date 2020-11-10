import org.joml.Matrix4f;
import tetrago.cobra.core.Cell;
import tetrago.cobra.core.LogLevel;
import tetrago.cobra.core.Program;
import tetrago.cobra.event.Events;
import tetrago.cobra.graphics.*;
import tetrago.cobra.io.Resource;
import tetrago.cobra.node.Node;
import tetrago.cobra.node.Node2D;
import tetrago.cobra.node.Scene;
import tetrago.cobra.node.SpriteRenderer;

import java.io.PrintStream;

public class TestProgram extends Program
{
    private Matrix4f view_;
    private Scene scene_;
    private PlayerController player_;

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

        scene_ = new Scene();
        player_ = scene_.root().add(PlayerController.class, "Player");
        player_.add(SpriteRenderer.class).color = Color.BLUE;
    }

    @Override
    public void update()
    {
        Renderer.clear();
        Renderer2D.prepare(view_);

        scene_.update();

        Renderer2D.flush();
    }
}
