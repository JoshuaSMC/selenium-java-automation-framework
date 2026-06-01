package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object para la página de JavaScript Alerts de The Internet.
 * URL: https://the-internet.herokuapp.com/javascript_alerts
 *
 * Demuestra el manejo de los tres tipos de diálogos de JavaScript:
 *
 *  - window.alert()   → solo OK, sin retorno de valor
 *  - window.confirm() → OK (accept) o Cancel (dismiss)
 *  - window.prompt()  → campo de texto + OK/Cancel
 *
 * En Selenium, todos se manejan con driver.switchTo().alert(),
 * que devuelve un objeto Alert con los métodos accept(), dismiss(),
 * getText() y sendKeys().
 */
public class TheInternetAlertsPage {

    public static final String URL = "https://the-internet.herokuapp.com/javascript_alerts";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By jsAlertBtnLocator   = By.cssSelector("button[onclick='jsAlert()']");
    private final By jsConfirmBtnLocator = By.cssSelector("button[onclick='jsConfirm()']");
    private final By jsPromptBtnLocator  = By.cssSelector("button[onclick='jsPrompt()']");
    private final By resultLocator       = By.id("result");

    public TheInternetAlertsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Acciones primitivas — click → alert presente ──────────────────────────

    @Step("Abrir página de JavaScript Alerts")
    public TheInternetAlertsPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(jsAlertBtnLocator));
        return this;
    }

    /**
     * Hace clic en el botón que dispara window.alert().
     * Espera a que el alert esté presente antes de retornar.
     */
    @Step("Hacer clic en el botón de JS Alert")
    public TheInternetAlertsPage clickJsAlertButton() {
        driver.findElement(jsAlertBtnLocator).click();
        wait.until(ExpectedConditions.alertIsPresent());
        return this;
    }

    /**
     * Hace clic en el botón que dispara window.confirm().
     * Espera a que el alert esté presente antes de retornar.
     */
    @Step("Hacer clic en el botón de JS Confirm")
    public TheInternetAlertsPage clickJsConfirmButton() {
        driver.findElement(jsConfirmBtnLocator).click();
        wait.until(ExpectedConditions.alertIsPresent());
        return this;
    }

    /**
     * Hace clic en el botón que dispara window.prompt().
     * Espera a que el alert esté presente antes de retornar.
     */
    @Step("Hacer clic en el botón de JS Prompt")
    public TheInternetAlertsPage clickJsPromptButton() {
        driver.findElement(jsPromptBtnLocator).click();
        wait.until(ExpectedConditions.alertIsPresent());
        return this;
    }

    // ── Interacciones con el alert activo ────────────────────────────────────

    /**
     * Lee el texto del alert activo sin cerrarlo.
     * Útil para verificar el mensaje antes de aceptar o cancelar.
     */
    @Step("Leer el texto del alert activo")
    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    /**
     * Acepta (OK) el alert activo.
     * Equivale a pulsar OK en cualquiera de los tres tipos de diálogo.
     */
    @Step("Aceptar (OK) el alert activo")
    public TheInternetAlertsPage acceptAlert() {
        driver.switchTo().alert().accept();
        return this;
    }

    /**
     * Cancela el alert activo.
     * Solo tiene efecto real en window.confirm() y window.prompt().
     * En window.alert(), dismiss() tiene el mismo efecto que accept().
     */
    @Step("Cancelar (Cancel) el alert activo")
    public TheInternetAlertsPage dismissAlert() {
        driver.switchTo().alert().dismiss();
        return this;
    }

    /**
     * Escribe texto en un window.prompt() activo y lo acepta.
     *
     * @param text texto a ingresar en el campo del prompt
     */
    @Step("Escribir '{text}' en el prompt y aceptar")
    public TheInternetAlertsPage typeInPromptAndAccept(String text) {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        alert.accept();
        return this;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    @Step("Obtener el mensaje de resultado")
    public String getResultMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(resultLocator)).getText();
    }
}
