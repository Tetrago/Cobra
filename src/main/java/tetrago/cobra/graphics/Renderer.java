package tetrago.cobra.graphics;

public class Renderer
{
    public static void clear()
    {
        RenderStack.current().clear();
    }
}
