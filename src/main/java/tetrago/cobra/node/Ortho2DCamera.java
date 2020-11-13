package tetrago.cobra.node;

import org.joml.Matrix4f;
import tetrago.cobra.core.Program;
import tetrago.cobra.graphics.Window;

public class Ortho2DCamera extends Camera
{
    private Matrix4f view_ = new Matrix4f();

    @Override
    public void start()
    {
        Window window = Program.get().window();

        final float scale = 10.0f / window.width();

        float we = window.width() * scale * 0.5f;
        float he = window.height() * scale * 0.5f;

        view_ = new Matrix4f().translate(0, 0, 1).ortho(-we, we, -he, he, 0.1f, 1000);
    }

    @Override
    public Matrix4f view()
    {
        return view_;
    }
}
