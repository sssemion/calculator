package com.sssemion.calculator.calc;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sssemion.calculator.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainCalculator {
    public String displayedNumber = "";
    public String displayedFormula = "";

    private KeyType lastKey = null;
    private KeyType lastOperation = null;

    private boolean isFirstOperation = false;
    private boolean resetValue = false;

    private double baseValue = 0.0;
    private double secondValue = 0.0;

    private Calculator calc;
    private Context context;

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
        } else if (lastKey == KeyType.DIGIT && operation != KeyType.ROOT && operation != KeyType.FACTORIAL && operation != KeyType.SIN && operation != KeyType.COS && operation != KeyType.LG) {
            handleResult();
        }

        resetValue = true;
        lastKey = operation;
        lastOperation = operation;

        if (operation == KeyType.ROOT || operation == KeyType.FACTORIAL || operation == KeyType.SIN || operation == KeyType.COS || operation == KeyType.LG) {
            baseValue = parseDouble(displayedNumber);
            calculateResult();
            resetValue = false;
        }
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

    private void calculateResult() {
        calculateResult(true);
    }

    private void calculateResult(boolean update) {
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

    public void handleResult() {
        secondValue = parseDouble(displayedNumber);
        calculateResult();
        baseValue = parseDouble(displayedNumber);
    }

    private void updateResult(Double value) {
        displayedNumber = formatDouble(value);
        calc.setValue(displayedNumber, context);
        baseValue = value;
    }

    private void updateFormula() {
        String first = formatDouble(baseValue);
        String second = formatDouble(secondValue);
        String sign = getSign(lastOperation);

        switch (sign) {
            case "√":
                setFormula(sign + first);
                break;
            case "!":
                setFormula(first + sign);
                break;
            case "":
                break;
            case "sin":
            case "cos":
            case "lg":
                setFormula(sign + "(" + first + ")");
                break;
            default: {
                if (secondValue == 0.0 && sign.equals("/")) {
                    Toast.makeText(context, context.getString(R.string.divide_by_zero_error),
                            Toast.LENGTH_SHORT).show();
                }
                setFormula(first + sign + second);
            }
        }
    }

    private void handlePercent() {
        secondValue = Double.parseDouble(displayedNumber);
        Double result = Operation.binaryOperation(KeyType.PERCENT, baseValue, secondValue);
        setFormula(formatDouble(baseValue) + getSign(lastOperation) + formatDouble(secondValue) + "%");
        if (result != null)
            secondValue = result;
        calculateResult(false);
    }

    private String getSign(KeyType operation) {
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
            case POWER:
                return "^";
            case ROOT:
                return "√";
            case FACTORIAL:
                return "!";
            case SIN:
                return "sin";
            case COS:
                return "cos";
            case TG:
                return "tg";
            case CTG:
                return "ctg";
            case LG:
                return "lg";
            default:
                return "";
        }
    }

    private KeyType getOperationBySign(String sign) {
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
            case "^":
                return KeyType.POWER;
            case "√":
                return KeyType.ROOT;
            case "!":
                return KeyType.FACTORIAL;
            case "sin":
                return KeyType.SIN;
            case "cos":
                return KeyType.COS;
            case "tg":
                return KeyType.TG;
            case "ctg":
                return KeyType.CTG;
            case "lg":
                return KeyType.LG;
            default:
                return null;
        }
    }

    private void setFormula(String value) {
        calc.setFormula(value, context);
        displayedFormula = value;
    }

    public void addDigit(int number) {
        displayedNumber += Integer.toString(number);
        calc.setValue(formatString(displayedNumber), context);
    }

    public void addE() {
        BigDecimal bd = new BigDecimal(Double.toString(Math.E));
        double out = bd.setScale(4, RoundingMode.DOWN).doubleValue();
        displayedNumber += Double.toString(out);
        calc.setValue(formatString(displayedNumber), context);
    }

    public void addPI() {
        BigDecimal bd = new BigDecimal(Double.toString(Math.PI));
        double out = bd.setScale(4, RoundingMode.DOWN).doubleValue();
        displayedNumber += Double.toString(out);
        calc.setValue(formatString(displayedNumber), context);
    }

    private String formatString(String str) {
        if (str.contains(","))
            return str;
        double val = parseDouble(str);
        return formatDouble(val);
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

    private double parseDouble(String str) {
        str = str.replace(",", ".");
        str = str.replace(" ", "");
        return Double.parseDouble(str);
    }

    private void resetValueIfNeeded() {
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

    private void zeroClicked() {
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

    public void onEClick(View view) {

        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValueIfNeeded();
            addE();
    }

    public void onPIClick(View view) {
        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValueIfNeeded();
        addPI();
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
        if (displayedNumber.startsWith("-"))
            displayedNumber = displayedNumber.replaceAll("^-", "");
        else
            displayedNumber = displayedNumber.replaceAll("^", "-");
        displayedNumber = formatString(displayedNumber);
        baseValue *= -1;
        calc.setValue(displayedNumber, context);
    }

    private void resetValues() {
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
