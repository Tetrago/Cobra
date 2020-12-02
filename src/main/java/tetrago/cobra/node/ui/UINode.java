package tetrago.cobra.node.ui;

import tetrago.cobra.graphics.Batch2D;
import tetrago.cobra.node.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class UINode extends Node
{
    private final UIRectangle rect_ = new UIRectangle();
    private final List<UIConstraint> constraints_ = new ArrayList<>();

    public void addConstraint(UIConstraint constraint)
    {
        constraints_.add(constraint);
    }

    public void refresh()
    {
        for(UIConstraint constraint : constraints_)
        {
            constraint.apply(this);
        }

        for(Node node : nodes())
        {
            if(node instanceof UINode)
            {
                ((UINode)node).refresh();
            }
        }
    }

    protected abstract void onDraw(Batch2D batch);

    public UIRectangle rectangle()
    {
        return rect_;
    }
}
