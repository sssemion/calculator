package com.sssemion.calculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sssemion.calculator.calc.DevCalculator;
import com.sssemion.calculator.calc.DevCalculatorInterface;
import com.sssemion.calculator.calc.NumeralSystem;
import com.sssemion.calculator.nav.ParentNavigationActivity;
import com.sssemion.calculator.R;

public class DevActivity extends ParentNavigationActivity implements DevCalculatorInterface {

    TextView formulaField; // текстовое поле для вывода формулы
    TextView resultField; // текстовое поле для вывода результата

    RadioButton hexSystem;
    RadioButton decSystem;
    RadioButton octSystem;
    RadioButton binSystem;
    RadioGroup numeralSystemGroup;

    private DevCalculator calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        formulaField = findViewById(R.id.formula);
        resultField = findViewById(R.id.result);

        calc = new DevCalculator(this, getApplicationContext());

        hexSystem = findViewById(R.id.hexSystem);
        decSystem = findViewById(R.id.decSystem);
        octSystem = findViewById(R.id.octSystem);
        binSystem = findViewById(R.id.binSystem);
        numeralSystemGroup = findViewById(R.id.numeralSystemsGroup);
        numeralSystemGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button != null) {
                        button.setTextColor(getColor(R.color.colorTextSecondary));
                    }
                }
                RadioButton button = findViewById(checkedId);
                button.setTextColor(getColor(R.color.colorTextPrimary));
                switch (checkedId) {
                    case R.id.hexSystem:
                        changeNumeralSystem(NumeralSystem.HEX);
                        break;
                    case R.id.decSystem:
                        changeNumeralSystem(NumeralSystem.DEC);
                        break;
                    case R.id.octSystem:
                        changeNumeralSystem(NumeralSystem.OCT);
                        break;
                    case R.id.binSystem:
                        changeNumeralSystem(NumeralSystem.BIN);
                        break;
                }
            }
        });
        decSystem.toggle();
        setValue("0", getApplicationContext());
        setNumeralsValues("0", "0", "0", "0", getApplicationContext());
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

    public void changeNumeralSystem(NumeralSystem numSys) {
        switch (numSys) {
            case BIN: {
                for (int i = 2; i < 16; i++) {
                    Button button = findViewById(getResources().getIdentifier(
                            "digit" + Integer.toString(i, 16).toUpperCase(),
                            "id", "com.sssemion.calculator"));
                    button.setEnabled(false);
                }
                break;
            }
            case OCT:
            case DEC: {
                for (int i = 1; i < numSys.base(); i++) {
                    Button button = findViewById(getResources().getIdentifier(
                            "digit" + Integer.toString(i, 16).toUpperCase(),
                            "id", "com.sssemion.calculator"));
                    button.setEnabled(true);
                }
                for (int i = numSys.base(); i < 16; i++) {
                    Button button = findViewById(getResources().getIdentifier(
                            "digit" + Integer.toString(i, 16).toUpperCase(),
                            "id", "com.sssemion.calculator"));
                    button.setEnabled(false);
                }
                break;
            }
            case HEX: {
                for (int i = 0; i < 16; i++) {
                    Button button = findViewById(getResources().getIdentifier(
                            "digit" + Integer.toString(i, 16).toUpperCase(),
                            "id", "com.sssemion.calculator"));
                    button.setEnabled(true);
                }
            }
        }
        calc.setCurrentNumeralSystem(numSys);
    }

    @Override
    public void setValue(String value, Context context) {
        resultField.setText(value);
    }

    @Override
    public void setNumeralsValues(String hex, String dec, String oct, String bin, Context context) {
        hexSystem.setText(hex);
        decSystem.setText(dec);
        octSystem.setText(oct);
        binSystem.setText(bin);
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

