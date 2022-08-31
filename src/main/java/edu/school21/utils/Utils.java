package edu.school21.utils;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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

    public static ByteBuffer getBytesFromFile(String fileName) throws Exception {
        ByteBuffer byteBuffer;

        try (InputStream inputStream = OBJLoader.class.getResourceAsStream(fileName)) {
            byte[] bytes = inputStream.readAllBytes();

            byteBuffer = BufferUtils.createByteBuffer(bytes.length + 1);
            byteBuffer.put(bytes);
            byteBuffer.put((byte) 0);
            byteBuffer.flip();
        }
        return byteBuffer;
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
