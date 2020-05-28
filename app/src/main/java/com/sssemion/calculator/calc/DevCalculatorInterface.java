package com.sssemion.calculator.calc;

import android.content.Context;

public interface DevCalculatorInterface extends Calculator {
    void setNumeralsValues(String hex, String dec, String oct, String bin, Context context);
}
