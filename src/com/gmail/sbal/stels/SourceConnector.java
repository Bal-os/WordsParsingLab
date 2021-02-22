package com.gmail.sbal.stels;

import java.io.FileNotFoundException;
import java.util.List;

public interface SourceConnector {
    List<String> getWordsList() throws FileNotFoundException;
}
