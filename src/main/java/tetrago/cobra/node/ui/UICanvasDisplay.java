package tetrago.cobra.node.ui;

import tetrago.cobra.node.Node;

public class UICanvasDisplay extends Node
{
    public UICanvas target;

    @Override
    public void update()
    {
        super.update();

        if(target != null)
        {
            target.onDraw(null);
        }
    }
}
