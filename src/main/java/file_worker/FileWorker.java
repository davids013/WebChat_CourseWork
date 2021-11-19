package file_worker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class FileWorker {
    public static final byte HOST_INDEX = 0;
    public static final byte PORT_INDEX = 1;
    private static final String HOST_CONFIG_KEY = "HOST: ";
    private static final String PORT_CONFIG_KEY = "PORT: ";

    public static String[] getHostAndPortFromConfig(String configFilename) {
        final String[] hostAndPort = new String[2];
        final File configFile = new File(configFilename);
        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead())
            createNewSettings(configFile);
        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            try {
                final List<String> lines = Files.readAllLines(Paths.get(configFilename), StandardCharsets.UTF_8);
                final String hostLine = lines.get(HOST_INDEX);
                final String portLine = lines.get(PORT_INDEX);
                if (lines.size() == hostAndPort.length
                        && hostLine.contains(HOST_CONFIG_KEY)
                        && portLine.contains(PORT_CONFIG_KEY)) {
                    final String tempHost = hostLine.replace(HOST_CONFIG_KEY, "");
                    final String tempPort = portLine.replace(PORT_CONFIG_KEY, "");
                    if (tempHost.length() >= 7 && tempPort.length() >= 4) {
                        hostAndPort[HOST_INDEX] = tempHost;
                        hostAndPort[PORT_INDEX] = portLine.replace(PORT_CONFIG_KEY, "");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (hostAndPort[HOST_INDEX] == null || hostAndPort[PORT_INDEX] == null) {
            createNewSettings(configFile);
            return getHostAndPortFromConfig(configFilename);
        }
        return hostAndPort;
    }

    private static boolean createNewSettings(File configFile) {
        final String BASE_HOST = "localhost";
        final int BASE_PORT = 11211;
        try (final PrintWriter writer = new PrintWriter(configFile)) {
            writer.println(HOST_CONFIG_KEY + BASE_HOST);
            writer.println(PORT_CONFIG_KEY + BASE_PORT);
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    private static String readLine(String filename) {
//        String line
//        try {
//            Files.readString(Paths.get(filename));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        TODO: code it
//        return false;
//    }
}
