package com.sssemion.calculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sssemion.calculator.R;
import com.sssemion.calculator.calc.MainCalculator;
import com.sssemion.calculator.conv.Converter;
import com.sssemion.calculator.conv.MainConverter;
import com.sssemion.calculator.nav.ParentNavigationActivity;

public class TranActivity extends ParentNavigationActivity implements Converter {

    MainConverter conv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        conv = new MainConverter(this, getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        conv.saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        conv.restoreInstanceState(savedInstanceState);
    }

    @Override
    public void setFirstValue(String value, Context context) {

    }

    @Override
    public void setSecondValue(String value, Context context) {

    }
}
