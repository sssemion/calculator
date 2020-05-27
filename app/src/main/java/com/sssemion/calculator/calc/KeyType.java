package com.sssemion.calculator.calc;

public enum KeyType {
    EQUALS,

    PLUS(true), MINUS(true), MULTIPLY(true),
    DIVIDE(true), PERCENT(false), POWER(true),
    ROOT(false), FACTORIAL(false),

    COS(false), SIN(false), TG(false),
    CTG(false), LG(false), LN(false),
    POWER_OF_2(false), POWER_OF_10(false),
    REVERSE(false),

    DEG, RAD,

    DIGIT;

    private Boolean binaryOperation;

    KeyType(Boolean binaryOperation) {
        this.binaryOperation = binaryOperation;
    }

    KeyType() {
        this.binaryOperation = null;
    }

    boolean isBinary() {
        return this.binaryOperation;
    }
}
