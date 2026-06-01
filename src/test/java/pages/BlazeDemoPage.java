package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import utils.WaitUtils;

import java.time.Duration;

/**
 * Page Object para BlazeDemo — formulario de búsqueda de vuelos.
 * URL: https://blazedemo.com
 */
public class BlazeDemoPage {

    private static final String URL = "https://blazedemo.com";

    private final WebDriver            driver;
    private final WebDriverWait        wait;
    // FluentWait compartido: igual que WebDriverWait pero también ignora
    // StaleElementReferenceException — usado donde el DOM puede estar transitoriamente stale.
    private final FluentWait<WebDriver> fluentWait;

    // ── Locators — página principal ──────────────────────────────────────────
    private final By fromPortLocator    = By.name("fromPort");
    private final By toPortLocator      = By.name("toPort");
    private final By findFlightsLocator = By.cssSelector("input[type='submit']");

    // ── Locators — página de resultados ─────────────────────────────────────
    private final By flightsTableLocator = By.cssSelector("table.table");

    public BlazeDemoPage(WebDriver driver) {
        this.driver     = driver;
        this.wait       = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.fluentWait = WaitUtils.build(driver, Duration.ofSeconds(10), Duration.ofMillis(500));
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    @Step("Abrir BlazeDemo")
    public BlazeDemoPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fromPortLocator));
        return this;
    }

    @Step("Seleccionar ciudad de origen: '{city}'")
    public BlazeDemoPage selectDeparture(String city) {
        // Usar elementToBeClickable (no findElement directo) para tolerar DOM transitorio stale
        new Select(wait.until(ExpectedConditions.elementToBeClickable(fromPortLocator)))
                .selectByVisibleText(city);
        return this;
    }

    @Step("Seleccionar ciudad de destino: '{city}'")
    public BlazeDemoPage selectDestination(String city) {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(toPortLocator)))
                .selectByVisibleText(city);
        return this;
    }

    @Step("Hacer clic en 'Find Flights'")
    public BlazeDemoPage clickFindFlights() {
        wait.until(ExpectedConditions.elementToBeClickable(findFlightsLocator)).click();
        return this;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Devuelve true si la tabla de vuelos es visible, false si no lo es.
     *
     * Usa fluentWait (con StaleElementReferenceException ignorado) porque la tabla
     * aparece después de una navegación donde el DOM puede estar transitoriamente stale.
     *
     * Se captura TimeoutException porque until() la lanza si el elemento nunca se
     * hace visible — en ese caso el método contrato devuelve false en lugar de propagar
     * la excepción al test.
     */
    @Step("Verificar que la tabla de vuelos es visible")
    public boolean areFlightsDisplayed() {
        try {
            fluentWait.until(ExpectedConditions.visibilityOfElementLocated(flightsTableLocator));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public String getTitle()      { return driver.getTitle(); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
