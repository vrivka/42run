package edu.school21.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {
    public static String loadResource(String fileName) throws IOException {
        String result;

        try (InputStream resourceStream = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(resourceStream, UTF_8)) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static String getPathToResource(String fileName) throws FileNotFoundException {
        String slash = "";
        if (!fileName.startsWith("/")) {
            slash = "/";
        }
        URL result = Utils.class.getResource(slash + fileName);
        if (result == null) {
            throw new FileNotFoundException("File: " + fileName + " not found");
        }
        return result.getPath();
    }

    public static float[] listFloatToArray(List<Float> list) {
        float[] result = new float[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static int[] listIntToArray(List<Integer> list) {
        int[] result = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
