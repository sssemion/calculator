package com.sssemion.calculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sssemion.calculator.R;
import com.sssemion.calculator.conv.Converter;
import com.sssemion.calculator.conv.CurrencyInterface;
import com.sssemion.calculator.conv.MainConverter;
import com.sssemion.calculator.nav.ParentNavigationActivity;

import java.util.ArrayList;
import java.util.Objects;

public class CurrencyActivity extends ParentNavigationActivity implements CurrencyInterface {

    MainConverter conv;

    Spinner firstCurrencySpinner;
    Spinner secondCurrencySpinner;

    TextView firstValue;
    TextView firstCurrencyName;
    TextView secondValue;
    TextView secondCurrencyName;
    TextView valueInRubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        firstCurrencySpinner = findViewById(R.id.firstCurrencySpinner);
        secondCurrencySpinner = findViewById(R.id.secondCurrencySpinner);

        firstCurrencySpinner.setOnItemSelectedListener(new listener());
        secondCurrencySpinner.setOnItemSelectedListener(new listener());

        firstValue = findViewById(R.id.firstValue);
        firstCurrencyName = findViewById(R.id.firstCurrencyName);

        secondValue = findViewById(R.id.secondValue);
        secondCurrencyName = findViewById(R.id.secondCurrencyName);

        valueInRubs = findViewById(R.id.rubValue);

        conv = new MainConverter(this, getApplicationContext());
    }

    public void onResetClick(View view) {
        conv.handleReset();
    }

    public void onBackspaceClick(View view) {
        conv.handleBackspace();
    }

    private class listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.firstCurrencySpinner)
                conv.setFirstCurrency(position);
            else if (parent.getId() == R.id.secondCurrencySpinner)
                conv.setSecondCurrency(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public void setFirstCurrencyName(String currencyName, Context context) {
        firstCurrencyName.setText(currencyName);
    }

    @Override
    public void setSecondCurrencyName(String currencyName, Context context) {
        secondCurrencyName.setText(currencyName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setFirstValue(String value, Context context) {
        firstValue.setText(value);
    }

    @Override
    public void setSecondValue(String value, Context context) {
        secondValue.setText(value);
    }

    @Override
    public void setValueInRubs(String value, Context context) {
        valueInRubs.setText(value);
    }

    @Override
    public void setSpinnersValues(ArrayList<String> values, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapter.addAll(values);
        firstCurrencySpinner.setAdapter(adapter);
        firstCurrencySpinner.setSelection(values.indexOf("USD"));
        secondCurrencySpinner.setAdapter(adapter);
        secondCurrencySpinner.setSelection(values.indexOf("EUR"));
    }

    public void onNumberClick(View view) {
        conv.onNumberClick(view);
    }
}
