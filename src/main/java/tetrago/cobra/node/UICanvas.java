package tetrago.cobra.node;

import org.joml.Vector2f;
import tetrago.cobra.graphics.Batch2D;

public class UICanvas extends Node2D
{
    private final Batch2D batch_ = new Batch2D();

    @Override
    public void update()
    {
        for(Node node : nodes())
        {
            if(node instanceof UICanvasNode)
            {
                ((UICanvasNode)node).drawUIElements(this);
            }
        }
    }

    public Vector2f resolvePosition(UICanvasNode node)
    {
        return node.getPosition();
    }

    public float resolveRotation(UICanvasNode node)
    {
        return node.getRotation();
    }

    public Vector2f resolveScale(UICanvasNode node)
    {
        return node.getScale();
    }

    public Batch2D batch() { return batch_; }
}
