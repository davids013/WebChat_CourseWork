package entities;

import java.io.*;

public class FileLogger implements Logger {
    public static final String LINE_SEPARATOR = "\r\n";
    private final File file;

    public FileLogger(String fileName) {
        file = new File(fileName);
        checkFile();
    }

    public FileLogger(File file) {
        this.file = file;
        checkFile();
    }

    private void checkFile() {
        if (!file.exists()) {
            final File logDirectory = new File(file.getParent());
            try {
                if (!logDirectory.exists()) logDirectory.mkdir();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean log(String info) {
        try (final FileWriter writer = new FileWriter(file, true)) {
            writer.write(info + LINE_SEPARATOR);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
