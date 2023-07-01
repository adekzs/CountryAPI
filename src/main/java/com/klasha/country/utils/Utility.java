package com.klasha.country.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Utility {

    public static HashMap<String, Double> readFileCSV() {
        HashMap<String, Double> currencies = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/m.afolabi/Documents/cresters/Test Application/src/main/resources/exchange_rate.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String curr = values[0] + "-" + values[1];
                double amount = getAmountFromArray(values);
                currencies.put(curr, amount);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    private static double getAmountFromArray(String[] values) {
        String amount = Arrays.stream(values)
                .skip(2)
                .collect(Collectors.joining(""));
        return Double.parseDouble(amount);
    }


}
