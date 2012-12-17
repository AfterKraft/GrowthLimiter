package com.afterkraft.growthlimiter.api;

public class GrowthLimiterWorld {
    
    public boolean grassBoolean = true;  // Is grass growth control enabled in this world?
    public boolean mycliumBoolean = true;   // Is mycelium growth control enabled in this world?
    public boolean vineBoolean = true;   // Is vine growth control enabled in this world?
    public int grassGrowth = 50;  // World grass growth percentage
    public int myceliumGrowth = 50;  // World mycelium growth percentage
    public int vineGrowth = 50;   // World vine growth percentage
    public int vineMaxDistance = 10;   // World vine maximum growth length
    public double grassGrowthPercent = 0.5;   // World growth percentage for calculation
    public double myceliumGrowthPercent = 0.5;   // World growth percentage for calculation
    public double vineGrowthPercent = 0.5;   // World growth percentage for calculation
    public String worldName = "world";   // World string name for processing

    @Override
    public String toString() {
        return worldName;
    }
}
