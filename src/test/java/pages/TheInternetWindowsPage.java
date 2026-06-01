package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

/**
 * Page Object para la página de Multiple Windows de The Internet.
 * URL: https://the-internet.herokuapp.com/windows
 *
 * Demuestra el manejo de múltiples ventanas y pestañas del browser:
 *
 *  - driver.getWindowHandle()     → handle (ID único) de la ventana activa
 *  - driver.getWindowHandles()    → Set con los handles de todas las ventanas abiertas
 *  - driver.switchTo().window()   → mueve el foco del driver a otra ventana
 *  - driver.close()               → cierra la ventana activa (no cierra el driver)
 *
 * Flujo típico:
 *  1. Guardar el handle de la ventana principal antes de abrir nuevas.
 *  2. Al hacer clic, esperar a que getWindowHandles().size() aumente.
 *  3. Iterar los handles y switchTo() al que no sea el principal.
 *  4. Interactuar con la nueva ventana normalmente.
 *  5. driver.close() + switchTo(mainHandle) para volver al contexto original.
 */
public class TheInternetWindowsPage {

    public static final String URL = "https://the-internet.herokuapp.com/windows";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    /**
     * Handle de la ventana principal — se captura en el constructor, no en open().
     *
     * Capturarlo aquí garantiza que siempre esté inicializado, independientemente
     * del orden en que el test llame a los métodos. Si se capturara solo en open(),
     * cualquier llamada a switchToNewWindow() o closeCurrentWindowAndSwitchBack()
     * antes de open() operaría sobre null.
     */
    private final String mainWindowHandle;

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By clickHereLinkLocator = By.cssSelector("a[href='/windows/new']");
    // Locator acotado al contenedor principal para no capturar h3 de otras secciones
    private final By newWindowTextLocator = By.cssSelector("div.example h3");

    public TheInternetWindowsPage(WebDriver driver) {
        this.driver           = driver;
        this.wait             = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.mainWindowHandle = driver.getWindowHandle();
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    @Step("Abrir página de múltiples ventanas")
    public TheInternetWindowsPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(clickHereLinkLocator));
        return this;
    }

    /**
     * Hace clic en "Click Here", que abre una nueva ventana del navegador.
     * Espera a que el handle de la nueva ventana esté disponible antes de retornar.
     */
    @Step("Hacer clic en 'Click Here' para abrir nueva ventana")
    public TheInternetWindowsPage clickOpenNewWindow() {
        int windowsBefore = driver.getWindowHandles().size();
        driver.findElement(clickHereLinkLocator).click();
        // Esperar a que aparezca el nuevo handle
        wait.until(d -> d.getWindowHandles().size() > windowsBefore);
        return this;
    }

    /**
     * Cambia el foco del driver a la nueva ventana (la que no es la principal).
     * Si hay más de dos ventanas, cambia al primer handle que no sea el principal.
     */
    @Step("Cambiar foco a la nueva ventana")
    public TheInternetWindowsPage switchToNewWindow() {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(mainWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return this;
    }

    /**
     * Cierra la ventana activa y devuelve el foco a la ventana principal.
     *
     * Importante: driver.close() cierra SOLO la ventana actual, no el WebDriver.
     * Después de close() el driver no tiene ventana activa — hay que hacer
     * switchTo().window(handle) para continuar usando el driver.
     */
    @Step("Cerrar ventana actual y volver a la ventana principal")
    public TheInternetWindowsPage closeCurrentWindowAndSwitchBack() {
        driver.close();
        driver.switchTo().window(mainWindowHandle);
        return this;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    @Step("Obtener título de la ventana activa")
    public String getActiveWindowTitle() {
        return driver.getTitle();
    }

    @Step("Obtener texto del encabezado de la ventana activa")
    public String getActiveWindowHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(newWindowTextLocator))
                   .getText();
    }

    /** Devuelve la cantidad de ventanas/pestañas abiertas actualmente. */
    public int getOpenWindowCount() {
        return driver.getWindowHandles().size();
    }

    /** Devuelve el handle de la ventana principal (la primera que se abrió). */
    public String getMainWindowHandle() {
        return mainWindowHandle;
    }

    /** Devuelve el handle de la ventana que tiene el foco en este momento. */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }
}
