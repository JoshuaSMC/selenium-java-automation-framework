package data;

/**
 * Centraliza los datos de test para evitar strings mágicos dispersos en los tests.
 * Si un sitio cambia una opción de dropdown o una URL, se corrige en un solo lugar.
 */
public class TestData {

    private TestData() { /* clase de constantes, no instanciable */ }

    // ── BlazeDemo — URLs ─────────────────────────────────────────────────────
    public static final String BLAZEDEMO_HOME_URL     = "https://blazedemo.com/";
    public static final String BLAZEDEMO_PURCHASE_URL = "https://blazedemo.com/purchase.php";

    // ── BlazeDemo — ciudades de origen ───────────────────────────────────────
    public static final String DEPARTURE_BOSTON   = "Boston";
    public static final String DEPARTURE_PARIS    = "Paris";
    public static final String DEPARTURE_PORTLAND = "Portland";

    // ── BlazeDemo — ciudades de destino ──────────────────────────────────────
    public static final String DESTINATION_LONDON       = "London";
    public static final String DESTINATION_BUENOS_AIRES = "Buenos Aires";
    public static final String DESTINATION_ROME         = "Rome";
}
