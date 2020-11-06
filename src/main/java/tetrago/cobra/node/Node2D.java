package tetrago.cobra.node;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Node2D extends Node
{
    public Vector2f position = new Vector2f();
    public float rotation = 0;
    public Vector2f scale = new Vector2f(1);

    public Vector2f getPosition()
    {
        if(parent() instanceof Node2D)
        {
            Node2D node = (Node2D)parent();

            Vector2f np = node.getPosition();
            float nr = node.getRotation();
            Vector2f ns = node.getScale();

            Vector4f vec = new Matrix4f()
                    .translate(np.x, np.y, 0)
                    .rotate(nr, 0, 0, 1)
                    .scale(ns.x, ns.y, 0).transform(new Vector4f(position.x, position.y, 0, 1));

            return new Vector2f(vec.x, vec.y);
        }
        else
        {
            return position;
        }
    }

    public float getRotation()
    {
        float rot = rotation;

        if(parent() instanceof Node2D)
        {
            rot += ((Node2D)parent()).rotation;
        }

        return rot;
    }

    public Vector2f getScale()
    {
        if(parent() instanceof Node2D)
        {
            return ((Node2D)parent()).scale.mul(scale);
        }

        return scale;
    }
}
