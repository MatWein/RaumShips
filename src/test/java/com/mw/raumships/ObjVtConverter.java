package com.mw.raumships;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ObjVtConverter {
    private static final String INPUT_FILE = "D:\\Entwicklung\\Java\\RaumShips\\src\\main\\resources\\assets\\raumships\\models\\entity\\atlantis.obj";
    private static final String OUTPUT_FILE = "D:\\Entwicklung\\Java\\RaumShips\\src\\main\\resources\\assets\\raumships\\models\\entity\\atlantis_2.obj";
    private static final String VT_PREFIX = "vt ";

    public static void main(String[] args) throws Exception {
        List<double[]> vtValues = Files.lines(Paths.get(INPUT_FILE))
                .filter(line -> line.startsWith(VT_PREFIX))
                .map(line -> StringUtils.removeStart(line, VT_PREFIX))
                .map(line -> StringUtils.split(line, " "))
                .map(splittedStrings -> new double[] { Math.abs(Double.parseDouble(splittedStrings[0])), Math.abs(Double.parseDouble(splittedStrings[1])) })
                .map(splittedDoubles -> new double[] { Math.min(1.0, splittedDoubles[0]), Math.min(1.0, splittedDoubles[1]) })
                .collect(Collectors.toList());

        int i = 0;
        DecimalFormat format = new DecimalFormat("#.0000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.setMinimumIntegerDigits(1);

        try (FileReader fileReader = new FileReader(INPUT_FILE)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                try (FileWriter fileWriter = new FileWriter(OUTPUT_FILE)) {
                    try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                        String currentLine;
                        while ((currentLine = bufferedReader.readLine()) != null) {
                            if (currentLine.startsWith("vt ")) {
                                double[] doubles = vtValues.get(i++);
                                bufferedWriter.write(VT_PREFIX + format.format(doubles[0]) + " " + format.format(doubles[1]));
                            } else {
                                bufferedWriter.write(currentLine);
                            }

                            bufferedWriter.newLine();
                        }

                        bufferedWriter.flush();
                    }
                }
            }
        }
    }
}
