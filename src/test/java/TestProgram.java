import tetrago.cobra.core.Program;
import tetrago.cobra.graphics.Color;
import tetrago.cobra.graphics.Renderer;
import tetrago.cobra.graphics.Renderer2D;
import tetrago.cobra.node.Camera;
import tetrago.cobra.node.Orthographic2DCamera;
import tetrago.cobra.node.Scene;
import tetrago.cobra.node.SpriteRenderer;
import tetrago.cobra.node.ui.*;

public class TestProgram extends Program
{
    private Scene scene_;
    private Camera camera_;
    private PlayerController player_;
    private UICanvas canvas_;

    public TestProgram()
    {
        super("Test");
    }

    @Override
    public void clean()
    {

    }

    @Override
    public void setup()
    {
        scene_ = new Scene();
        camera_ = scene_.root().add(Orthographic2DCamera.class, "Main Camera");

        player_ = scene_.root().add(PlayerController.class, "Player");
        player_.add(SpriteRenderer.class).color = Color.GREEN;

        canvas_ = scene_.root().add(UICanvas.class, "Canvas");
        UISprite spr = canvas_.add(UISprite.class, "Sprite");
        spr.color = Color.BLUE;
        spr.addConstraint(new UIPixelConstraint(UIConstraint.Side.LEFT, 50));
        spr.addConstraint(new UIPercentageConstraint(UIConstraint.Side.RIGHT, 25));
        spr.rectangle().size.y = 50;

        scene_.root().add(UICanvasDisplay.class).target = canvas_;
    }

    @Override
    public void onStart()
    {
        scene_.start();
    }

    @Override
    public void onStop()
    {
        scene_.stop();
    }

    @Override
    public void update()
    {
        Renderer.clear();

        Renderer2D.prepare(camera_);
        scene_.update();
        Renderer2D.flush();
    }
}
