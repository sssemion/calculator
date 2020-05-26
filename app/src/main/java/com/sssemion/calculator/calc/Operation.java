package com.sssemion.calculator.calc;

public class Operation {
    public static Double binaryOperation(KeyType operation, double a, double b) {
        switch (operation) {
            case PLUS: return a + b;
            case MINUS: return a - b;
            case DIVIDE: return b != 0 ? a / b : 0;
            case MULTIPLY: return a * b;
            case PERCENT: return percent(a, b);
            case POWER: return power(a, b);
            default: return null;
        }
    }

    public static Double unaryOperation(KeyType operation, double a) {
        switch (operation) {
            case ROOT: return Math.sqrt(a);
            case FACTORIAL: return factorial(a);
            default: return null;
        }
    }

    private static double percent(double a, double b) {
        double result = 0.0;
        if (b != 0.0) {
            result = a / 100 * b;
        }
        return result;
    }

    private static double power(double a, double b) {
        double result = Math.pow(a, b);
        if (Double.isInfinite(result) || Double.isNaN(result))
            result = 0.0;
        return result;
    }

    private static double factorial(double a) {
        double result = 0;
        if (a == 0.0 || a == 1.0){
            return result;
        }
        for (int i = 2; i < a; i++)
            result *= i;
        return result;
    }
}
