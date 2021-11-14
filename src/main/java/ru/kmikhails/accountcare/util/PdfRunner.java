package ru.kmikhails.accountcare.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.exception.AccountException;

import java.io.IOException;

public class PdfRunner {
    private static final Logger LOG = LogManager.getLogger(PdfRunner.class);

    public void showScan(String filename) {
        try {
            String command = "SumatraPDF.exe " + filename;
            LOG.info(command);
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            if (!process.isAlive()) {
                throw new AccountException("Ошибка отображения скана\nОбратитесь в поддержку");
            }
        } catch (IOException e) {
            LOG.error("Ошибка отображения скана", e);
            throw new AccountException("Ошибка отображения скана\nОбратитесь в поддержку");
        }
    }
}
