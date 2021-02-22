package com.gmail.sbal.stels;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final int TASK_NUMBER = 1;
    private static final String SOURCE_FILE_PATH = "csv.csv";

    private static final List<WordProcessor> functionList = List.of(
            // Alternative 1
            wordList -> wordList
                    .stream()
                    .distinct()
                    .filter(w -> w.length() == Collections.max(
                            wordList,
                            Comparator.comparing(String::length)).length()
                    )
                    .forEach(System.out::println),
            // 1
            wordList -> wordList
                    .stream()
                    .distinct()
                    .collect(
                            Collectors.groupingBy(
                                    String::length,
                                    TreeMap::new,
                                    Collectors.toList()
                            )
                    )
                    .lastEntry()
                    .getValue()
                    .forEach(System.out::println),
            // 2
            wordList ->
                wordList.stream().distinct().forEach(word -> System.out.println(
                        new StringBuilder()
                                .append("word: ")
                                .append(word)
                                .append(", count: ")
                                .append(
                                        Collections.frequency(wordList, word)
                                ).toString()
                )),
            // 3
            (new WordProcessor() {
                private final Pattern charPattern = Pattern.compile("[^aeiou]");

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .filter(w -> !charPattern.matcher(w.toLowerCase()).find())
                            .distinct()
                            .forEach(System.out::println);
                }
            }),
            // 4
            wordList ->
                    wordList.stream()
                            .filter(
                                    w -> w.chars()
                                            .filter(
                                                    ch -> w.indexOf(ch) == w.lastIndexOf(ch)
                                            )
                                            .count() == w.length()
                            )
                            .distinct()
                            .forEach(System.out::println),
            //5
            (new WordProcessor() {
                private final Pattern charPattern = Pattern.compile("([^aeiou])\\1");

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .distinct()
                            .filter(w -> charPattern.matcher(w.toLowerCase()).find())
                            .forEach(System.out::println);
                }
            }),
            //6
            (new WordProcessor() {
                private final List<Character> charList = List.of('a','e','i','o','u');

                private long getNumberOfSymbolsInRow (String word){
                    int counter = 0;
                    int max = 0;
                    for(var i: word.toCharArray()){
                        if(charList.contains(i)) {
                            max = Math.max(counter, max);
                            counter = 0;
                        } else counter++;
                    }
                    return max;
                }

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .distinct()
                            .collect(
                                    Collectors.groupingBy(
                                        w -> getNumberOfSymbolsInRow(w),
                                        TreeMap::new,
                                        Collectors.toList()
                                    )
                            )
                            .lastEntry()
                            .getValue()
                            .forEach(System.out::println);
                }
            }),
            // Alternative 7
            wordList -> wordList
                    .stream()
                    .distinct()
                    .collect(
                            Collectors.groupingBy(
                                    w -> Collections.frequency(wordList, w),
                                    TreeMap::new,
                                    Collectors.toList()
                            )
                    )
                    .lastEntry()
                    .getValue()
                    .forEach(System.out::println),
            // 7
            wordList -> {
                var list =
                        wordList.stream()
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                .entrySet()
                                .stream()
                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                .collect(Collectors.toList());
                Long max = list.get(0).getValue();
                list.stream()
                        .filter(w -> Objects.equals(w.getValue(), max))
                        .forEach(w -> System.out.println(w.getKey()));
            },
            // 8
            (new WordProcessor() {
                private final Pattern charPattern = Pattern.compile("[aeiou]");

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .filter(w -> charPattern.matcher(w.toLowerCase()).results().count() > (double) w.length() / 2)
                            .distinct()
                            .forEach(System.out::println);
                }
            }),
            // 9
            wordList ->
                    wordList.stream()
                            .distinct()
                            .collect(
                                    Collectors.groupingBy(
                                            w -> w.toLowerCase()
                                                    .chars()
                                                    .distinct()
                                                    .count(),
                                            TreeMap::new,
                                            Collectors.toList()
                                    )
                            )
                            .lastEntry()
                            .getValue()
                            .forEach(System.out::println),
            // 10
            wordList ->
                    wordList.stream()
                            .distinct()
                            .sorted(Comparator.comparing(String::length))
                            .forEach(System.out::println),
            // 11
            (new WordProcessor() {
                private final Pattern charPattern = Pattern.compile("[aeiou]");

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .distinct()
                            .sorted(Comparator.comparing(
                                    w -> charPattern.matcher(w.toLowerCase()).results().count() / (double) w.length())
                            )
                            .forEach(System.out::println);
                }
            }),
            // 12
            (new WordProcessor() {
                private final Pattern charPattern = Pattern.compile("[^aeiou]");

                @Override
                public void printResult(List<String> wordList) {
                    wordList.stream()
                            .distinct()
                            .sorted(Comparator.comparing(
                                    w -> charPattern.matcher(w.toLowerCase()).results().count())
                            )
                            .forEach(System.out::println);
                }
            }),
            // 13
            new DistanceWordProcessor(){
                @Override
                public void printResult(List<String> wordList) {
                    getMaxDistList(
                            wordList.stream()
                                    .distinct()
                                    .collect(Collectors.toList())
                    ).forEach(System.out::println);
                }
            },
            // 14
            (new DistanceWordProcessor(){
                @Override
                public void printResult(List<String> wordList) {
                    getSetCollectionOfMaxSubsetsFromList(new HashSet<>(wordList)).forEach(System.out::println);
                }
            })

    );

    public static int taskToFunkNum(int num){
        if(num <= 0 || num > functionList.size())
            throw new IllegalArgumentException("There is no such task number");

        if (num < 7) return num;
        return num + 1;
    }

    public static String getSourceFilePath(){
        String ans = null;
        System.out.print("Enter path to the source file: ");
        Scanner reader = new Scanner(System.in);
        ans = reader.nextLine().trim();
        return ans.equals("") ? SOURCE_FILE_PATH : ans;
    }

    public static void main(String[] args){
        try {
            functionList.get(taskToFunkNum(TASK_NUMBER))
                        .printResult(
                                new FileSourceConnector.ConnectionBuilder(getSourceFilePath()).build().getWordsList()
                        );
        } catch (FileNotFoundException | IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
