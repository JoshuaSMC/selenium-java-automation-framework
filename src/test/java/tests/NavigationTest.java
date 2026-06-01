package tests;

import base.BaseTest;
import data.TestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de la API de navegación del browser: back(), forward(), to() y refresh().
 *
 * Nota de diseño: estos tests operan directamente sobre `driver` en lugar de usar
 * un Page Object. Esto es intencional: lo que se está probando no es una página
 * concreta sino el comportamiento del browser (historial de navegación). Crear un
 * Page Object para envolver back()/forward() no aportaría abstracción real — solo
 * agregaría una capa vacía. Las URLs se centralizan en TestData para evitar magic
 * strings y facilitar el mantenimiento.
 */
@Tag("navegacion")
@Feature("Navegación del Browser")
@DisplayName("Pruebas de navegación del browser")
class NavigationTest extends BaseTest {

    @Test
    @Story("Navegación hacia atrás")
    @DisplayName("Navegar atrás con back() vuelve a la URL anterior")
    void shouldNavigateBack() {
        driver.get(TestData.BLAZEDEMO_HOME_URL);
        driver.get(TestData.BLAZEDEMO_PURCHASE_URL);

        driver.navigate().back();

        // navigate().back() es asíncrono — se espera a que la URL cambie
        // antes de assertar para evitar falsos negativos en CI o red lenta
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(TestData.BLAZEDEMO_HOME_URL));

        assertThat(driver.getCurrentUrl())
                .as("Debería volver a la home de BlazeDemo, no a purchase.php")
                .doesNotContain("purchase")
                .endsWith("blazedemo.com/");
    }

    @Test
    @Story("Navegación hacia adelante")
    @DisplayName("Navegar adelante con forward() avanza a la URL siguiente")
    void shouldNavigateForward() {
        driver.get(TestData.BLAZEDEMO_HOME_URL);
        driver.get(TestData.BLAZEDEMO_PURCHASE_URL);
        driver.navigate().back();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(TestData.BLAZEDEMO_HOME_URL));

        driver.navigate().forward();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("purchase"));

        assertThat(driver.getCurrentUrl())
                .as("Debería avanzar de vuelta a purchase.php")
                .contains("purchase");
    }

    @Test
    @Story("Navegación directa a URL")
    @DisplayName("navigate().to() carga la URL indicada")
    void shouldNavigateToUrl() {
        driver.navigate().to(TestData.BLAZEDEMO_HOME_URL);

        assertThat(driver.getTitle())
                .as("El título debería ser el de BlazeDemo")
                .contains("BlazeDemo");
    }

    @Test
    @Story("Refresh de página")
    @DisplayName("navigate().refresh() mantiene la URL actual")
    void shouldRefreshPage() {
        driver.get(TestData.BLAZEDEMO_HOME_URL);
        String titleAntes = driver.getTitle();

        driver.navigate().refresh();

        // Con pageLoadStrategy = "normal" (default), navigate().refresh() bloquea
        // hasta que document.readyState = "complete", por lo que el título ya
        // está disponible al salir de la llamada. El wait explícito sería redundante,
        // pero lo incluimos por consistencia con los otros tests de navegación.
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleIs(titleAntes));

        assertThat(driver.getTitle())
                .as("El título no debería cambiar luego del refresh")
                .isEqualTo(titleAntes);
    }
}
