package edu.school21.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {
    public static String loadResource(String fileName) throws IOException {
        String result;

        try (InputStream resourceStream = resourceAsStream(fileName);
             Scanner scanner = new Scanner(resourceStream, UTF_8)) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (InputStream resourceStream = resourceAsStream(fileName);
             Scanner scanner = new Scanner(resourceStream, UTF_8)) {
            while (scanner.hasNextLine()){
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }

    public static InputStream resourceAsStream(String fileName) {
        return Utils.class.getResourceAsStream(fileName);
    }
}
