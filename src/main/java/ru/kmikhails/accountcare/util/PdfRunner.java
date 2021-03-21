package ru.kmikhails.accountcare.util;

import ru.kmikhails.accountcare.exception.AccountException;

import java.io.IOException;

public class PdfRunner {

    public void showScan(String filename) {
        try {
            String command = "SumatraPDF.exe " + filename;
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            if (!process.isAlive()) {
                throw new AccountException("Ошибка отображения скана\nОбратитесь в поддержку");
            }
        } catch (IOException e) {
            throw new AccountException("Ошибка отображения скана\nОбратитесь в поддержку");
        }
    }
}
