package org.example.scraper;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class YahooCryptoScraper {
    private static final String BASE_URL = "https://finance.yahoo.com/markets/crypto/all/?start=%d&count=250";
    private static final int TOTAL_ROWS = 9947;
    private static final int ROWS_PER_PAGE = 250;

    public static void main(String[] args) {
        // Schedule the scraper to run every 24 hours
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Starting scraper at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            scrapeCryptoData();
        }, 0, 24, TimeUnit.HOURS);
    }

    public static void scrapeCryptoData() {
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckoDriver\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);  // Set to true to run the browser in the background

        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String csvFilePath = "crypto_data_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            // CSV Header
            String[] headers = {"Currency Name", "Price", "Change", "Change %", "Market Cap", "Volume", "Circulating Supply", "Timestamp"};
            writer.writeNext(headers);

            // Loop through pages (9947 total rows, 250 rows per page)
            for (int start = 0; start < TOTAL_ROWS; start += ROWS_PER_PAGE) {
                String url = String.format(BASE_URL, start);
                driver.get(url);

                // Handle cookie consent if present
                try {
                    WebElement consentOverlay = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".consent-overlay")));
                    WebElement acceptButton = consentOverlay.findElement(By.cssSelector(".accept-all"));
                    acceptButton.click();
                    System.out.println("Accepted cookie consent.");
                } catch (Exception e) {
                    System.out.println("No cookie consent form displayed or failed to accept.");
                }

                // Wait for the table to load
                String tableXPath = "/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table";
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(tableXPath)));

                // Scrape rows of data for this page
                for (int i = 1; i <= ROWS_PER_PAGE; i++) {
                    try {
                        // Name of the currency
                        String currencyNameXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[1]/span/div/a/div/span[2]", i);
                        String currencyName = driver.findElement(By.xpath(currencyNameXPath)).getText().trim();

                        // Price
                        String priceXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[2]/span/fin-streamer", i);
                        String price = driver.findElement(By.xpath(priceXPath)).getText().trim();

                        // Change
                        String changeXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[3]/span/fin-streamer", i);
                        String change = driver.findElement(By.xpath(changeXPath)).getText().trim();

                        // Change percentage
                        String changePercentXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[4]/span/fin-streamer", i);
                        String changePercent = driver.findElement(By.xpath(changePercentXPath)).getText().trim();

                        // Market Cap
                        String marketCapXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[6]/span/fin-streamer", i);
                        String marketCap = driver.findElement(By.xpath(marketCapXPath)).getText().trim();

                        // Volume
                        String volumeXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[7]/span/fin-streamer", i);
                        String volume = driver.findElement(By.xpath(volumeXPath)).getText().trim();

                        // Circulating Supply
                        String supplyXPath = String.format("/html/body/div[2]/main/section/section/section/article/section[1]/div/div[2]/div/table/tbody/tr[%d]/td[10]/span", i);
                        String supply = driver.findElement(By.xpath(supplyXPath)).getText().trim();

                        // Write the data into CSV
                        String[] data = {currencyName, price, change, changePercent, marketCap, volume, supply, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))};
                        writer.writeNext(data);

                    } catch (Exception e) {
                        System.err.println("Error processing row " + i + " on page " + (start / ROWS_PER_PAGE + 1) + ": " + e.getMessage());
                    }
                }
                System.out.println("Completed scraping page starting at row " + start);
            }

            System.out.println("Data extraction completed successfully and saved to: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
