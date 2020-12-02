package tetrago.cobra.node;

import org.joml.Matrix4f;
import tetrago.cobra.core.Program;
import tetrago.cobra.event.Events;
import tetrago.cobra.event.WindowEvent;
import tetrago.cobra.event.WindowResizeEvent;
import tetrago.cobra.graphics.Window;

public class Orthographic2DCamera extends Camera
{
    private Matrix4f view_ = new Matrix4f();

    public Orthographic2DCamera()
    {
        Events.WINDOW.listen(e ->
        {
            if(e.type() == WindowEvent.Type.RESIZE)
            {
                WindowResizeEvent wre = (WindowResizeEvent)e;
                calculateMatrix(wre.width(), wre.height());
            }
        });
    }

    private void calculateMatrix(float w, float h)
    {
        final float scale = 10.0f / w;

        float we = w * scale * 0.5f;
        float he = h * scale * 0.5f;

        view_ = new Matrix4f().translate(0, 0, 1).ortho(-we, we, -he, he, 0.1f, 1000);
    }

    @Override
    public void start()
    {
        Window window = Program.get().window();
        calculateMatrix(window.width(), window.height());
    }

    @Override
    public Matrix4f view()
    {
        return view_;
    }
}
