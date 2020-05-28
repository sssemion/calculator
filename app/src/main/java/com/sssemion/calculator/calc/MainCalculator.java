package com.sssemion.calculator.calc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sssemion.calculator.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainCalculator {
    String displayedNumber = "";
    String displayedFormula = "";

    KeyType lastKey = null;
    KeyType lastOperation = null;

    boolean isFirstOperation = false;
    boolean resetValue = false;

    double baseValue = 0.0;
    double secondValue = 0.0;

    Calculator calc;
    Context context;

    public MainCalculator(Calculator calc, Context context) {
        this.calc = calc;
        this.context = context;
    }

    public void handleOperation(View view) {
        KeyType operation = getOperationBySign(((Button) view).getText().toString());
        if (lastKey == KeyType.DIGIT && lastOperation != null && operation == KeyType.PERCENT) {
            KeyType tempOp = lastOperation;
            handlePercent();
            lastKey = tempOp;
            lastOperation = tempOp;
        } else if (lastKey == KeyType.DIGIT) {
            handleResult();
        }

        resetValue = true;
        lastKey = operation;
        lastOperation = operation;
    }

    public void handleEquals() {
        if (lastKey == KeyType.EQUALS)
            calculateResult();
        if (lastKey != KeyType.DIGIT)
            return;

        secondValue = parseDouble(displayedNumber);
        calculateResult();
        lastKey = KeyType.EQUALS;
    }

    protected void calculateResult() {
        calculateResult(true);
    }

    protected void calculateResult(boolean update) {
        if (update) updateFormula();
        if (lastOperation == null)
            return;
        Double result = Operation.binaryOperation(lastOperation, baseValue, secondValue);
        if (result == null) {
            result = Operation.unaryOperation(lastOperation, baseValue);
        }
        if (result != null) {
            updateResult(result);
        }
        isFirstOperation = false;
    }

    void handleResult() {
        secondValue = parseDouble(displayedNumber);
        calculateResult();
        baseValue = parseDouble(displayedNumber);
    }

    void updateResult(Double value) {
        displayedNumber = formatDouble(value);
        calc.setValue(displayedNumber, context);
        baseValue = value;
    }

    protected void updateFormula() {
        String first = formatDouble(baseValue);
        String second = formatDouble(secondValue);
        String sign = getSign(lastOperation);

        if (!"".equals(sign)) {
            if (secondValue == 0.0 && sign.equals("/")) {
                Toast.makeText(context, context.getString(R.string.divide_by_zero_error),
                        Toast.LENGTH_SHORT).show();
            }
            setFormula(first + sign + second);
        }
    }

    void handlePercent() {
        secondValue = Double.parseDouble(displayedNumber);
        Double result = Operation.binaryOperation(KeyType.PERCENT, baseValue, secondValue);
        setFormula(formatDouble(baseValue) + getSign(lastOperation) + formatDouble(secondValue) + "%");
        if (result != null)
            secondValue = result;
        calculateResult(false);
    }

    protected String getSign(KeyType operation) {
        if (operation == null)
            return "";
        switch (operation) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case PERCENT:
                return "%";
            default:
                return "";
        }
    }

    protected KeyType getOperationBySign(String sign) {
        switch (sign) {
            case "+":
                return KeyType.PLUS;
            case "-":
                return KeyType.MINUS;
            case "*":
                return KeyType.MULTIPLY;
            case "/":
                return KeyType.DIVIDE;
            case "%":
                return KeyType.PERCENT;
            default:
                return null;
        }
    }

    void setFormula(String value) {
        calc.setFormula(value, context);
        displayedFormula = value;
    }

    protected void addDigit(int number) {
        displayedNumber += Integer.toString(number);
        calc.setValue(formatString(displayedNumber), context);
    }

    String formatString(String str) {
        if (str.contains(","))
            return str;
        double val = parseDouble(str);
        return formatDouble(val);
    }

    String formatDouble(double val) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(12);
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setGroupingUsed(true);
        return formatter.format(val);
    }

    double parseDouble(String str) {
        str = str.replace(",", ".");
        str = str.replace(" ", "");
        return str.isEmpty() ? 0 : Double.parseDouble(str);
    }

    void resetValueIfNeeded() {
        if (resetValue)
            displayedNumber = "0";

        resetValue = false;
    }

    private void signClicked() {
        if (!displayedNumber.contains(",")) {
            displayedNumber += ",";
        }
        calc.setValue(displayedNumber, context);
    }

    void zeroClicked() {
        if (!displayedNumber.equals("0"))
            addDigit(0);
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;

        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValueIfNeeded();

        if (button.getText().equals(context.getString(R.string.signComma)))
            signClicked();
        else if (button.getText().equals(context.getString(R.string.digit0)))
            zeroClicked();
        else
            addDigit(Integer.parseInt(button.getText().toString()));
    }

    public void handleBackspace() {
        String oldValue = displayedNumber;
        String newValue = "0";
        int len = oldValue.length();
        int minLen = 1;
        if (oldValue.contains("-"))
            minLen++;
        if (len > minLen) {
            newValue = oldValue.substring(0, len - 1);
        }

        newValue = newValue.replaceAll(",$", "");
        updateResult(parseDouble(newValue));
    }

    public void handleReset() {
        resetValues();
        displayedNumber = "";
        calc.setValue(displayedNumber, context);
        setFormula("");
    }

    public void switchSign() {
        if (displayedNumber.isEmpty())
            return;
        else if (displayedNumber.startsWith("-"))
            displayedNumber = displayedNumber.replaceAll("^-", "");
        else
            displayedNumber = displayedNumber.replaceAll("^", "-");
        displayedNumber = formatString(displayedNumber);
        baseValue *= -1;
        calc.setValue(displayedNumber, context);
    }

    void resetValues() {
        baseValue = 0.0;
        secondValue = 0.0;
        resetValue = false;
        lastOperation = null;
        lastKey = null;
        displayedNumber = "";
        displayedFormula = "";
        isFirstOperation = true;
    }

    public void saveInstanceState(Bundle outState) {
        outState.putDouble("baseValue", baseValue);
        outState.putDouble("secondValue", secondValue);
        outState.putBoolean("resetValue", resetValue);
        outState.putSerializable("lastOperation", lastOperation);
        outState.putSerializable("lastKey", lastKey);
        outState.putString("displayedNumber", displayedNumber);
        outState.putString("displayedFormula", displayedFormula);
        outState.putBoolean("isFirstOperation", isFirstOperation);
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        baseValue = savedInstanceState.getDouble("baseValue");
        secondValue = savedInstanceState.getDouble("seconsValue");
        resetValue = savedInstanceState.getBoolean("resetValue");
        lastOperation = (KeyType) savedInstanceState.getSerializable("lastOperation");
        lastKey = (KeyType) savedInstanceState.getSerializable("lastKey");
        displayedNumber = savedInstanceState.getString("displayedNumber");
        displayedFormula = savedInstanceState.getString("displayedFormula");
        isFirstOperation = savedInstanceState.getBoolean("isFirstOperation");
        updateFormula();
        updateResult(baseValue);
    }
}
