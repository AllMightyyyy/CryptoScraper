package org.example.analyzer;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CryptoDataReader {

    public static List<Crypto> readCryptoData(String filePath) throws IOException, CsvValidationException {
        List<Crypto> cryptocurrencies = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // Skip header row

            while ((line = reader.readNext()) != null) {
                String currencyName = line[0].trim();
                BigDecimal price = parseBigDecimal(line[1]);
                BigDecimal change = parseBigDecimal(line[2]);
                BigDecimal changePercent = parsePercentage(line[3]);
                BigDecimal marketCap = parseBigDecimal(line[4]);
                BigDecimal volume = parseBigDecimal(line[5]);
                BigDecimal supply = parseBigDecimal(line[6]);
                String timestamp = line[7].trim();

                cryptocurrencies.add(new Crypto(currencyName, price, change, changePercent, marketCap, volume, supply, timestamp));
            }
        }
        return cryptocurrencies;
    }

    // Helper method to clean and convert values to BigDecimal, handling "B", "M", "T", and "k" suffixes
    private static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty() || value.equals("-")) {
            return BigDecimal.ZERO;
        }

        value = value.replace(",", "").trim();  // Remove commas and trim spaces

        BigDecimal multiplier = BigDecimal.ONE;

        if (value.endsWith("T")) {
            multiplier = BigDecimal.valueOf(1_000_000_000_000L);
            value = value.replace("T", "");
        } else if (value.endsWith("B")) {
            multiplier = BigDecimal.valueOf(1_000_000_000L);
            value = value.replace("B", "");
        } else if (value.endsWith("M")) {
            multiplier = BigDecimal.valueOf(1_000_000L);
            value = value.replace("M", "");
        } else if (value.endsWith("k")) {
            multiplier = BigDecimal.valueOf(1_000L);
            value = value.replace("k", "");
        }

        try {
            return new BigDecimal(value).multiply(multiplier);
        } catch (NumberFormatException e) {
            System.err.println("Invalid numeric value: " + value);
            return BigDecimal.ZERO;
        }
    }

    // Helper method to parse percentage values (strip '%' and commas, and convert to BigDecimal)
    private static BigDecimal parsePercentage(String value) {
        if (value == null || value.isEmpty() || value.equals("-")) {
            return BigDecimal.ZERO;
        }

        try {
            value = value.replace("%", "").replace(",", "").trim();
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid percentage value: " + value);
            return BigDecimal.ZERO;
        }
    }
}
