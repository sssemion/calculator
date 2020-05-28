package com.sssemion.calculator.conv;

import android.content.Context;
import android.os.Bundle;

import com.sssemion.calculator.calc.Calculator;
import com.sssemion.calculator.calc.KeyType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainConverter {

    public String type1, type2;
    public String value, result;

    public Converter conv;
    private Context context;


    public MainConverter(Converter conv, Context context) {
        this.conv = conv;
        this.context = context;
    }

    private String formatDouble(double val) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(12);
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setGroupingUsed(true);
        return formatter.format(val);
    }


    public void saveInstanceState(Bundle outState) {
        outState.putString("type1", type1);
        outState.putString("type2", type2);
        outState.putString("value", value);
        outState.putString("result", result);
    }


    public void restoreInstanceState(Bundle savedInstanceState) {
        type1 = savedInstanceState.getString("type1");
        type2 = savedInstanceState.getString("type2");
        value = savedInstanceState.getString("value");
        result = savedInstanceState.getString("result");
//        updateFields(); TODO
//        updateResult(result);
    }
}
