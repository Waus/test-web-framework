package pl.tmobile.recruitment.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static final ByteArrayOutputStream consoleBuffer = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;
    private static final PrintStream logStream = new PrintStream(consoleBuffer);
    private static final String LOG_DIR = "test-log";

    @Before
    public void setup() {
        Path logDirectory = Paths.get(LOG_DIR);
        try {
            Files.createDirectories(logDirectory);
            logger.info("Folder test-log został utworzony: {}", LOG_DIR);
        } catch (IOException e) {
            logger.error("Nie udało się utworzyć folderu test-log: {}", e.getMessage());
        }

        System.setOut(new PrintStream(new TeeOutputStream(originalOut, logStream)));
        System.setErr(new PrintStream(new TeeOutputStream(originalErr, logStream)));
        logger.info("\n==================== Rozpoczynam nowy scenariusz... ====================");
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("\n==================== Zakończenie scenariusza ====================");

        if (scenario.isFailed()) {
            logger.error("Test NIE POWIÓDŁ SIĘ: {}", scenario.getName());
            reportError(scenario);
        } else {
            logger.info("Test zakończony POMYŚLNIE: {}", scenario.getName());
        }

        logger.info("Zamykam przeglądarkę...");
        closeWebDriver();

        consoleBuffer.reset();
    }

    private void reportError(Scenario scenario) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String logFileName = LOG_DIR + "/test-error_" + timestamp + ".log";

        try (FileWriter writer = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(writer)) {

            printWriter.println("\n==================================================");
            printWriter.println("Test nie powiódł się w scenariuszu: " + scenario.getName());
            printWriter.println("Status: FAILED");
            printWriter.println("Czas: " + LocalDateTime.now());

            printWriter.println("\nPełny log testu:");
            printWriter.println(consoleBuffer.toString());

            printWriter.println("==================================================");

            logger.error("Log błędu zapisany do: {}", logFileName);

        } catch (IOException e) {
            logger.error("Błąd zapisu do pliku test-error.log: {}", e.getMessage());
        }
    }
}
