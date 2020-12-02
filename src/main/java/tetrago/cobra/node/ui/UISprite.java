package tetrago.cobra.node.ui;

import org.joml.Vector4f;
import tetrago.cobra.graphics.Batch2D;
import tetrago.cobra.graphics.Color;
import tetrago.cobra.graphics.TextureClip;

public class UISprite extends UINode
{
    public Vector4f color = Color.WHITE;
    public TextureClip texture;

    @Override
    protected void onDraw(Batch2D batch)
    {
        if(texture == null)
        {
            batch.drawQuad(color, rectangle().position, 0, rectangle().size);
        }
        else
        {
            batch.drawQuad(texture, color, rectangle().position, 0, rectangle().size);
        }
    }
}
