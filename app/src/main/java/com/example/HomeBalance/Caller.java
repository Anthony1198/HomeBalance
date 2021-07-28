package com.example.HomeBalance;

import java.io.BufferedReader;

/**
 * Interface für das Answer-Handling
 */
public interface Caller {
    void handleAnswer(BufferedReader bufferedReader, String identifier, String message);
}
