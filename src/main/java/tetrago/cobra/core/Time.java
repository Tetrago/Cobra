package tetrago.cobra.core;

public class Time
{
    private static long lastTime_ = 0;
    private static double deltaTime_ = 0;

    static void tick()
    {
        long now = System.currentTimeMillis();
        deltaTime_ = (now - lastTime_) / 1000.0;
        lastTime_ = now;
    }

    public static double deltaTime() { return deltaTime_; }
}
