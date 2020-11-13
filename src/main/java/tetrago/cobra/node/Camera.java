package tetrago.cobra.node;

import org.joml.Matrix4f;

public abstract class Camera extends Node
{
    public abstract Matrix4f view();
}
