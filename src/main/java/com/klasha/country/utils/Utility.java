package com.klasha.country.utils;

import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class Utility {

    public  HashMap<String, Double> readFileCSV() throws IOException {
        HashMap<String, Double> currencies = new HashMap<>();
        InputStream resource = new ClassPathResource(
                "exchange_rate.csv").getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String curr = values[0] + "-" + values[1];
                double amount = getAmountFromArray(values);
                currencies.put(curr, amount);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    private  double getAmountFromArray(String[] values) {
        String amount = Arrays.stream(values)
                .skip(2)
                .collect(Collectors.joining(""));
        return Double.parseDouble(amount);
    }


}
