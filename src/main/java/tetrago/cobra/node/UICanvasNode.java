package tetrago.cobra.node;

public abstract class UICanvasNode extends Node2D
{
    void drawUIElements(UICanvas canvas)
    {
        onDrawUIElement(canvas);

        for(Node node : nodes())
        {
            if(node instanceof UICanvasNode)
            {
                ((UICanvasNode)node).drawUIElements(canvas);
            }
        }
    }

    public abstract void onDrawUIElement(UICanvas canvas);
}
