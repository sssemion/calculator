package com.sssemion.calculator.conv;

import android.content.Context;

public interface CurrencyInterface extends Converter {
    void setFirstCurrencyName(String currencyName, Context context);

    void setSecondCurrencyName(String currencyName, Context context);

    void setValueInRubs(String value, Context context);
}
