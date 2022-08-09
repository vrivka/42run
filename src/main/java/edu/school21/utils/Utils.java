package edu.school21.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {
    public static String loadResource(String fileName) throws IOException {
        String result;

        try (InputStream resourceAsStream = resourceStream(fileName);
             Scanner scanner = new Scanner(resourceAsStream, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static InputStream resourceStream(String fileName) {
        return Utils.class.getResourceAsStream(fileName);
    }
}
