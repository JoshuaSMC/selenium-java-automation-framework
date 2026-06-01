package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object para Google Search.
 *
 * Patrón Page Object Model (POM):
 *  - Encapsula los locators y las acciones de una página.
 *  - Los tests no conocen detalles del DOM; solo llaman métodos de negocio.
 *  - Usa Fluent Interface (retorna this) para encadenar acciones.
 */
public class GooglePage {

    private static final String URL = "https://www.google.com";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Locators ────────────────────────────────────────────────────────────
    private final By searchBoxLocator    = By.name("q");
    // Acotado a #search para no capturar h3 de "People also ask", snippets ni publicidades
    private final By searchResultLocator = By.cssSelector("div#search h3");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    @Step("Abrir Google")
    public GooglePage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));
        return this;
    }

    @Step("Buscar '{term}'")
    public GooglePage search(String term) {
        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));
        box.clear();
        box.sendKeys(term);
        box.sendKeys(Keys.ENTER);
        // Esperar a que la navegación se complete antes de retornar.
        // Sin este wait, getCurrentUrl() puede devolver la URL de la homepage
        // si el test la lee antes de que la respuesta del servidor llegue.
        wait.until(ExpectedConditions.urlContains("/search?"));
        return this;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Devuelve los textos de los títulos de resultados, no WebElement vivos.
     * Retornar List<WebElement> causaría StaleElementReferenceException si Google
     * re-renderiza el DOM entre la espera y la iteración en el test.
     */
    @Step("Obtener títulos de resultados de búsqueda")
    public List<String> getResultTitles() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultLocator));
        return driver.findElements(searchResultLocator)
                     .stream()
                     .map(e -> e.getText())
                     .filter(t -> !t.isBlank())
                     .toList();
    }

    public String getTitle()      { return driver.getTitle(); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
