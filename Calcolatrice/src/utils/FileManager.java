package utils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {

    public static void createFile(String name) {
        try {
            if (!Files.exists(Paths.get(name))) {
                Files.createFile(Paths.get(name));
                return;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readFile(String name) {
        System.out.println("Reading file: " + name);
        try {
            /*
             * if (Files.exists(Paths.get(name))) {
             * System.out.println("File exists");
             * } else {
             * System.out.println("File does not exist");
             * }
             */

            List<String> lines = Files.readAllLines(Paths.get(name));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeFile(String name, String content) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
