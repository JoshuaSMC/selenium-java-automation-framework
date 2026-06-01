# 🧪 Selenium Test — Automatización de pruebas web

Proyecto educativo mejorado de automatización de pruebas con Selenium WebDriver.
Cubre los fundamentos y buenas prácticas del testing automatizado aplicadas a sitios reales:
**Page Object Model**, **Explicit Waits**, **Allure Reports**, manejo de **JS Alerts**,
**múltiples ventanas**, tests parametrizados, screenshots en fallos y ejecución headless.

---

## ⚙️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 21+ | Lenguaje principal |
| Selenium WebDriver | 4.27 | Automatización del browser |
| Selenium Manager | incluido en 4.6+ | Gestión automática de ChromeDriver (sin path manual) |
| JUnit 5 | 5.11 | Framework de testing + `@ParameterizedTest` |
| AssertJ | 3.26 | Assertions fluidas con mensajes descriptivos |
| Allure | 2.29 | Reportes HTML interactivos con pasos y capturas |
| AspectJ Weaver | 1.9.24 | Intercepción de `@Step` en Page Objects |
| Maven | 3.8+ | Gestión de dependencias y ejecución |

---

## 🗂️ Estructura del proyecto

```
selenium-test/
├── pom.xml
└── src/
    └── test/
        └── java/
            ├── base/
            │   └── BaseTest.java                  # Setup de Chrome, screenshot en fallo, teardown
            ├── data/
            │   └── TestData.java                  # Constantes centralizadas — sin magic strings
            ├── pages/                              # Page Object Model
            │   ├── GooglePage.java                # Búsqueda + espera de navegación
            │   ├── BlazeDemoPage.java             # Formulario con Select y FluentWait
            │   ├── TheInternetLoginPage.java      # Login con locators variados
            │   ├── TheInternetAlertsPage.java     # JS alert(), confirm(), prompt()
            │   └── TheInternetWindowsPage.java    # Múltiples ventanas/pestañas
            ├── tests/
            │   ├── NavigationTest.java            # back(), forward(), refresh(), navigate.to()
            │   ├── GoogleSearchTest.java          # Búsquedas + @ParameterizedTest con @ValueSource
            │   ├── BlazeDemoTest.java             # Formulario con Select y redirección
            │   ├── TheInternetLoginTest.java      # Login válido + @ParameterizedTest con @CsvSource
            │   ├── AlertsTest.java                # JS Alert, Confirm (accept/dismiss), Prompt
            │   └── MultipleWindowsTest.java       # Abrir, cambiar foco, cerrar ventana
            └── utils/
                ├── ScreenshotUtil.java            # Captura PNG resiliente (maneja alerts abiertos)
                └── WaitUtils.java                 # FluentWait con polling e ignored exceptions
```

---

## 🚀 Instalación local

### Requisitos
- Java 21+
- Maven 3.8+
- Google Chrome instalado

> **No necesitás descargar ChromeDriver manualmente.**
> Selenium Manager (incluido en Selenium 4.6+) lo descarga y gestiona automáticamente.

### Clonar el repositorio
```bash
git clone https://github.com/JoshuaSMC/selenium-test.git
cd selenium-test
```

### Correr todos los tests
```bash
mvn test
```

### Correr en modo headless (sin abrir el browser)
```bash
mvn test -Dheadless=true
```

### Correr tests por tag
```bash
mvn test -Dgroups=login
mvn test -Dgroups=navegacion
mvn test -Dgroups=google
mvn test -Dgroups=blazedemo
mvn test -Dgroups=alertas
mvn test -Dgroups=ventanas
```

### Ver reporte Allure

Después de correr los tests, servir el reporte en el browser:
```bash
mvn allure:serve
```

O generar el HTML estático en `target/site/allure-maven-plugin/`:
```bash
mvn allure:report
```

> El reporte muestra cada test agrupado por `@Feature` → `@Story`, con los pasos
> individuales de cada Page Object (`@Step`) y las capturas de pantalla adjuntas en fallos.

---

## 🧪 Tests incluidos

| Suite | Tests | Qué verifica |
|-------|-------|-------------|
| `NavigationTest` | 4 | `back()`, `forward()`, `refresh()`, `navigate.to()` |
| `GoogleSearchTest` | 6 | Título, resultados, URL + `@ParameterizedTest` × 3 términos |
| `BlazeDemoTest` | 4 | Título, tabla de vuelos, redirección, cambio de título |
| `TheInternetLoginTest` | 4 | Login válido + `@ParameterizedTest` × 2 credenciales inválidas |
| `AlertsTest` | 5 | JS Alert (accept + leer texto), Confirm (accept/dismiss), Prompt |
| `MultipleWindowsTest` | 4 | Abrir ventana, verificar contenido, cambio de foco, cerrar y volver |

**Total: 27 tests** distribuidos en 6 suites.

---

## 🧩 Patrones y conceptos aplicados

