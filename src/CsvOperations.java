import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class CsvOperations {

    public static List<String> readFromCsv(Path file, boolean ignoreHeader) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            System.err.println("Error occurred during reading the file");
        }

        return ignoreHeader ? lines.subList(1, lines.size()) : lines;
    }

    public static void writeToCsv(String content) throws IOException {
        Path path = Files.createDirectories(Path.of("./outputs"));
        File file = new File(path.toString() + "/" + LocalDateTime.now() + ".csv");
        String[] lines = content.split("\n");
        try(BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.append(line).append("\n");
            }
        }
    }
}
