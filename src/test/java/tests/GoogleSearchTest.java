package tests;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.GooglePage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("google")
@Feature("Búsqueda en Google")
@DisplayName("Pruebas de búsqueda en Google")
class GoogleSearchTest extends BaseTest {

    @Test
    @Story("Carga de la página de inicio")
    @DisplayName("Abrir Google muestra el título correcto")
    void shouldOpenGoogleWithCorrectTitle() {
        GooglePage google = new GooglePage(driver).open();

        assertThat(google.getTitle())
                .as("El título debería contener 'Google'")
                .contains("Google");
    }

    @Test
    @Story("Resultados de búsqueda")
    @DisplayName("Buscar un término devuelve resultados")
    void shouldShowResultsAfterSearch() {
        GooglePage google = new GooglePage(driver)
                .open()
                .search("Selenium WebDriver");

        assertThat(google.getResultTitles())
                .as("Debería haber al menos un resultado orgánico")
                .isNotEmpty();
    }

    @Test
    @Story("URL de resultados")
    @DisplayName("La URL de resultados refleja el término buscado")
    void searchUrlShouldContainSearchTerm() {
        GooglePage google = new GooglePage(driver)
                .open()
                .search("JUnit 5");

        assertThat(google.getCurrentUrl())
                .as("La URL debería reflejar el término buscado")
                .contains("JUnit");
    }

    @ParameterizedTest(name = "Buscar ''{0}'' devuelve resultados")
    @ValueSource(strings = { "Java", "Spring Boot", "Testing automatizado" })
    @Story("Búsqueda parametrizada")
    @DisplayName("Búsquedas parametrizadas devuelven resultados")
    void shouldReturnResultsForMultipleTerms(String term) {
        GooglePage google = new GooglePage(driver)
                .open()
                .search(term);

        assertThat(google.getResultTitles())
                .as("La búsqueda de '%s' debería tener resultados", term)
                .isNotEmpty();
    }
}
