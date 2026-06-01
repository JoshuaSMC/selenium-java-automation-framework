package base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ScreenshotUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

/**
 * Clase base para todos los tests de Selenium.
 *
 * Responsabilidades:
 *  - Inicializar ChromeDriver con Selenium Manager (sin path manual).
 *  - Capturar screenshot automáticamente si un test falla.
 *  - Cerrar el driver al finalizar cada test.
 *
 * Modo headless: ejecutar con -Dheadless=true
 *   mvn test -Dheadless=true
 */
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * TestWatcher: se ejecuta DESPUÉS del test pero ANTES de que se cierre el driver,
     * lo que permite tomar screenshots en caso de fallo.
     */
    @RegisterExtension
    final TestWatcher lifecycle = new TestWatcher() {

        @Override
        public void testFailed(ExtensionContext ctx, Throwable cause) {
            System.out.println("❌ Test fallido: " + ctx.getDisplayName());
            ScreenshotUtil.take(driver, ctx.getDisplayName());
            quitDriver();
        }

        @Override
        public void testSuccessful(ExtensionContext ctx) {
            System.out.println("✅ Test exitoso: " + ctx.getDisplayName());
            quitDriver();
        }

        @Override
        public void testAborted(ExtensionContext ctx, Throwable cause) {
            quitDriver();
        }

        @Override
        public void testDisabled(ExtensionContext ctx, Optional<String> reason) {
            // El driver nunca se inició
        }

        private void quitDriver() {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    };

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }

        // Opciones recomendadas para entornos CI/CD y macOS
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-search-engine-choice-screen");

        // Anti-detección de bots: evita que sitios como Google identifiquen el browser
        // como automatizado y muestren páginas de CAPTCHA o "¿Eres un robot?"
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Selenium Manager descarga y gestiona ChromeDriver automáticamente
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        // Sin implicit wait: se usa exclusivamente WebDriverWait (explicit wait) en cada Page Object.
        // Mezclar implicit + explicit wait genera timeouts impredecibles (Selenium docs lo advierte).
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }
}
