package com.sssemion.calculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.sssemion.calculator.calc.Calculator;
import com.sssemion.calculator.calc.EngineerCalculator;
import com.sssemion.calculator.R;
import com.sssemion.calculator.nav.ParentNavigationActivity;

public class EngineerActivity extends ParentNavigationActivity implements Calculator {

    TextView formulaField; // текстовое поле для вывода формулы
    TextView resultField; // текстовое поле для вывода результата

    private EngineerCalculator calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer);
        formulaField = findViewById(R.id.formula);
        resultField = findViewById(R.id.result);
        calc = new EngineerCalculator(this, getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        calc.saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calc.restoreInstanceState(savedInstanceState);
    }

    public void onOperationClick(View view) {
        calc.handleOperation(view);
    }

    public void onEqualsClick(View view) {
        calc.handleEquals();
    }

    public void onNumberClick(View view) {
        calc.onNumberClick(view);
    }

    public void onBackspaceClick(View view) {
        calc.handleBackspace();
    }

    public void onResetClick(View view) {
        calc.handleReset();
    }

    public void onSignSwitchClick(View view) {
        calc.switchSign();
    }

    public void onEClick(View view){
        calc.onEClick();
    }

    public void onPIClick(View view){
        calc.onPIClick();
    }

    public void onSwitchDegRadClick(View view) {
        calc.onSwitchDegRadClick(view);
    }

    @Override
    public void setValue(String value, Context context) {
        resultField.setText(value);
    }

    @Override
    public void setFormula(String value, Context context) {
        formulaField.setText(value);
    }

    @Override
    public void setFormula(Spanned value, Context context) {
        formulaField.setText(value);
    }
}
