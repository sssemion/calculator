package com.sssemion.calculator.calc;

public enum NumeralSystem {
    BIN(2), OCT(8), DEC(10), HEX(16);

    private int base;

    NumeralSystem(int base) {
        this.base = base;
    }

    public int base() {
        return this.base;
    }
}
