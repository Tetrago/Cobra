package tetrago.cobra.node;

import org.joml.Vector4f;
import tetrago.cobra.graphics.Renderer2D;

public class SpriteRenderer extends Node2D
{
    public Vector4f color;

    @Override
    public void update()
    {
        Renderer2D.drawQuad(color, getPosition(), getRotation(), getScale());
    }
}
