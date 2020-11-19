package tetrago.cobra.node;

import org.joml.Math;
import org.joml.Vector4f;
import tetrago.cobra.graphics.Color;
import tetrago.cobra.graphics.Renderer2D;
import tetrago.cobra.graphics.TextureClip;

public class SpriteRenderer extends Node2D
{
    public Vector4f color = Color.WHITE;
    public TextureClip texture;

    @Override
    public void update()
    {
        if(texture != null)
        {
            Renderer2D.drawQuad(texture, color, getPosition(), Math.toRadians(getRotation()), getScale());
        }
        else
        {
            Renderer2D.drawQuad(color, getPosition(), Math.toRadians(getRotation()), getScale());
        }
    }
}
