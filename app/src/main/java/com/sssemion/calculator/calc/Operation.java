package com.sssemion.calculator.calc;

import com.sssemion.calculator.calc.exceptions.LogarithmOfNotPositiveNumber;
import com.sssemion.calculator.calc.exceptions.FactorialOfNegativeNumber;
import com.sssemion.calculator.calc.exceptions.SQRTOfNegativeNumber;
import com.sssemion.calculator.calc.exceptions.TrigonometricValueIsNotDefined;

import static java.lang.Math.abs;

class Operation {
    static Double binaryOperation(KeyType operation, double a, double b) {
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

    static Long binaryOperation(KeyType operation, long a, long b) {
        Double result = binaryOperation(operation, (double) a, (double) b);
        if (result != null)
            return (long) Math.floor(result);
        switch (operation) {
            case LSHIFT: return a << b;
            case RSHIFT: return a >> b;
            case AND: return a & b;
            case OR: return a | b;
            case XOR: return a ^ b;
            default: return null;
        }
    }

    static Double unaryOperation(KeyType operation, double a) {
        return unaryOperation(operation, a, KeyType.DEG);
    }

    static Double unaryOperation(KeyType operation, double a, KeyType measureUnit) {
        switch (operation) {
            case ROOT: return root(a);
            case FACTORIAL: return factorial(a);
            case SIN: return Math.sin(measureUnit == KeyType.RAD ? a : Math.toRadians(a));
            case COS: return Math.cos(measureUnit == KeyType.RAD ? a : Math.toRadians(a));
            case TG: return tg(a, measureUnit);
            case CTG: return ctg(a, measureUnit);
            case LG: return lg(a);
            case LN: return ln(a);
            case POWER_OF_2: return Math.pow(2, a);
            case POWER_OF_10: return Math.pow(10, a);
            case REVERSE: return a != 0 ? 1 / a : 0;
            default: return null;
        }
    }

    private static Double lg(double a) {
        if (a <= 0)
            throw new LogarithmOfNotPositiveNumber();
        return Math.log10(a);
    }

    private static Double ln(double a) {
        if (a <= 0)
            throw new LogarithmOfNotPositiveNumber();
        return Math.log(a);
    }

    private static Double tg(double a, KeyType measureUnit) {
        if (measureUnit == KeyType.DEG)
            a = Math.toRadians(a);
        a %= 2 * Math.PI;
        if (abs(Math.PI / 2 - a) < 0.0001 || abs(Math.PI * 3 / 2 - a) < 0.0001)
            throw new TrigonometricValueIsNotDefined();
        double tan = abs(Math.tan(a));
        return tan < 0.00000000001 ? 0 : tan;

    }

    private static Double ctg(double a, KeyType measureUnit) {
        if (measureUnit == KeyType.DEG)
            a = Math.toRadians(a);
        a %= 2 * Math.PI;
        if (abs(0 - a) < 0.0001 || abs(Math.PI - a) < 0.0001 || abs(Math.PI * 2 - a) < 0.0001)
            throw new TrigonometricValueIsNotDefined();
        double ctg = abs(1 / Math.tan(a));
        return ctg < 0.00000000001 ? 0 : ctg;
    }

    private static Double root(double a) {
        if (a < 0)
            throw new SQRTOfNegativeNumber();
        return Math.sqrt(a);
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
        double result = 1;
        if (a == 0.0 || a == 1.0) {
            return result;
        } else if (a < 0) {
            throw new FactorialOfNegativeNumber();
        }
        for (int i = 2; i <= a; i++)
            result *= i;
        return result;
    }
}
