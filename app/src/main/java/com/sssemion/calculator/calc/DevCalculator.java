package com.sssemion.calculator.calc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sssemion.calculator.R;

public class DevCalculator extends MainCalculator {
    private NumeralSystem currentNumeralSystem;

    private long baseValue;
    private long secondValue;

    private DevCalculatorInterface calc;

    public DevCalculator(DevCalculatorInterface calc, Context context) {
        super(calc, context);
        this.calc = calc;
        this.context = context;
    }

    public void setCurrentNumeralSystem(NumeralSystem numSys) {
        this.currentNumeralSystem = numSys;
        updateResult(baseValue);
    }

    @Override
    public void handleOperation(View view) {
        KeyType operation = getOperationBySign(((Button) view).getText().toString());
        if (lastKey == KeyType.DIGIT) {
            handleResult();
        }

        resetValue = true;
        lastKey = operation;
        lastOperation = operation;
    }

    @Override
    protected void calculateResult() {
        calculateResult(true);
    }

    @Override
    protected void calculateResult(boolean update) {
        if (update) updateFormula();
        if (lastOperation == null)
            return;
        Long result = Operation.binaryOperation(lastOperation, baseValue, secondValue);
        if (result != null) {
            updateResult(result);
        }
        isFirstOperation = false;
    }

    @Override
    public void handleEquals() {
        if (lastKey == KeyType.EQUALS)
            calculateResult();
        if (lastKey != KeyType.DIGIT)
            return;

        secondValue = parseInt(displayedNumber);
        calculateResult();
        lastKey = KeyType.EQUALS;
    }

    @Override
    void updateResult(Double value) {
    }

    private void updateResult(long value) {
        updateResult(value, true);
    }

    private void updateResult(long value, boolean updateBase) {
        displayedNumber = formatInt(value);
        calc.setValue(displayedNumber, context);
        calc.setNumeralsValues(Long.toHexString(value), Long.toString(value),
                Long.toOctalString(value), Long.toBinaryString(value), context);
        if (updateBase)
            baseValue = value;
    }

    @Override
    void handleResult() {
        secondValue = parseInt(displayedNumber);
        calculateResult();
        baseValue = parseInt(displayedNumber);
    }

    @Override
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

        updateResult(parseInt(newValue));
    }

    @Override
    public void handleReset() {
        resetValues();
        updateResult(0);
        setFormula("");
    }

    @Override
    protected void updateFormula() {
        String first = formatInt(baseValue);
        String second = formatInt(secondValue);
        String sign = getSign(lastOperation);

        if (!"".equals(sign)) {
            if (secondValue == 0 && sign.equals("/")) {
                Toast.makeText(context, context.getString(R.string.divide_by_zero_error),
                        Toast.LENGTH_SHORT).show();
            }
            setFormula(first + sign + second);
        }
    }

    @Override
    protected void addDigit(int number) {
        String oldValue = displayedNumber;
        displayedNumber += Integer.toString(number, currentNumeralSystem.base());
        try {
            updateResult(parseInt(displayedNumber), false);
        } catch (NumberFormatException e) {
            updateResult(parseInt(oldValue));
        }
    }

    private String formatInt(long value) {
        return Long.toString(value, currentNumeralSystem.base());
    }

    @Override
    String formatString(String str) {
        long val = parseInt(str);
        return formatInt(val);
    }

    private long parseInt(String str) {
        return str.isEmpty() ? 0 : Long.parseLong(str, currentNumeralSystem.base());
    }

    @Override
    public void onNumberClick(View view) {
        Button button = (Button) view;

        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValueIfNeeded();

        if (button.getText().equals(context.getString(R.string.digit0)))
            zeroClicked();
        else
            addDigit(Integer.parseInt(button.getText().toString(), currentNumeralSystem.base()));
    }

    @Override
    public void switchSign() {
        if (displayedNumber.isEmpty())
            return;
        baseValue *= -1;
        updateResult(baseValue);
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        outState.putSerializable("currentNumeralSystem", currentNumeralSystem);
    }

    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        super.restoreInstanceState(savedInstanceState);
        currentNumeralSystem = (NumeralSystem) savedInstanceState.getSerializable("currentNumeralSystem");
        updateResult(baseValue);
    }


    protected String getSign(KeyType operation) {
        if (operation == null)
            return "";
        switch (operation) {
            case PLUS:
                return context.getString(R.string.operationAdd);
            case MINUS:
                return context.getString(R.string.operationSubtract);
            case MULTIPLY:
                return context.getString(R.string.operationMultiply);
            case DIVIDE:
                return context.getString(R.string.operationDivide);
            case LSHIFT:
                return context.getString(R.string.operationLeftShift);
            case RSHIFT:
                return context.getString(R.string.operationRightShift);
            case AND:
                return context.getString(R.string.operationAND);
            case OR:
                return context.getString(R.string.operationOR);
            case XOR:
                return context.getString(R.string.operationXOR);
            default:
                return "";
        }
    }

    protected KeyType getOperationBySign(String sign) {
        if (context.getString(R.string.operationAdd).equals(sign)) {
            return KeyType.PLUS;
        } else if (context.getString(R.string.operationSubtract).equals(sign)) {
            return KeyType.MINUS;
        } else if (context.getString(R.string.operationMultiply).equals(sign)) {
            return KeyType.MULTIPLY;
        } else if (context.getString(R.string.operationDivide).equals(sign)) {
            return KeyType.DIVIDE;
        } else if (context.getString(R.string.operationLeftShift).equals(sign)) {
            return KeyType.LSHIFT;
        } else if (context.getString(R.string.operationRightShift).equals(sign)) {
            return KeyType.RSHIFT;
        } else if (context.getString(R.string.operationAND).equals(sign)) {
            return KeyType.AND;
        } else if (context.getString(R.string.operationOR).equals(sign)) {
            return KeyType.OR;
        } else if (context.getString(R.string.operationXOR).equals(sign)) {
            return KeyType.XOR;
        } return null;
    }
}
