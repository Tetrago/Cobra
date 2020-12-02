package tetrago.cobra.node.ui;

public class UIPercentageConstraint extends UIConstraint
{
    private final UIConstraint.Side side_;
    private final float percent_;

    public UIPercentageConstraint(UIConstraint.Side side, float percent)
    {
        side_ = side;
        percent_ = percent;
    }

    private void move(UIRectangle rect, UIRectangle canvas, Side side, float p)
    {
        float x = canvas.size.x * p / 100;
        float y = canvas.size.y * p / 100;

        switch(side)
        {
            case TOP:
            {
                float currentPos = rect.position.y;
                rect.position.y = y;
                rect.size.y += currentPos - rect.position.y;
            } break;
            case RIGHT:
            {
                rect.size.x = canvas.size.x - x - rect.position.x;
            } break;
            case BOTTOM:
            {
                rect.size.y = canvas.size.y - y - rect.position.y;
            } break;
            case LEFT:
            {
                float currentPos = rect.position.x;
                rect.position.x += x;
                rect.size.x += currentPos - rect.position.x;
            } break;
        }
    }

    @Override
    public void apply(UINode node)
    {
        assert node.parent() instanceof UINode;
        UINode parent = (UINode)node.parent();

        move(node.rectangle(), parent.rectangle(), side_, percent_);
    }
}
