import org.joml.Matrix4f;
import tetrago.cobra.core.Program;
import tetrago.cobra.graphics.*;
import tetrago.cobra.node.*;

public class TestProgram extends Program
{
    private Matrix4f view_;
    private Scene scene_;
    private Camera camera_;
    private PlayerController player_;

    public TestProgram()
    {
        super("Test");
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
