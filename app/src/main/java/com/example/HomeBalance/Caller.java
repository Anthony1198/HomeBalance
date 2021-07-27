package com.example.HomeBalance;

import java.io.BufferedReader;

public interface Caller {
    void handleAnswer(BufferedReader bufferedReader, String identifier, String message);
}
