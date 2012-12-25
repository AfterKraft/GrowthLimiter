/*
 * Copyright 2012 Gabriel Harris-Rouquette. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this list of
 *     conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list
 *     of conditions and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.afterkraft.growthlimiter.api;

/**
 * Represents a configured world for Bukkit
 * @author gabizou
 *
 */
public class GrowthLimiterWorld {
    
    public boolean grassBoolean = true;  // Is grass growth control enabled in this world?
    public boolean myceliumBoolean = true;   // Is mycelium growth control enabled in this world?
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
    
    public void setName(String name) {
        this.worldName = name;
    }
    
    public void setGrassGrowth(int grassGrowth) {
        this.grassGrowth = grassGrowth;
    }
    
    public void setMyceliumGrowth(int myceliumGrowth) {
        this.myceliumGrowth = myceliumGrowth;
    }
    
    public void setVineGrowth(int vineGrowth) {
        this.vineGrowth = vineGrowth;
    }
    
    public void setVineMaxDistance(int vineMaxDistance) {
        this.vineMaxDistance = vineMaxDistance;
    }
    
    public int getGrassGrowth() {
        return grassGrowth;
    }
    
    public int getMyceliumGrowth() {
        return myceliumGrowth;
    }
    
    public int getVineGrowth() {
        return vineGrowth;
    }
    
    public int getVineMaxDistance() {
        return vineMaxDistance;
    }
    
    public void calculatePercentages() {
        grassGrowthPercent = grassGrowth/100;
        myceliumGrowthPercent = myceliumGrowth/100;
        vineGrowthPercent = vineGrowth/100;
    }
}
