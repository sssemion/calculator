package com.sssemion.calculator.conv;

import android.content.Context;

public interface Converter {
    void setFirstValue(String value, Context context);

    void setSecondValue(String value, Context context);
}
