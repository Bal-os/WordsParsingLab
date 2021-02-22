package com.gmail.sbal.stels;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public enum FileSourceConnector implements SourceConnector {
    CONNECTION;

    private final Pattern wordPattern = Pattern.compile("[\\W^0-9]");
    private final int maxLengthOfWords = 30;
    private File file;

    private List<String> split(String longWord, int maxLength){
        List<String> ans = new ArrayList<>();
        for(int i = 0; i < longWord.length(); i += maxLength){
            ans.add(longWord.substring(i, Math.min(longWord.length(), i + maxLength)));
        }
        return ans;
    }

    @Override
    public List<String> getWordsList() throws FileNotFoundException {
        List<String> ans = new ArrayList<>();

        try (Scanner input = new Scanner(file)) {
            input.useDelimiter(wordPattern);
            while (input.hasNext()) {
                String word = input.next();
                if (!word.isEmpty()) ans.addAll(split(word, maxLengthOfWords));
            }
        } catch (FileNotFoundException e) {throw new FileNotFoundException();}
        return ans;
    }

    private void build(ConnectionBuilder connectionBuilder){
        this.file = connectionBuilder.file;
    }

    public static class ConnectionBuilder{

        private final File file;

        ConnectionBuilder(String path) throws FileNotFoundException{
            File file = new File(path);
            if(file.exists()) this.file = file;
            else throw new FileNotFoundException("no file with such path");
        }

        public FileSourceConnector build(){
            FileSourceConnector.CONNECTION.build(this);
            return CONNECTION;
        }

    }
}
