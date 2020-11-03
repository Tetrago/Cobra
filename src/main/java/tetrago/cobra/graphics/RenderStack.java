package tetrago.cobra.graphics;

import java.util.Stack;

public class RenderStack
{
    private static final Stack<Graphics> stack_ = new Stack<>();

    public static void push(Graphics graphics)
    {
        stack_.add(graphics);
        graphics.use();
    }

    public static void pop()
    {
        stack_.pop();

        if(!stack_.empty())
        {
            stack_.peek().use();
        }
    }

    public static Graphics current()
    {
        return stack_.peek();
    }
}
