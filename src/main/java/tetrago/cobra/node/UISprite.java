package tetrago.cobra.node;

import org.joml.Math;
import org.joml.Vector4f;
import tetrago.cobra.graphics.Color;
import tetrago.cobra.graphics.TextureClip;

public class UISprite extends UICanvasNode
{
    private Vector4f color_ = Color.WHITE;
    private TextureClip texture_;

    @Override
    public void onDrawUIElement(UICanvas canvas)
    {
        if(texture_ != null)
        {
            canvas.batch().drawQuad(texture_, color_, canvas.resolvePosition(this),
                    Math.toRadians(canvas.resolveRotation(this)), canvas.resolveScale(this));
        }
        else
        {
            canvas.batch().drawQuad(color_, canvas.resolvePosition(this),
                    Math.toRadians(canvas.resolveRotation(this)), canvas.resolveScale(this));
        }
    }
}
