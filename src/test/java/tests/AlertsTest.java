package tests;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.TheInternetAlertsPage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("alertas")
@Feature("Pop-ups y Alertas JavaScript")
@DisplayName("Pruebas de JavaScript Alerts — The Internet")
class AlertsTest extends BaseTest {

    @Test
    @Story("JS Alert — aceptar")
    @DisplayName("Aceptar un JS Alert muestra el mensaje de confirmación")
    void shouldAcceptJsAlert() {
        TheInternetAlertsPage page = new TheInternetAlertsPage(driver)
                .open()
                .clickJsAlertButton()
                .acceptAlert();

        assertThat(page.getResultMessage())
                .as("Debería confirmar que el alert fue aceptado")
                .isEqualTo("You successfully clicked an alert");
    }

    @Test
    @Story("JS Alert — verificar texto")
    @DisplayName("El texto del JS Alert muestra el mensaje esperado antes de aceptarlo")
    void shouldReadAlertTextBeforeAccepting() {
        TheInternetAlertsPage page = new TheInternetAlertsPage(driver)
                .open()
                .clickJsAlertButton();

        // Verificamos el mensaje del alert ANTES de aceptarlo
        assertThat(page.getAlertText())
                .as("El texto del alert debería decir 'I am a JS Alert'")
                .isEqualTo("I am a JS Alert");

        page.acceptAlert(); // cerrar el alert para no dejar estado sucio
    }

    @Test
    @Story("JS Confirm — aceptar")
    @DisplayName("Aceptar un JS Confirm muestra el mensaje de OK")
    void shouldAcceptJsConfirm() {
        TheInternetAlertsPage page = new TheInternetAlertsPage(driver)
                .open()
                .clickJsConfirmButton()
                .acceptAlert();

        assertThat(page.getResultMessage())
                .as("Debería confirmar que el usuario pulsó OK en el confirm")
                .isEqualTo("You clicked: Ok");
    }

    @Test
    @Story("JS Confirm — cancelar")
    @DisplayName("Cancelar un JS Confirm muestra el mensaje de Cancel")
    void shouldDismissJsConfirm() {
        TheInternetAlertsPage page = new TheInternetAlertsPage(driver)
                .open()
                .clickJsConfirmButton()
                .dismissAlert();

        assertThat(page.getResultMessage())
                .as("Debería confirmar que el usuario pulsó Cancel en el confirm")
                .isEqualTo("You clicked: Cancel");
    }

    @Test
    @Story("JS Prompt — ingresar texto")
    @DisplayName("Ingresar texto en un JS Prompt muestra el texto ingresado en el resultado")
    void shouldTypeTextInJsPrompt() {
        String inputText = "Selenium 2026";

        TheInternetAlertsPage page = new TheInternetAlertsPage(driver)
                .open()
                .clickJsPromptButton()
                .typeInPromptAndAccept(inputText);

        assertThat(page.getResultMessage())
                .as("El resultado debería reflejar el texto ingresado en el prompt")
                .isEqualTo("You entered: " + inputText);
    }
}
