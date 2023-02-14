package org.example;

import org.example.group.impl.GroupStrategyImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    private static final String regex = "(^([\"]{1})([0-9]+)([\"]{1})$)";
    private static final String FILEPATH = "src/main/resources/lng.txt";
    private static final String SAVE_RESULT = "src/main/resources/result.txt";

    public static void main(String[] args) {

        File file = new File(FILEPATH);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file);
                 Stream<String> streamFromFile = scanner.tokens()) {
                List<Set<String>> groups = streamFromFile
                        .distinct()
                        .map(x -> x.split(";"))
                        .filter(Main::isStringValid)
                        .collect(new GroupStrategyImpl());
                groups.sort(Comparator.comparing(Set::size, Comparator.reverseOrder()));
                saveGroupsIntoFile(groups);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("File not found!");
        }
    }

    public static boolean isStringValid(String[] candidate) {
        for (String subString : candidate)
            if (!Pattern.matches(regex, subString))
                return false;
        return true;
    }

    public static void saveGroupsIntoFile(List<Set<String>> results) {
        Path pathToResultFile = Paths.get(SAVE_RESULT);
        if (Files.notExists(pathToResultFile)) {
            try {
                Files.createFile(pathToResultFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToResultFile.toString()))) {

            for (int i = 0; i < results.size(); ++i) {
                if ((results.get(i).size() <= 1) || (i == results.size() - 1)) {
                    bufferedWriter.write("Число групп с более чем одним элементом: " + (i));
                    bufferedWriter.write("\n");
                    break;
                }
            }
            for (int i = 1; i < results.size(); ++i) {
                Iterator<String> iterator = results.get(i).iterator();
                if (iterator.hasNext()) {
                    bufferedWriter.write("Группа: " + i);
                    bufferedWriter.write("\n");
                }
                while (iterator.hasNext()) {
                    bufferedWriter.write(iterator.next());
                    bufferedWriter.write("\n");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

