package com.sssemion.calculator.calc;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sssemion.calculator.R;
import com.sssemion.calculator.calc.exceptions.FactorialOfNegativeNumber;
import com.sssemion.calculator.calc.exceptions.LogarithmOfNotPositiveNumber;
import com.sssemion.calculator.calc.exceptions.SQRTOfNegativeNumber;
import com.sssemion.calculator.calc.exceptions.TrigonometricValueIsNotDefined;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EngineerCalculator extends MainCalculator {
    private KeyType currentMeasureUnit = KeyType.DEG;

    public EngineerCalculator(Calculator calc, Context context) {
        super(calc, context);
        this.calc = calc;
        this.context = context;
    }

    @Override
    public void handleOperation(View view) {
        KeyType operation = getOperationBySign(((Button) view).getText().toString());
        if (lastKey == KeyType.DIGIT && lastOperation != null && operation == KeyType.PERCENT) {
            KeyType tempOp = lastOperation;
            handlePercent();
            lastKey = tempOp;
            lastOperation = tempOp;
        } else if (lastKey == KeyType.DIGIT && operation.isBinary()) {
            handleResult();
        }

        resetValue = true;
        lastKey = operation;
        lastOperation = operation;

        if (!operation.isBinary()) {
            baseValue = parseDouble(displayedNumber);
            calculateResult();
            secondValue = baseValue;
            resetValue = false;
        }
    }

    @Override
    protected void calculateResult(boolean update) {
        if (update) updateFormula();
        if (lastOperation == null)
            return;
        Double result = Operation.binaryOperation(lastOperation, baseValue, secondValue);
        if (result == null) {
            try {
                result = Operation.unaryOperation(lastOperation, baseValue, currentMeasureUnit);
            } catch (SQRTOfNegativeNumber e) {
                Toast.makeText(context, R.string.sqrt_of_negative_error, Toast.LENGTH_SHORT).show();
            } catch (FactorialOfNegativeNumber e) {
                Toast.makeText(context, R.string.factorial_of_negative_error, Toast.LENGTH_SHORT).show();
            } catch (LogarithmOfNotPositiveNumber e) {
                Toast.makeText(context, R.string.logarithm_of_not_positive_error, Toast.LENGTH_SHORT).show();
            } catch (TrigonometricValueIsNotDefined e) {
                Toast.makeText(context, R.string.trigonometric_value_not_defined_error, Toast.LENGTH_SHORT).show();
            }
        }
        if (result != null) {
            updateResult(result);
        } else {
            handleReset();
        }
        isFirstOperation = false;
    }

    @Override
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
            case PERCENT:
                return context.getString(R.string.operationPercent);
            case POWER:
                return context.getString(R.string.operationPower);
            case ROOT:
                return context.getString(R.string.operationRoot);
            case FACTORIAL:
                return context.getString(R.string.operationFactorial);
            case SIN:
                return context.getString(R.string.operationSin);
            case COS:
                return context.getString(R.string.operationCos);
            case TG:
                return context.getString(R.string.operationTg);
            case CTG:
                return context.getString(R.string.operationCtg);
            case LG:
                return context.getString(R.string.operationLg);
            case LN:
                return context.getString(R.string.operationLn);
            case POWER_OF_2:
                return context.getString(R.string.operationPowerOf2);
            case POWER_OF_10:
                return context.getString(R.string.operationPowerOf10);
            case REVERSE:
                return context.getString(R.string.operationReverse);
            default:
                return "";
        }
    }

    @Override
    protected KeyType getOperationBySign(String sign) {
        if (context.getString(R.string.operationAdd).equals(sign)) {
            return KeyType.PLUS;
        } else if (context.getString(R.string.operationSubtract).equals(sign)) {
            return KeyType.MINUS;
        } else if (context.getString(R.string.operationMultiply).equals(sign)) {
            return KeyType.MULTIPLY;
        } else if (context.getString(R.string.operationDivide).equals(sign)) {
            return KeyType.DIVIDE;
        } else if (context.getString(R.string.operationPercent).equals(sign)) {
            return KeyType.PERCENT;
        } else if (context.getString(R.string.operationPower).equals(sign)) {
            return KeyType.POWER;
        } else if (context.getString(R.string.operationRoot).equals(sign)) {
            return KeyType.ROOT;
        } else if (context.getString(R.string.operationFactorial).equals(sign)) {
            return KeyType.FACTORIAL;
        } else if (context.getString(R.string.operationSin).equals(sign)) {
            return KeyType.SIN;
        } else if (context.getString(R.string.operationCos).equals(sign)) {
            return KeyType.COS;
        } else if (context.getString(R.string.operationTg).equals(sign)) {
            return KeyType.TG;
        } else if (context.getString(R.string.operationCtg).equals(sign)) {
            return KeyType.CTG;
        } else if (context.getString(R.string.operationLg).equals(sign)) {
            return KeyType.LG;
        } else if (context.getString(R.string.operationLn).equals(sign)) {
            return KeyType.LN;
        } else if (context.getString(R.string.operationPowerOf2).equals(sign)) {
            return KeyType.POWER_OF_2;
        } else if (context.getString(R.string.operationPowerOf10).equals(sign)) {
            return KeyType.POWER_OF_10;
        } else if (context.getString(R.string.operationReverse).equals(sign)) {
            return KeyType.REVERSE;
        }
        return null;
    }

    @Override
    protected void updateFormula() {
        String first = formatDouble(baseValue);
        String second = formatDouble(secondValue);
        String sign = getSign(lastOperation);
        if (lastOperation == null)
            return;
        switch (lastOperation) {
            case ROOT:
                setFormula(sign + first);
                break;
            case FACTORIAL:
                setFormula(first + sign);
                break;
            case SIN:
            case COS:
            case TG:
            case CTG:
            case LG:
            case LN:
                setFormula(sign + "(" + first + ")");
                break;
            case POWER: {
                String value = first + "<sup><small>" + second + "</small></sup>";
                calc.setFormula(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT), context);
                displayedFormula = value;
                break;
            }
            case POWER_OF_2: {
                String value = "2<sup><small>" + first + "</small></sup>";
                calc.setFormula(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT), context);
                displayedFormula = value;
                break;
            }
            case POWER_OF_10: {
                String value = "10<sup><small>" + first + "</small></sup>";
                calc.setFormula(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT), context);
                displayedFormula = value;
                break;
            }
            case REVERSE: {
                if (baseValue == 0.0)
                    Toast.makeText(context, context.getString(R.string.divide_by_zero_error),
                            Toast.LENGTH_SHORT).show();
                setFormula("1/" + first);
                break;
            }
            default: {
                if (secondValue == 0.0 && lastOperation == KeyType.DIVIDE) {
                    Toast.makeText(context, context.getString(R.string.divide_by_zero_error),
                            Toast.LENGTH_SHORT).show();
                }
                setFormula(first + sign + second);
            }
        }
    }

    private void addE() {
        BigDecimal bd = new BigDecimal(Double.toString(Math.E));
        double out = bd.setScale(12, RoundingMode.DOWN).doubleValue();
        displayedNumber += Double.toString(out);
        calc.setValue(formatString(displayedNumber), context);
    }

    private void addPI() {
        BigDecimal bd = new BigDecimal(Double.toString(Math.PI));
        double out = bd.setScale(12, RoundingMode.DOWN).doubleValue();
        displayedNumber += Double.toString(out);
        calc.setValue(formatString(displayedNumber), context);
    }

    public void onEClick() {
        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValue = true;
        resetValueIfNeeded();
        addE();
    }

    public void onPIClick() {
        if (lastKey == KeyType.EQUALS)
            lastOperation = KeyType.EQUALS;

        lastKey = KeyType.DIGIT;
        resetValue = true;
        resetValueIfNeeded();
        addPI();
    }

    public void onSwitchDegRadClick(View view) {
        Button button = (Button) view;
        KeyType oldUnit = currentMeasureUnit;
        if (currentMeasureUnit == KeyType.DEG) {
            currentMeasureUnit = KeyType.RAD;
            button.setText(context.getString(R.string.switchRad));
        } else {
            currentMeasureUnit = KeyType.DEG;
            button.setText(context.getString(R.string.switchDeg));
        }
        if (lastOperation == null)
            return;
        double a;
        switch (lastOperation) {
            case SIN:
                a = Math.asin(secondValue);
                break;
            case COS:
                a = Math.acos(secondValue);
                break;
            case TG:
                a = Math.atan(secondValue);
                break;
            case CTG:
                a = Math.atan(1 / secondValue);
                break;
            default:
                return;
        }
        a = Math.toDegrees(a);
        updateResult(Operation.unaryOperation(lastOperation, a, currentMeasureUnit));
    }
}
