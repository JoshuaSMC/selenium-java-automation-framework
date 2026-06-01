package tests;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.TheInternetLoginPage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("login")
@Feature("Login — The Internet")
@DisplayName("Pruebas de login — The Internet")
class TheInternetLoginTest extends BaseTest {

    @Test
    @Story("Login exitoso")
    @DisplayName("Login con credenciales válidas redirige al área segura")
    void shouldLoginSuccessfully() {
        TheInternetLoginPage loginPage = new TheInternetLoginPage(driver)
                .open()
                .enterUsername(TheInternetLoginPage.VALID_USER)
                .enterPassword(TheInternetLoginPage.VALID_PASSWORD)
                .clickLogin();

        assertThat(loginPage.isLoginSuccessful())
                .as("Debería mostrar el mensaje de login exitoso")
                .isTrue();

        assertThat(loginPage.getCurrentUrl())
                .as("Debería redirigir al área segura")
                .contains("secure");
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @CsvSource({
        "tomsmith,          contraseña-incorrecta,        Your password is invalid",
        "usuario-invalido,  SuperSecretPassword!,         Your username is invalid"
    })
    @Story("Login fallido — credenciales inválidas")
    @DisplayName("Login con credenciales inválidas muestra el mensaje correcto")
    void shouldShowErrorForInvalidCredentials(String username, String password, String expectedMsg) {
        TheInternetLoginPage loginPage = new TheInternetLoginPage(driver)
                .open()
                .enterUsername(username.trim())
                .enterPassword(password.trim())
                .clickLogin();

        assertThat(loginPage.getFlashMessage())
                .as("El mensaje de error debería corresponder a la credencial inválida")
                .contains(expectedMsg.trim());
    }

    @Test
    @Story("Carga de la página de login")
    @DisplayName("La página de login carga con el título y el formulario visibles")
    void shouldLoadLoginPageCorrectly() {
        TheInternetLoginPage loginPage = new TheInternetLoginPage(driver).open();

        assertThat(loginPage.getPageTitle())
                .as("El encabezado de la página debería decir 'Login Page'")
                .contains("Login Page");

        assertThat(loginPage.isLoginFormDisplayed())
                .as("El formulario de login debería estar visible")
                .isTrue();
    }
}
