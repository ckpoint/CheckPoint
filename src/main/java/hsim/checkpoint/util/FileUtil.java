package hsim.checkpoint.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileUtil {

    private static BufferedReader getBufferReader(final File file, final Charset charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            throw new IOException("file status not normal : " + file.getName());
        }
        return Files.newBufferedReader(file.toPath(), charset);
    }

    public static String readFileToString(File file, final Charset charset) throws IOException {
        String line = null;
        BufferedReader reader = getBufferReader(file, charset);
        StringBuffer strBuffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            strBuffer.append(line);
        }
        return strBuffer.toString();
    }

    private static OutputStream getOutputStream(final File file) throws IOException {
        return Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public static void writeStringToFile(File file, String content) throws IOException {
        OutputStream outputStream = getOutputStream(file);
        outputStream.write(content.getBytes());
    }
}
