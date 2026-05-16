package com.agropredict.infrastructure.backup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamCopier {
    public void copy(InputStream input, OutputStream output) throws IOException {
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}