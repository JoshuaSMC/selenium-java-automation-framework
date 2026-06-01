package utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

/**
 * Utilidades de espera avanzadas.
 *
 * WebDriverWait vs FluentWait:
 *
 *  - WebDriverWait: wrapper de FluentWait con polling fijo de 500 ms.
 *    Suficiente para la mayoría de los casos.
 *
 *  - FluentWait: configuración completa — timeout, intervalo de polling
 *    e ignorar excepciones específicas. Útil cuando el elemento aparece
 *    de forma irregular o puede estar momentáneamente stale.
 */
public class WaitUtils {

    private WaitUtils() { /* clase utilitaria, no instanciable */ }

    /**
     * Crea un FluentWait con timeout y polling personalizado.
     * Ignora NoSuchElementException y StaleElementReferenceException
     * durante los reintentos para evitar falsos negativos en páginas dinámicas.
     *
     * @param driver          instancia activa del WebDriver
     * @param timeout         tiempo máximo de espera
     * @param pollingInterval intervalo entre cada reintento
     */
    public static FluentWait<WebDriver> build(WebDriver driver,
                                               Duration timeout,
                                               Duration pollingInterval) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
}
