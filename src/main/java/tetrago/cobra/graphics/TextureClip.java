package tetrago.cobra.graphics;

import org.joml.Vector2f;

public class TextureClip
{
    final Texture texture_;
    final Vector2f uv0_, uv1_;

    TextureClip(Texture texture, Vector2f uv0, Vector2f uv1)
    {
        texture_ = texture;
        uv0_ = uv0;
        uv1_ = uv1;
    }
}
