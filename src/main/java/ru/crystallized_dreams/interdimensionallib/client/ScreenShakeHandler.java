package ru.crystallized_dreams.interdimensionallib.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ScreenShakeHandler {
    private static double intensity = 0.0;
    private static int duration = 0;
    private static int curTick = 0;
    private static int sequenceRate = 0;
    private static final Random rand = new Random();

    public static boolean isActive() {return curTick<duration;}

    public static void start(double newIntensity, int newDuration, int newRate, boolean override, boolean notify) {
        if(!override&&isActive()) return;
        intensity=newIntensity;sequenceRate=newRate;duration=newDuration;curTick=0;
        if(notify) InterdimensionalLib.LOGGER.info("Started screen shake with intensity {} (rate: {}) for {} ticks", intensity, sequenceRate, duration);
    }
    public static void stop() {
        intensity=0;
        duration=0;
        curTick=0;
        sequenceRate=0;
    }
    public static void update() {if(isActive()) curTick++;}
    public static double getOffset() {
        if(!isActive()) return 0.0;
        //Current intensity will be decreased every tick.
        double curIntensity = intensity;
        if(sequenceRate==-1) curIntensity = intensity*(1.0-(double)curTick/(double)duration);
        else if(sequenceRate==1) curIntensity = intensity*((double)curTick/(double)duration);
        return (rand.nextBoolean()?1:-1)*(rand.nextDouble()*2.0-1.0)*curIntensity;
    }
}
