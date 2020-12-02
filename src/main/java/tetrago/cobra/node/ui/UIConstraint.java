package tetrago.cobra.node.ui;

public abstract class UIConstraint
{
    public enum Side
    {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public abstract void apply(UINode node);
}
