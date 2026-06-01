package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object para la página de login de The Internet.
 * URL: https://the-internet.herokuapp.com/login
 *
 * Credenciales válidas:
 *  Usuario:    tomsmith
 *  Contraseña: SuperSecretPassword!
 */
public class TheInternetLoginPage {

    public static final String URL            = "https://the-internet.herokuapp.com/login";
    public static final String VALID_USER     = "tomsmith";
    public static final String VALID_PASSWORD = "SuperSecretPassword!";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Locators ────────────────────────────────────────────────────────────
    // By.id
    private final By usernameLocator = By.id("username");
    private final By passwordLocator = By.id("password");
    private final By flashLocator    = By.id("flash");
    // By.cssSelector
    private final By loginBtnLocator = By.cssSelector("button[type='submit']");
    // By.xpath
    private final By pageTitleLocator = By.xpath("//h2[contains(text(),'Login Page')]");
    private final By loginFormLocator = By.xpath("//form[@id='login']");

    public TheInternetLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    @Step("Abrir página de login")
    public TheInternetLoginPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameLocator));
        return this;
    }

    @Step("Ingresar usuario '{username}'")
    public TheInternetLoginPage enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(usernameLocator));
        field.clear();
        field.sendKeys(username);
        return this;
    }

    @Step("Ingresar contraseña")
    public TheInternetLoginPage enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(passwordLocator));
        field.clear();
        field.sendKeys(password);
        return this;
    }

    @Step("Hacer clic en el botón Login")
    public TheInternetLoginPage clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginBtnLocator)).click();
        return this;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    @Step("Obtener mensaje flash")
    public String getFlashMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(flashLocator)).getText();
    }

    public boolean isLoginSuccessful() {
        return getFlashMessage().contains("You logged into a secure area");
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitleLocator)).getText();
    }

    public boolean isLoginFormDisplayed() {
        // Usa wait explícito como el resto de los métodos — evita NoSuchElementException
        // si la página tarda en renderizar (frecuente en Heroku free-tier con cold start)
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginFormLocator))
                   .isDisplayed();
    }

    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
