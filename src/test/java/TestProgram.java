import org.joml.Matrix4f;
import tetrago.cobra.core.Cell;
import tetrago.cobra.core.Program;
import tetrago.cobra.graphics.*;
import tetrago.cobra.io.Resource;
import tetrago.cobra.node.*;

public class TestProgram extends Program
{
    private Matrix4f view_;
    private Scene scene_;
    private Camera camera_;
    private PlayerController player_;

    private Texture color_;
    private Framebuffer framebuffer_;
    private Shader shader_;
    private VertexArray vao_;

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

        color_ = new Texture().create(get().window().width(), get().window().height(), Texture.Format.RGBA8, null);
        framebuffer_ = new Framebuffer(new Framebuffer.Attachment[]{ new Framebuffer.Attachment(
                Framebuffer.AttachmentType.COLOR, new Cell<>(color_), 0) });
        shader_ = new Shader(Resource.toString(getClass().getResourceAsStream("fx.shader")));
        vao_ = new VertexArray();
        vao_.attachVertexBuffer(new Cell<>(new Buffer(new float[]{ -1, -1, 1, -1, 1, 1, -1, 1 }, Buffer.Usage.STATIC)),
                new VertexArray.Layout(0, 2, VertexArray.ValueType.FLOAT));
        vao_.attachVertexBuffer(new Cell<>(new Buffer(new float[]{ 0, 0, 1, 0, 1, 1, 0, 1 }, Buffer.Usage.STATIC)),
                new VertexArray.Layout(1, 2, VertexArray.ValueType.FLOAT));
        vao_.setIndexBuffer(new Cell<>(new Buffer(new int[]{ 0, 1, 2, 0, 2, 3 }, Buffer.Usage.STATIC)));
    }

    @Override
    public void update()
    {
        Renderer.clear();

        Graphics g = RenderStack.current();
        g.bind(framebuffer_);

        Renderer2D.prepare(camera_);

        scene_.update();

        Renderer2D.flush();

        g.unbind();

        g.setShader(shader_);
        g.setVertexArray(vao_);
        g.setTexture(0, color_);

        shader_.upload("u_sampler", 0);

        g.drawIndexed(6);
    }

    @Override
    public void clean()
    {
        framebuffer_.close();

        scene_.stop();
    }
}
