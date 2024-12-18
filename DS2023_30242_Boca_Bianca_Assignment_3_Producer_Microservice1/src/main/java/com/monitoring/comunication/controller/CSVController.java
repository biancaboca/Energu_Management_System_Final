package com.monitoring.comunication.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@RestController
public class CSVController {

    @PostMapping("/csv")
    public List<Double> parseCSV() throws IOException {
        String csvUrl = "https://dsrl.eu/courses/sd/materials/sensor.csv";
        URL url = new URL(csvUrl);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
            List<String[]> allRows = csvReader.readAll();
            List<Double> rowMap = new ArrayList<>();

            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                for (int j = 0; j < row.length; j++) {
                    Double cellValue = Double.parseDouble(row[j]);

                    rowMap.add(cellValue);
                }
            }
            System.out.println(rowMap);

            return rowMap;
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
