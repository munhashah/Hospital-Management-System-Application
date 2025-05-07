package HealthData;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import User.Patient;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class VitalCsvImporter {

    /**
     * Reads a UTF‑8 comma‑delimited CSV with header:
     *   heartRate,oxygenLevel,bloodPressure,temperature
     * and stores each row as a VitalSign for the given patient.
     */
    public static void importForPatient(File csvFile, Patient patient)
            throws IOException, CsvValidationException {
        try (
            InputStreamReader isr = new InputStreamReader(
                                      new FileInputStream(csvFile),
                                      StandardCharsets.UTF_8);
            CSVReader reader    = new CSVReader(isr)
        ) {
            String[] header = reader.readNext();  // skip or validate
            if (header == null || header.length < 4) {
                throw new IOException("CSV header missing or too few columns");
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                int    heartRate     = Integer.parseInt(row[0].trim());
                int    oxygenLevel   = Integer.parseInt(row[1].trim());
                String bloodPressure = row[2].trim();
                double temperature   = Double.parseDouble(row[3].trim());

                VitalSign vs = new VitalSign(
                                    heartRate,
                                    oxygenLevel,
                                    bloodPressure,
                                    temperature
                                );
                VitalDatabase.addOrUpdatePatientVitals(patient, vs);
            }
        }
    }
}
