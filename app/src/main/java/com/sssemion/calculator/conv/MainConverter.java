package com.sssemion.calculator.conv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sssemion.calculator.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MainConverter {

    private CurrencyInterface conv;
    private Context context;

    private ArrayList<Currency> currencies;
    private Date lastUpdated;

    private String displayedFirstValue = "0";

    private Currency firstCurrency;
    private Currency secondCurrency;


    public MainConverter(CurrencyInterface conv, Context context) {
        this.conv = conv;
        this.context = context;

        parseCurrencies();
        updateCurrencyDataIfNeeded();
    }

    private void setSpinners() {
        ArrayList<String> values = new ArrayList<>();
        for (Currency currency :
                currencies) {
            values.add(currency.getCharCode());
        }
        conv.setSpinnersValues(values, context);
    }

    private long parseInt(String str) {
        return str.isEmpty() ? 0 : Long.parseLong(str);
    }


    @SuppressLint("DefaultLocale")
    private void updateResult() {
        long firstValue = parseInt(displayedFirstValue);
        int nominal = firstCurrency.getNominal();
        double secondValue = firstValue * secondCurrency.getNominal() * firstCurrency.getValue() / nominal / secondCurrency.getValue();
        double valueInRubs = firstValue * firstCurrency.getValue() / nominal;
        conv.setFirstValue(displayedFirstValue, context);
        conv.setSecondValue(String.format("%.2f", secondValue), context);
        conv.setValueInRubs(String.format("%.2f", valueInRubs), context);
    }

    private void parseCurrencies() {
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException ignored) {
        }
        if (factory == null)
            return;
        factory.setNamespaceAware(true);
        XmlPullParser currency_data;
        try {
            currency_data = factory.newPullParser();
            currency_data.setInput(new FileInputStream(new File(context.getFilesDir(),
                    "currency_data.xml")), "utf-8");
        } catch (XmlPullParserException | FileNotFoundException e) {
            return;
        }
        currencies = new ArrayList<>();
        int eventType;
        try {
            eventType = currency_data.getEventType();
        } catch (XmlPullParserException e) {
            return;
        }

        Currency currency = null;

        // итерационно обходим xml файл пока не наткнемся на тип события 'конец документа'
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            // обрабатываем события начала документа
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG: {
                    name = currency_data.getName();
                    if (name.equals("Valute")) {
                        currency = new Currency();
                    } else if (name.equals("ValCurs")) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                            lastUpdated = format.parse("29.05.2020");
                        } catch (ParseException ignored) {
                        }
                    } else if (currency != null) {
                        String value;
                        switch (name) {
                            case "NumCode":
                                value = getNextValue(currency_data);
                                if (value != null)
                                    currency.setNumCode(Integer.parseInt(value));
                                break;
                            case "CharCode":
                                value = getNextValue(currency_data);
                                if (value != null)
                                    currency.setCharCode(value);
                                break;
                            case "Nominal":
                                value = getNextValue(currency_data);
                                if (value != null)
                                    currency.setNominal(Integer.parseInt(value));
                                break;
                            case "Name":
                                value = getNextValue(currency_data);
                                if (value != null)
                                    currency.setName(value);
                                break;
                            case "Value":
                                value = getNextValue(currency_data);
                                if (value != null)
                                    currency.setValue(Double.parseDouble(value.replace(",", ".")));
                                break;
                        }
                    }
                    break;
                }
                case XmlPullParser.END_TAG:
                    name = currency_data.getName();
                    if (name.equalsIgnoreCase("Valute") && currency != null) {
                        currencies.add(currency);
                    }
                    break;
            }
            // переходим к следующему событию внутри XML
            try {
                eventType = currency_data.next();
            } catch (IOException | XmlPullParserException ignored) {
            }
        }

        setSpinners();

    }

    private void updateCurrencyDataIfNeeded() {
        if (lastUpdated == null || lastUpdated.before(new Date())) {
            DownloadData downloadData = new DownloadData();
            downloadData.execute("https://www.cbr.ru/scripts/XML_daily.asp");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            Toast.makeText(context, "Данные актуальны на " + formatter.format(lastUpdated), Toast.LENGTH_SHORT).show();
        }
    }

    private String getNextValue(XmlPullParser parser) {
        try {
            return parser.nextText();
        } catch (IOException | XmlPullParserException e) {
            return null;
        }
    }

    public void handleReset() {
        displayedFirstValue = "0";
        updateResult();
    }

    public void handleBackspace() {
        String oldValue = displayedFirstValue;
        String newValue = "0";
        int len = oldValue.length();
        int minLen = 1;
        if (oldValue.contains("-"))
            minLen++;
        if (len > minLen) {
            newValue = oldValue.substring(0, len - 1);
        }

        displayedFirstValue = newValue;
        updateResult();
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            File file = new File(context.getFilesDir(), "currency_data.xml");
            StringBuilder oldData = new StringBuilder();
            try {
                FileReader reader = new FileReader(file);
                int c;
                while ((c = reader.read()) != -1) {
                    oldData.append((char) c);
                }
                FileWriter writer = new FileWriter(file, false);
                writer.write(s);
                writer.flush();
                parseCurrencies();
            } catch (IOException | NullPointerException e) {
                try {
                    FileWriter writer = new FileWriter(file, false);
                    writer.write(oldData.toString());
                    writer.flush();
                    writer.close();
                    parseCurrencies();
                } catch (IOException ignored) {
                }
            } finally {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                Toast.makeText(context, "Данные актуальны на " + formatter.format(lastUpdated), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String content;
            try {
                content = downloadXML(strings[0]);
                return content;
            } catch (IOException e) {
                return null;
            }
        }

        private String downloadXML(String urlPath) throws IOException {
            StringBuilder xmlResult = new StringBuilder();
            BufferedReader reader = null;
            try {
                URL url = new URL(urlPath);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "windows-1251"));
                String line;
                while ((line = reader.readLine()) != null) {
                    xmlResult.append(line);
                }
                return xmlResult.toString();
            } catch (SecurityException | IOException ignored) {
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return null;
        }
    }

    private void zeroClicked() {
        if (!displayedFirstValue.equals("0"))
            addDigit(0);
    }

    private void addDigit(int number) {
        String oldValue = displayedFirstValue;
        if (displayedFirstValue.equals("0"))
            displayedFirstValue = Integer.toString(number);
        else
            displayedFirstValue += Integer.toString(number);
        try {
            updateResult();
        } catch (NumberFormatException e) {
            displayedFirstValue = oldValue;
        }
        updateResult();
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        if (button.getText().equals(context.getString(R.string.digit0)))
            zeroClicked();
        else
            addDigit(Integer.parseInt(button.getText().toString()));
    }

    public void setFirstCurrency(int currencyIndex) {
        this.firstCurrency = currencies.get(currencyIndex);
        conv.setFirstCurrencyName(firstCurrency.getName(), context);
        try {
            updateResult();
        } catch (NullPointerException ignored) {}
    }

    public void setSecondCurrency(int currencyIndex) {
        this.secondCurrency = currencies.get(currencyIndex);
        conv.setSecondCurrencyName(secondCurrency.getName(), context);
        try {
            updateResult();
        } catch (NullPointerException ignored) {}
    }
}
