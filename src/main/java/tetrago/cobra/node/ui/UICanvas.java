package tetrago.cobra.node.ui;

import org.joml.Matrix4f;
import tetrago.cobra.core.Logger;
import tetrago.cobra.core.Program;
import tetrago.cobra.event.Events;
import tetrago.cobra.event.WindowEvent;
import tetrago.cobra.event.WindowResizeEvent;
import tetrago.cobra.graphics.Batch2D;
import tetrago.cobra.graphics.Window;
import tetrago.cobra.node.Node;

public class UICanvas extends UINode
{
    private Batch2D batch_;

    public UICanvas()
    {
        this(Program.get().window());
    }

    public UICanvas(Window window)
    {
        UIRectangle r = rectangle();
        r.position.x = 0;
        r.position.y = 0;

        onWindowResize(window.width(), window.height());

        Events.WINDOW.listen(e ->
        {
            if(e.window() == window && e.type() == WindowEvent.Type.RESIZE)
            {
                WindowResizeEvent re = (WindowResizeEvent)e;
                onWindowResize(re.width(), re.height());
            }
        });
    }

    private void onWindowResize(float w, float h)
    {
        UIRectangle r = rectangle();

        r.size.x = w;
        r.size.y = h;
    }

    @Override
    public void addConstraint(UIConstraint constraint)
    {
        Logger.COBRA.warn("Attempting to add constraint to canvas");
    }

    @Override
    protected void onDraw(Batch2D batch)
    {
        Batch2D use = batch == null ? batch_ : batch;

        for(Node node : nodes())
        {
            if(node instanceof UINode)
            {
                UINode ui = (UINode)node;

                ui.refresh();
                ui.onDraw(use);
            }
        }

        if(use == batch_)
        {
            use.execute();
        }
    }

    @Override
    public void start()
    {
        super.start();

        batch_ = new Batch2D();
        batch_.setMatrix(new Matrix4f()
                .ortho(0, rectangle().size.x, rectangle().size.y, 0, 0.1f, 100f));
    }

    @Override
    public void stop()
    {
        super.stop();

        batch_.close();
    }

    public Batch2D batch() { return batch_; }
}
