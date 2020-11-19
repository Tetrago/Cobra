package tetrago.cobra.graphics;

import org.joml.Vector2f;
import org.joml.Vector4f;
import tetrago.cobra.node.Camera;

public class Renderer2D
{
    private static Batch2D batch_;

    public static void init()
    {
        batch_ = new Batch2D();
    }

    public static void free()
    {
        batch_.close();
    }

    public static void prepare(Camera camera)
    {
        batch_.setMatrix(camera.view());
    }

    public static void flush()
    {
        batch_.execute();
    }

    public static void drawQuad(Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        batch_.drawQuad(color, pos, radians, scale);
    }

    public static void drawQuad(TextureClip texture, Vector2f pos, float radians, Vector2f scale)
    {
        drawQuad(texture, Color.WHITE, pos, radians, scale);
    }

    public static void drawQuad(TextureClip texture, Vector4f color, Vector2f pos, float radians, Vector2f scale)
    {
        drawQuad(texture, color, pos, radians, scale);
    }
}
