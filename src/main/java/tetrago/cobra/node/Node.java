package tetrago.cobra.node;

import tetrago.cobra.core.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node
{
    private String name_ = getClass().getName();
    private Node parent_;
    private final List<Node> nodes_ = new ArrayList<>();

    public void start() {}
    public void stop() {}
    public void update() {}

    public void rename(String name)
    {
        name_ = name;
    }

    public <T extends Node> T add(Class<T> klass)
    {
        return add(klass, klass.getName());
    }

    public <T extends Node> T add(Class<T> klass, String name)
    {
        assert !has(klass);

        Node node = null;

        try
        {
            node = klass.getDeclaredConstructor().newInstance();
            node.rename(name);
            node.parent_ = this;

            nodes_.add(node);
        }
        catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            Logger.COBRA.error("Failed to create node %s: %s", klass.getName(), e.getMessage());
            e.printStackTrace();
        }

        return (T)node;
    }

    public <T extends Node> T get(String name)
    {
        assert has(name);

        return (T)nodes_.stream().filter(n -> n.name_ == name).findFirst().get();
    }

    public <T extends Node> T get(Class<T> klass)
    {
        assert has(klass);

        return (T)nodes_.stream().filter(n -> klass.isAssignableFrom(n.getClass())).findFirst().get();
    }

    public <T extends Node> boolean has(String name)
    {
        return nodes_.stream().anyMatch(n -> n.name_ == name);
    }

    public <T extends Node> boolean has(Class<T> klass)
    {
        return nodes_.stream().anyMatch(n -> klass.isAssignableFrom(n.getClass()));
    }

    public String name() { return name_; }
    public Node parent() { return parent_; }
    public List<Node> nodes() { return Collections.unmodifiableList(nodes_); }
}
