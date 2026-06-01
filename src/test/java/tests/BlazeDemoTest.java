package tests;

import base.BaseTest;
import data.TestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.BlazeDemoPage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("blazedemo")
@Feature("Búsqueda de Vuelos — BlazeDemo")
@DisplayName("Pruebas de formulario — BlazeDemo")
class BlazeDemoTest extends BaseTest {

    @Test
    @Story("Carga de la página principal")
    @DisplayName("Abrir BlazeDemo muestra el título correcto")
    void shouldOpenWithCorrectTitle() {
        BlazeDemoPage page = new BlazeDemoPage(driver).open();

        assertThat(page.getTitle())
                .as("El título debería contener 'BlazeDemo'")
                .contains("BlazeDemo");
    }

    @Test
    @Story("Tabla de resultados de vuelos")
    @DisplayName("Buscar vuelos Boston → London muestra tabla de resultados")
    void shouldDisplayFlightsTable() {
        BlazeDemoPage page = new BlazeDemoPage(driver)
                .open()
                .selectDeparture(TestData.DEPARTURE_BOSTON)
                .selectDestination(TestData.DESTINATION_LONDON)
                .clickFindFlights();

        assertThat(page.areFlightsDisplayed())
                .as("Debería mostrarse la tabla de vuelos disponibles")
                .isTrue();
    }

    @Test
    @Story("Redirección a página de reserva")
    @DisplayName("Buscar vuelos redirige a la página de reserva")
    void shouldRedirectToReservePage() {
        BlazeDemoPage page = new BlazeDemoPage(driver)
                .open()
                .selectDeparture(TestData.DEPARTURE_PARIS)
                .selectDestination(TestData.DESTINATION_BUENOS_AIRES)
                .clickFindFlights();

        assertThat(page.getCurrentUrl())
                .as("Debería redirigir a la página de reserva")
                .contains("reserve");
    }

    @Test
    @Story("Título de la página de resultados")
    @DisplayName("La página de resultados muestra el título correcto")
    void shouldShowResultsPageTitle() {
        BlazeDemoPage page = new BlazeDemoPage(driver)
                .open()
                .selectDeparture(TestData.DEPARTURE_PORTLAND)
                .selectDestination(TestData.DESTINATION_ROME)
                .clickFindFlights();

        // El título de la home es "BlazeDemo: Book a Flights"
        // El de resultados es "BlazeDemo: reserve"
        assertThat(page.getTitle())
                .as("La página de resultados debería mostrar el título de reserva")
                .contains("reserve");
    }
}
