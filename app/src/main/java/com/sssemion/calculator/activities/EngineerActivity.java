package com.sssemion.calculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sssemion.calculator.calc.Calculator;
import com.sssemion.calculator.calc.EngineerCalculator;
import com.sssemion.calculator.calc.MainCalculator;
import com.sssemion.calculator.nav.ParentNavigationActivity;
import com.sssemion.calculator.R;

public class EngineerActivity extends MainActivity{

    EngineerCalculator calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer);
        formulaField = (TextView) findViewById(R.id.formula);
        resultField = (TextView) findViewById(R.id.result);
        calc = new EngineerCalculator(this, getApplicationContext());
    }

}
