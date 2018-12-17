package com.mw.raumships.common.calc;

public class PowerUnitCalculator {
    public static double toEU(int forgeEnergy) {
        return forgeEnergy * 0.25;
    }

    public static double toJ(int forgeEnergy) {
        return forgeEnergy * 2.5;
    }

    public static double toMJ(int forgeEnergy) {
        return forgeEnergy * 0.1;
    }

    public static double togJ(int forgeEnergy) {
        return forgeEnergy * 1.6;
    }
}
