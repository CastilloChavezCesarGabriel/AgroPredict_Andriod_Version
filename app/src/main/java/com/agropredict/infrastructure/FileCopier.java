package com.agropredict.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileCopier {
    private static final int BUFFER_SIZE = 4096;

    private FileCopier() {}

    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
