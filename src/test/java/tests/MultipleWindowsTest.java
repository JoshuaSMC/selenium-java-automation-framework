package tests;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.TheInternetWindowsPage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ventanas")
@Feature("Múltiples Ventanas y Pestañas")
@DisplayName("Pruebas de múltiples ventanas — The Internet")
class MultipleWindowsTest extends BaseTest {

    @Test
    @Story("Abrir nueva ventana")
    @DisplayName("Clic en 'Click Here' abre una segunda ventana del browser")
    void shouldOpenNewWindow() {
        TheInternetWindowsPage page = new TheInternetWindowsPage(driver)
                .open()
                .clickOpenNewWindow();

        assertThat(page.getOpenWindowCount())
                .as("Debería haber exactamente 2 ventanas abiertas")
                .isEqualTo(2);
    }

    @Test
    @Story("Contenido de la nueva ventana")
    @DisplayName("La nueva ventana muestra el encabezado 'New Window'")
    void shouldShowCorrectHeaderInNewWindow() {
        TheInternetWindowsPage page = new TheInternetWindowsPage(driver)
                .open()
                .clickOpenNewWindow()
                .switchToNewWindow();

        assertThat(page.getActiveWindowHeader())
                .as("La nueva ventana debería mostrar el título 'New Window'")
                .isEqualTo("New Window");
    }

    @Test
    @Story("Cambio de foco entre ventanas")
    @DisplayName("Después de switchToNewWindow() el handle activo es distinto al principal")
    void shouldSwitchFocusToNewWindow() {
        TheInternetWindowsPage page = new TheInternetWindowsPage(driver)
                .open()
                .clickOpenNewWindow()
                .switchToNewWindow();

        assertThat(page.getCurrentWindowHandle())
                .as("El handle activo no debería ser el de la ventana principal")
                .isNotEqualTo(page.getMainWindowHandle());
    }

    @Test
    @Story("Cerrar ventana y volver")
    @DisplayName("Cerrar la nueva ventana devuelve el foco y queda solo una ventana")
    void shouldReturnToMainWindowAfterClosingNew() {
        TheInternetWindowsPage page = new TheInternetWindowsPage(driver)
                .open()
                .clickOpenNewWindow()
                .switchToNewWindow()
                .closeCurrentWindowAndSwitchBack();

        assertThat(page.getOpenWindowCount())
                .as("Debería quedar solo una ventana abierta")
                .isEqualTo(1);

        assertThat(page.getCurrentWindowHandle())
                .as("El foco activo debería ser la ventana principal")
                .isEqualTo(page.getMainWindowHandle());
    }
}
