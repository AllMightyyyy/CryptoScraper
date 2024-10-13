package org.example.analyzer;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;

public class CryptoAnalysisApp {

    public static void main(String[] args) {
        try {
            // Read the cryptocurrency data from the CSV file
            List<Crypto> cryptocurrencies = CryptoDataReader.readCryptoData("crypto_data_2024-10-13_13-14.csv");

            // Initialize the CryptoAnalyzer with the data
            CryptoAnalyzer analyzer = new CryptoAnalyzer(cryptocurrencies);

            // Perform the analysis
            System.out.println("Total Market Cap: " + analyzer.totalMarketCap());
            System.out.println("Total Trading Volume: " + analyzer.totalTradingVolume());

            // Example: Display top 5 currencies by price
            System.out.println("\nTop 5 Currencies by Price:");
            analyzer.topNCurrenciesByPrice(5).forEach(c ->
                    System.out.println(c.getCurrencyName() + " - $" + c.getPrice()));

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
