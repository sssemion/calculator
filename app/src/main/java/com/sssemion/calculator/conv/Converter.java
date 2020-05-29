package com.sssemion.calculator.conv;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public interface Converter {
    void setFirstValue(String value, Context context);

    void setSecondValue(String value, Context context);

    void setSpinnersValues(ArrayList<String> values, Context context);
}
