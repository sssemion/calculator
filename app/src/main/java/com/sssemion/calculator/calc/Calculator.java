package com.sssemion.calculator.calc;

import android.content.Context;
import android.text.Spanned;

public interface Calculator {
    void setValue(String value, Context context);

    void setFormula(String value, Context context);

    void setFormula(Spanned value, Context context);
}
