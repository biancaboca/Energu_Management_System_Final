package com.monitoring.comunication.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CSVService {
    private final Logger logger = LoggerFactory.getLogger(CSVService.class);

    private final List<Double> measurements = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<String[]> allRows;

    public CSVService() throws IOException {
        // Pre-fetch the CSV data once
        String csvUrl = "https://dsrl.eu/courses/sd/materials/sensor.csv";
        URL url = new URL(csvUrl);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build(); // Skip header
            allRows = csvReader.readAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse CSV", e);
        }
    }

    @Scheduled(fixedRate = 10000) // 10 seconds
    public void addRandomMeasurement() {
        logger.info("Scheduled task called to add random measurement");
        if (allRows != null && !allRows.isEmpty()) {
            int randomRowIndex = new Random().nextInt(allRows.size());
            String[] randomRow = allRows.get(randomRowIndex);
            try {
                double measurementValue = Double.parseDouble(randomRow[0]);
                synchronized (measurements) {
                    measurements.add(measurementValue);
                  //  System.out.println(measurements);

                    logger.info("Added measurement: {}", measurementValue);
                }
            } catch (NumberFormatException e) {
                logger.error("Failed to parse measurement value", e);
            }
        } else {
            logger.warn("CSV data is empty or null");
        }
    }

    public Double getLatestMeasurement() {
        if (measurements.isEmpty()) {
            // If no measurements have been added yet, return null or throw an exception
            return null;
        }
        return measurements.get(measurements.size() - 1);
    }

    public List<Double> getAllMeasurement() {
        if (measurements.isEmpty()) {
            // If no measurements have been added yet, return null or throw an exception
            return null;
        }
        return measurements;
    }
    public String getMeasurementsAsJson() throws JsonProcessingException {
        synchronized (measurements) {
            return objectMapper.writeValueAsString(measurements);
        }
    }
}
