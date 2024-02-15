import java.io.*;
import java.util.*;

public class Sorter {
    private static final String INTEGER_FILE = "integers.txt";
    private static final String FLOAT_FILE = "floats.txt";
    private static final String STRING_FILE = "strings.txt";

    private static String outputPath = "";
    private static String filePrefix = "";
    private static boolean appendMode = false;
    private static boolean showFullStats = false;

    private static int intCount = 0;
    private static double intSum = 0;
    private static int floatCount = 0;
    private static double floatSum = 0;
    private static int stringCount = 0;
    private static int shortestStringLength = Integer.MAX_VALUE;
    private static int longestStringLength = 0;

    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("-help")) {
            printHelp();
            return;
        }

        parseArgs(args);

        if (outputPath.isEmpty() && filePrefix.isEmpty()) {
            System.out.println("Please specify output path or file prefix or use '-help' for more information.");
            return;
        }

        for (String inputFile : args) {
            // Check if inputFile is not a flag
            if (!inputFile.startsWith("-")) {
                processFile(inputFile);
            }
        }

        printStats();
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    outputPath = args[++i];
                    break;
                case "-p":
                    filePrefix = args[++i];
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    showFullStats = false;
                    break;
                case "-f":
                    showFullStats = true;
                    break;
                default:
                    break;
            }
        }
        // Если outputPath не был указан, установить папку по умолчанию
        if (outputPath.isEmpty()) {
            outputPath = "output" + File.separator;
        }
    }

    private static void processFile(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("-?\\d+")) {
                    int num = Integer.parseInt(line);
                    writeToFile(outputPath + INTEGER_FILE, String.valueOf(num));
                    intCount++;
                    intSum += num;
                } else if (line.matches("-?\\d+(\\.\\d+)?")) {
                    double num = Double.parseDouble(line);
                    writeToFile(outputPath + FLOAT_FILE, String.valueOf(num));
                    floatCount++;
                    floatSum += num;
                } else {
                    writeToFile(outputPath + STRING_FILE, line);
                    stringCount++;
                    shortestStringLength = Math.min(shortestStringLength, line.length());
                    longestStringLength = Math.max(longestStringLength, line.length());
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + inputFile);
            e.printStackTrace();
            System.out.println("Use '-help' for more information.");
        }
    }

    private static void writeToFile(String filePath, String data) throws IOException {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, appendMode))) {
            writer.write(data);
            writer.newLine();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java Sorter [options] [files...]");
        System.out.println("Options:");
        System.out.println("-o <outputPath>  Specify output directory");
        System.out.println("-p <filePrefix>  Specify prefix for output files");
        System.out.println("-a               Enable append mode for output files");
        System.out.println("-s               Show basic statistics only");
        System.out.println("-f               Show full statistics");
    }

    private static void printStats() {
        System.out.println("Integers:");
        printStatsHelper(intCount, intSum);
        System.out.println("Floats:");
        printStatsHelper(floatCount, floatSum);
        System.out.println("Strings:");
        printStatsHelper(stringCount, shortestStringLength, longestStringLength);
    }

    private static void printStatsHelper(int count, double sum) {
        if (showFullStats) {
            double avg = count == 0 ? 0 : sum / count;
            System.out.println("Count: " + count);
            System.out.println("Sum: " + sum);
            System.out.println("Average: " + avg);
            System.out.println("----------");
        } else {
            System.out.println(count);
        }
    }

    private static void printStatsHelper(int count, int shortest, int longest) {
        if (showFullStats) {
            System.out.println("Count: " + count);
            System.out.println("Shortest length: " + shortest);
            System.out.println("Longest length: " + longest);
            System.out.println("----------");
        } else {
            System.out.println(count);
        }
    }
}