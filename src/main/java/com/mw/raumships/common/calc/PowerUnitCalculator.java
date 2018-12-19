package com.mw.raumships.common.calc;

public class PowerUnitCalculator {
    public static double toEU(long forgeEnergy) {
        return forgeEnergy * 0.25;
    }

    public static double toJ(long forgeEnergy) {
        return forgeEnergy * 2.5;
    }

    public static double toMJ(long forgeEnergy) {
        return forgeEnergy * 0.1;
    }

    public static double togJ(long forgeEnergy) {
        return forgeEnergy * 1.6;
    }
}