### Page Object Model (POM) con Fluent Interface
Cada sitio tiene su propia clase que encapsula locators y acciones.
Los tests no conocen el DOM — solo encadenan llamadas a métodos de negocio.

```java
new BlazeDemoPage(driver)
    .open()
    .selectDeparture("Boston")
    .selectDestination("London")
    .clickFindFlights();
```

### Explicit Wait — WebDriverWait y FluentWait
Se espera a que el elemento esté en el estado correcto antes de interactuar.
No se usa `implicitlyWait` — mezclar ambos genera timeouts impredecibles.

```java
// WebDriverWait: para la mayoría de los casos
wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

// FluentWait (via WaitUtils): cuando el DOM puede estar transitoriamente stale
WaitUtils.build(driver, Duration.ofSeconds(10), Duration.ofMillis(500))
         .until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
```

### Tests parametrizados
`@CsvSource` para múltiples combinaciones de datos, `@ValueSource` para listas simples.

```java
// @CsvSource — múltiples pares de entrada/salida esperada
@ParameterizedTest(name = "[{index}] {2}")
@CsvSource({
    "tomsmith, contraseña-incorrecta, Your password is invalid",
    "usuario-invalido, SuperSecretPassword!, Your username is invalid"
})
void shouldShowErrorForInvalidCredentials(String user, String pass, String msg) { ... }

// @ValueSource — misma assertion, distintos valores de entrada
@ParameterizedTest(name = "Buscar ''{0}'' devuelve resultados")
@ValueSource(strings = { "Java", "Spring Boot", "Testing automatizado" })
void shouldReturnResultsForMultipleTerms(String term) { ... }
```

### Pop-ups y Alertas JavaScript
`driver.switchTo().alert()` expone `accept()`, `dismiss()`, `getText()` y `sendKeys()`.

```java
// Separar click, lectura e interacción para poder verificar el texto del alert
page.clickJsAlertButton();
assertThat(page.getAlertText()).isEqualTo("I am a JS Alert");
page.acceptAlert();

// Prompt: sendKeys() antes de accept()
Alert prompt = driver.switchTo().alert();
prompt.sendKeys("mi texto");
prompt.accept();
```

### Múltiples Ventanas y Pestañas
El handle de la ventana principal se captura en el **constructor** del Page Object,
no en `open()`, para garantizar que siempre esté inicializado.

```java
public TheInternetWindowsPage(WebDriver driver) {
    this.driver           = driver;
    this.wait             = new WebDriverWait(driver, Duration.ofSeconds(10));
    this.mainWindowHandle = driver.getWindowHandle(); // antes de abrir ninguna otra
}
```

Flujo completo de manejo de ventanas:
```java
// Esperar a que el nuevo handle aparezca
wait.until(d -> d.getWindowHandles().size() > windowsBefore);

// Cambiar al handle que no sea el principal
for (String handle : driver.getWindowHandles()) {
    if (!handle.equals(mainWindowHandle)) {
        driver.switchTo().window(handle);
        break;
    }
}

// Cerrar y volver — driver.close() cierra solo la ventana activa
driver.close();
driver.switchTo().window(mainWindowHandle);
```

### Allure Reports
`@Feature` agrupa tests por área funcional, `@Story` los sub-clasifica por escenario.
`@Step` en los Page Objects registra cada acción como paso navegable en el reporte.

```java
// En el test:
@Feature("Login — The Internet")
@Story("Login exitoso")
@Test void shouldLoginSuccessfully() { ... }

// En el Page Object:
@Step("Ingresar usuario '{username}'")
public LoginPage enterUsername(String username) { ... }
```

### Datos de test centralizados
`TestData` reúne todas las constantes de test en un único lugar.
Si un sitio cambia una URL o un valor de dropdown, se corrige en un solo archivo.

```java
// En TestData:
public static final String DEPARTURE_BOSTON   = "Boston";
public static final String BLAZEDEMO_HOME_URL = "https://blazedemo.com/";

// En el test:
.selectDeparture(TestData.DEPARTURE_BOSTON)
```

### Screenshot resiliente ante alerts abiertos
`TakesScreenshot.getScreenshotAs()` lanza `UnhandledAlertException` si hay un dialog
nativo abierto. `ScreenshotUtil` descarta el alert antes de capturar para no suprimir
el mensaje del fallo original.

```java
private static void dismissAlertIfPresent(WebDriver driver) {
    try {
        driver.switchTo().alert().dismiss();
    } catch (Exception ignored) { }
}
```

### Tipos de locators utilizados

| Tipo | Ejemplo en el proyecto |
|------|----------------------|
| `By.id` | `By.id("username")` |
| `By.name` | `By.name("fromPort")` |
| `By.cssSelector` | `By.cssSelector("button[type='submit']")` |
| `By.cssSelector` (atributo) | `By.cssSelector("button[onclick='jsAlert()']")` |
| `By.xpath` | `By.xpath("//form[@id='login']")` |

---

## 👤 Autor

- [@JoshuaSMC](https://github.com/JoshuaSMC)

---

## 📄 Licencia

MIT
