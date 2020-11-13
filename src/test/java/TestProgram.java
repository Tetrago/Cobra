import org.joml.Matrix4f;
import tetrago.cobra.core.LogLevel;
import tetrago.cobra.core.Program;
import tetrago.cobra.event.Events;
import tetrago.cobra.graphics.*;
import tetrago.cobra.node.*;

import java.io.PrintStream;

public class TestProgram extends Program
{
    private Matrix4f view_;
    private Scene scene_;
    private Camera camera_;
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
        scene_ = new Scene();
        camera_ = scene_.root().add(Ortho2DCamera.class, "Main Camera");

        player_ = scene_.root().add(PlayerController.class, "Player");
        player_.add(SpriteRenderer.class).color = Color.GREEN;

        scene_.start();
    }

    @Override
    public void update()
    {
        Renderer.clear();
        Renderer2D.prepare(camera_);

        scene_.update();

        Renderer2D.flush();
    }

    @Override
    public void clean()
    {
        scene_.stop();
    }
}
