package com.mcndsj.TNTRun.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Matthew on 28/06/2016.
 */
public class FileUtils {
    public static String readFile(String path)
            throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static void writeFile(String path, String write)
            throws IOException {

        Files.write(Paths.get(path),write.getBytes());
    }
}
