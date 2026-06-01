package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para capturar pantallas durante los tests.
 * Las imágenes se guardan en la carpeta screenshots/ del proyecto.
 */
public class ScreenshotUtil {

    private static final String DIR = "screenshots";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void take(WebDriver driver, String testName) {
        if (driver == null) return;
        try {
            // Si hay un JS alert abierto, getScreenshotAs() lanza UnhandledAlertException.
            // Intentamos descartarlo antes de capturar para no suprimir el error original.
            dismissAlertIfPresent(driver);

            Path dir = Paths.get(DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String name = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            Path file   = dir.resolve(name + "_" + LocalDateTime.now().format(FORMAT) + ".png");

            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, bytes);
            System.out.println("📸 Screenshot guardado: " + file);
        } catch (IOException e) {
            System.err.println("No se pudo guardar el screenshot: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier excepción de WebDriver para no suprimir el fallo original
            System.err.println("No se pudo guardar el screenshot (WebDriver): " + e.getMessage());
        }
    }

    /**
     * Descarta un alert nativo si está presente.
     * No hace nada si no hay ninguno abierto.
     */
    private static void dismissAlertIfPresent(WebDriver driver) {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception ignored) {
            // No hay alert activo — continuar normalmente
        }
    }
}
