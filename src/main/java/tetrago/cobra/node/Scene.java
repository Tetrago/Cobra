package tetrago.cobra.node;

import tetrago.cobra.core.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.function.Consumer;

public class Scene
{
    private final Node root_;

    public Scene()
    {
        root_ = new Node();
        root_.rename("root");
    }

    public void callInternal(Consumer<Node> consumer)
    {
        Stack<Node> stack = new Stack<>();
        stack.add(root_);

        while(!stack.empty())
        {
            Node node = stack.pop();
            stack.addAll(node.nodes());
            consumer.accept(node);
        }
    }

    public void start()
    {
        callInternal(Node::start);
    }

    public void stop()
    {
        callInternal(Node::stop);
    }

    public void update()
    {
        callInternal(Node::update);
    }

    public Node root() { return root_; }
}
