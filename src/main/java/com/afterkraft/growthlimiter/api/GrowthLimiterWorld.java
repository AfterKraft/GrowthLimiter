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

import com.google.common.base.Objects;

/**
 * Represents a configured world for Bukkit
 * @author gabizou
 *
 */
public class GrowthLimiterWorld {
    
    public boolean grassBoolean = true;  // Is grass growth control enabled in this world?
    public boolean myceliumBoolean = true;   // Is mycelium growth control enabled in this world?
    public boolean vineBoolean = true;   // Is vine growth control enabled in this world?
    public boolean wheatBoolean = true; // Is wheat growth control enabled in this world?
    public int grassGrowth = 50;  // World grass growth percentage
    public int myceliumGrowth = 50;  // World mycelium growth percentage
    public int vineGrowth = 50;   // World vine growth percentage
    public int vineMaxDistance = 10;   // World vine maximum growth length
    public int wheatGrowth = 50; // World wheat growth percentage
    public int sugarCaneGrowth = 50;
    public int cactusGrowth = 50;
    public double grassGrowthPercent = 0.5;   // World growth percentage for calculation
    public double myceliumGrowthPercent = 0.5;   // World growth percentage for calculation
    public double vineGrowthPercent = 0.5;   // World growth percentage for calculation
    public double wheatGrowthPercent = 0.5; // World growth percentage for calculation
    public double sugarGrowthPercent = 0.5;
    public double cactusGrowthPercent = 0.5;
    public String worldName = "world";   // World string name for processing

    @Override
    public String toString() {
        return worldName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(grassBoolean, myceliumBoolean, vineBoolean, wheatBoolean, grassGrowth, myceliumGrowth, vineGrowth, vineMaxDistance,
                                wheatGrowth, sugarCaneGrowth, cactusGrowth, grassGrowthPercent, myceliumGrowthPercent, vineGrowthPercent,
                                wheatGrowthPercent, worldName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GrowthLimiterWorld other = (GrowthLimiterWorld) obj;
        return Objects.equal(this.grassBoolean, other.grassBoolean)
               && Objects.equal(this.myceliumBoolean, other.myceliumBoolean)
               && Objects.equal(this.vineBoolean, other.vineBoolean)
               && Objects.equal(this.wheatBoolean, other.wheatBoolean)
               && Objects.equal(this.grassGrowth, other.grassGrowth)
               && Objects.equal(this.myceliumGrowth, other.myceliumGrowth)
               && Objects.equal(this.vineGrowth, other.vineGrowth)
               && Objects.equal(this.vineMaxDistance, other.vineMaxDistance)
               && Objects.equal(this.wheatGrowth, other.wheatGrowth)
               && Objects.equal(this.sugarCaneGrowth, other.sugarCaneGrowth)
               && Objects.equal(this.cactusGrowth, other.cactusGrowth)
               && Objects.equal(this.grassGrowthPercent, other.grassGrowthPercent)
               && Objects.equal(this.myceliumGrowthPercent, other.myceliumGrowthPercent)
               && Objects.equal(this.vineGrowthPercent, other.vineGrowthPercent)
               && Objects.equal(this.wheatGrowthPercent, other.wheatGrowthPercent)
               && Objects.equal(this.worldName, other.worldName);
    }
}
