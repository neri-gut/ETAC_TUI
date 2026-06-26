# Especificación de Diseño: Herramienta CLI CRUD (Spring Boot 3 + Supabase + Java 21)

Este documento detalla la planificación, arquitectura base, configuración de herramientas y el flujo de interactividad para el desarrollo de la herramienta de interfaz de línea de comandos (CLI) de alto rendimiento.

---

## 1. Stack Tecnológico y Requerimientos

- **Lenguaje:** Java 21 (LTS) - Uso exclusivo de *Records*, *Pattern Matching* y *Sealed Classes* donde aplique. Sin Lombok.
- **Framework Base:** Spring Boot 3.3+ (Modo no web/headless para TUI-CLI).
- **Interfaz de Terminal:** Spring Shell Starter 3.2+ (Uso de `ComponentFlow` y `CommandExceptionResolver`).
- **Gestor de Dependencias:** Gradle (Kotlin o Groovy DSL).
- **Cliente HTTP (API):** Spring `RestClient` (Síncrono, bloqueante, ideal para flujos CLI lineales).
- **Base de Datos / Backend-as-a-Service:** Supabase (Acceso vía API REST sobre PostgREST).
- **Compilación de Alto Rendimiento:** GraalVM Native Image (Eliminación del tiempo de arranque de la JVM).

---

## 2. Configuración del Entorno de Construcción (`build.gradle`)

El archivo de configuración de Gradle debe incorporar el plugin de compilación nativa de GraalVM y gestionar las dependencias sin arrastrar componentes web innecesarios para el servidor, pero manteniendo el soporte para el cliente HTTP.

```groovy
plugins {
    id 'org.springframework.boot' version '3.3.0'
    id 'io.spring.dependency-management' version '1.1.5'
    id 'java'
    id 'org.graalvm.buildtools.native' version '0.10.2'
}

group = 'com.supabase.cli'
version = '1.0.0-SNAPSHOT'
sourceCompatibility = '21'

repositories {
    mavenCentral()
}

dependencies {
    // Motor principal de la CLI
    implementation 'org.springframework.shell:spring-shell-starter:3.2.5'
    
    // RestClient y serialización de Jackson nativa de Spring
    implementation 'org.springframework.boot:spring-boot-starter-web'
    
    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.shell:spring-shell-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}

// Configuración de GraalVM Native Image
graalvmNative {
    binaries {
        main {
            imageName = 'supabase-crud-cli' // Nombre del binario ejecutable final
            buildArgs.add('--no-fallback')   // Fuerza la compilación nativa pura sin fallback a JVM
        }
    }
}

```

---

## 3. Arquitectura del Sistema e Inyección de Dependencias

Se implementará un diseño desacoplado donde la interfaz de usuario (Consola) dependa de abstracciones de servicios, abstrayendo por completo el origen de los datos (Supabase API).

```
   [ Capa CLI / TUI ]           --> @ShellComponent (Manejo de Comandos e Interacción)
           │
           ▼ (Inyección por Constructor)
   [ Capa de Servicio ]         --> Interfaces y Clases @Service (Lógica de Negocio)
           │
           ▼ (Inyección por Constructor)
[ Capa de Infraestructura ]     --> Clases Cliente con RestClient (Llamadas a Supabase)

```

### Flujo de Datos y Mapeo Moderno (Java 21)

Para garantizar la inmutabilidad y prescindir de Lombok, la transferencia de datos se realizará mediante **Java Records**.

Ejemplo de definición estructural para el mapeo con Jackson:

```java

public record UsersSupabaseRequest(
        UsersBodyRequest usersBodyRequest,
        String table,
        Array params
) {
}

public record UsersBodyRequest(
        String userIdentifier,
        String userName,
        Integer statusId
) {
}

public record SupabaseResponse(
        UsersSupabaseResponse usersSupabaseResponse
){}

public record UsersSupabaseResponse(
        Integer userId,
        String userIdentifier,
        String userName,
        Integer statusId,
        @JsonProperty("created_at")
        String createdAt
){}

```


---

## 4. Control de Interactividad (Estrategia Híbrida)

La CLI responderá a dos modos de ejecución dentro del mismo comando. El **Modo Interactivo** será el punto de entrada predeterminado para facilitar la usabilidad guiada mediante componentes TUI visuales.

### Reglas de Evaluación de Comando:

1. **Modo Scripting (No Interactivo):** Si el comando es invocado con todos los argumentos requeridos pasados explícitamente (ej. `product create --name "Teclado Mecánico" --price 85.50`), la aplicación procesará la solicitud directamente de manera silenciosa.
2. **Modo TUI (Interactivo - Principal):** Si el comando es invocado de manera aislada (ej. `product create`), se disparará un flujo `ComponentFlow` dinámico que interrogará recursivamente al usuario con prompts validados.

### Ejemplo de Estructura de Control en Comandos:

```java
@ShellMethod(key = "product create", value = "Registrar un nuevo producto")
public String createProduct(
        @ShellOption(defaultValue = ShellOption.NULL, arity = 1) String name,
        @ShellOption(defaultValue = "-1.0", arity = 1) double price) {

    // Evaluación de Modo de Ejecución
    if (name != null && price >= 0) {
        // Ejecución No Interactiva Directa
        Product newProduct = new Product(null, name, price, null);
        productService.save(newProduct);
        return "📦 Producto creado directamente.";
    }

    // Ejecución Interactiva mediante ComponentFlow (TUI)
    ComponentFlow flow = componentFlowBuilder.clone().reset()
            .withStringInput("productName")
                .name("📝 Nombre del Producto: ")
                .and()
            .withStringInput("productPrice")
                .name("💵 Precio ($): ")
                .and()
            .build();

    ComponentFlow.ComponentFlowResult result = flow.run();
    String inputName = result.getContext().get("productName");
    double inputPrice = Double.parseDouble(result.getContext().get("productPrice"));

    productService.save(new Product(null, inputName, inputPrice, null));
    return "✨ Producto creado exitosamente a través del asistente.";
}

```

---

## 5. Manejo Global de Errores y Resiliencia en Terminal

Para evitar fugas de trazas de la JVM (*StackTraces*) y mantener la terminal limpia ante fallos de red o errores HTTP de la API de Supabase, se implementará un puente de excepciones a través de `CommandExceptionResolver`. Bajo ninguna condicion se deben usar bloques try catch que intercepten exepciones genericas, el global exeption handler, debe encargarse de mostrar errores facilmente y evitar que falle la arquitectura.

### Diagrama de Excepciones:

```
[ Error de API / Red (404/401) ] ──> Lanzar SupabaseApiException
                                             │
                                             ▼
                                [ CliExceptionHandler ] (Captura)
                                             │
                                             ▼
                                Imprimir "❌ Error: [Mensaje]"

```

### Configuración del Interceptor de Consola:

```java

@Component
public class GlobalExceptionHandler implements CommandExceptionResolver {

    private static final int ERROR_EXIT_CODE = 1;

    @Override
    public CommandHandlingResult resolve(Exception exception) {
        return switch (exception) {

            // 1. Error personalizado de la integración con Supabase
            case SupabaseApiException supabaseEx -> handleSupabaseError(supabaseEx);

            // 2. Errores nativos de RestClient
            case RestClientResponseException restEx -> handleRestClientError(restEx);

            // 3. Errores de validación de negocio/DTOs contenedores
            case BusinessValidationException validEx -> handleValidationError(validEx);

            // 4. Errores de conectividad (Timeouts, DNS, sin internet)
            case ResourceAccessException netEx -> handleNetworkError(netEx);

            // 5. Captura genérica
            default -> handleUnexpectedError(exception);
        };
    }

    private CommandHandlingResult handleSupabaseError(SupabaseApiException ex) {
        String message = String.format("""
                
                ⚠️  [Error de Infraestructura] -> Fallo en el servidor de Supabase
                🛑 Código HTTP: %d
                📝 Detalle: %s
                """, ex.getStatusCode(), ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleRestClientError(RestClientResponseException ex) {
        String message = String.format("""
                
                ⚡ [Error de Comunicación] -> La API externa devolvió una estructura no esperada.
                🛑 Estado: %s
                🔍 Respuesta Cruda: %s
                """, ex.getStatusText(), ex.getResponseBodyAsString());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleValidationError(BusinessValidationException ex) {
        String message = String.format("""
                
                🛑 [Datos Inválidos] -> La petición no cumple con las reglas de negocio.
                📝 Validación: %s
                """, ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleNetworkError(ResourceAccessException ex) {
        String message = String.format("""
                
                🌐 [Error de Red] -> No se pudo establecer conexión con el servidor.
                🔌 Verifique que su dispositivo cuente con acceso a internet.
                🔍 Detalle: %s
                """, ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }

    private CommandHandlingResult handleUnexpectedError(Throwable ex) {
        String message = String.format("""
                
                ❌ [Fallo Inesperado] -> Ha ocurrido un error no controlado en el sistema.
                🔍 Excepción: %s
                ⚠️  Mensaje Técnico: %s
                💡 Sugerencia: Intente ejecutar el comando mediante el asistente guiado.
                """, ex.getClass().getSimpleName(), ex.getMessage());
        return CommandHandlingResult.of(message, ERROR_EXIT_CODE);
    }
}
```

---

## 6. Configuración de Infraestructura de Comunicaciones (Supabase)

La comunicación se centraliza mediante un bean único de `RestClient`, aislando las cabeceras de seguridad requeridas por PostgREST.

```java
package com.supabase.cli.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class SupabaseClientConfig {

    @Value("${supabase.url}")
    private String url;

    @Value("${supabase.key}")
    private String apiKey;

    @Bean
    public RestClient supabaseRestClient() {
        return RestClient.builder()
                .baseUrl(url + "/rest/v1")
                .defaultHeader("apikey", apiKey)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Prefer", "return=representation")
                .build();
    }
}
```

---

## 7. Elementos Estéticos y de Experiencia de Usuario (UX)

Para cumplir con el requerimiento de una interfaz atractiva y divertida, se aplicarán las siguientes directrices en los outputs de texto:

1. **Uso de Emojis Semánticos:**
* `📦` Operaciones con Productos.
* `👥` Operaciones con Usuarios.
* `✨` Procesos completados con éxito.
* `🛑` Errores de validación de usuario.
* `⚡` Conexiones e infraestructura.


2. **Tablas de Datos Dinámicas:** Para los comandos `list` se prohíbe el uso de volcados de texto plano. Se utilizará el formateador interno de Spring Shell para dibujar cuadrículas con alineación automática basadas en la longitud del texto.
3. **Banner Corporativo:** El archivo `src/main/resources/banner.txt` contendrá un diseño en bloque ASCII representando el espacio de trabajo CLI.

## 8. Estructura de los paquetes para la TUI/CLI.

Para mantener una buena organizacion y facilidad para trabajar con interfaces solo en las librerias para colocar metodos y constructores que ocupara la interfaz en la implementacion.

```Markdown
com.supabase.cli
│
├── command/           <-- 📥 Punto de entrada, comandos o entrada de datos de TUI/CLI para la herramienta.
│       └─── igsf
│              └─── t001
│                     └── IGSFT001.java
│                            └── executeUserCommand()
│                                          └── IGSFR002.executeUserInsert()
│
├── config/             <-- ⚙️ Configuración del RestClient, Beans de Spring, etc.
│      └── SupabaseClientConfig.java
│
├── exception/          <-- ⚠️ Manejo de errores global de la CLI y de cada UUAA
│       ├── CliGlobalExceptionHandler.java
│       └─── igsf
│             ├── UserExeption.java
│             ├── ProductExeption.java
│             └── SupabaseApiException.java
│
├── repository/         <-- 🌐 Librerias para la capa de infraestructura, solo CRUD, sin logica de negocio (Clientes HTTP / RestClient)
│       └─── igsf
│              └─── r001
│                    ├── IGSFR001.java
│                    └── IGSFR001Impl.java
│                           └── executeSupabaseConnection()
│
├── service/            <-- 🧠 Librerias para la lógica de negocio (Puente entre comandos y datos)
│      └─── igsf
│             └─── r002
│                   ├── IGSFR002.java
│                   └── IGSFR002Impl.java 
│                            └── executeUserInsert()
│
└── dto/                <-- 🔗 Los records que utilizaran las librerias (Contratos para toda la comunicacion entre capas)
     └─── igsf
            ├── c001.java
            │     ├─── request
            │     └─── response
            ├── c002java
            │     ├─── request
            │     └─── response
            └── c003.java
                  ├─── request
                  └─── response
```

## 9. Modelado de Entidad Relacion para el proyecto (SQL)

Para poder mantener un orden en el diseño de las tablas para guardar los datos y en si para comunicarnos con supabase y su cliente Rest.

## Diseño de las tablas

```sql

CREATE TABLE public.status (
  statusId bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  statusValue character varying NOT NULL,
  statusDescription character varying NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT status_pkey PRIMARY KEY (statusId)
);
CREATE TABLE public.users (
  userId bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  userIdentifier character varying NOT NULL,
  userName character varying NOT NULL,
  statusId bigint,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT users_pkey PRIMARY KEY (userId),
  CONSTRAINT users_statusId_fkey FOREIGN KEY (statusId) REFERENCES public.status(statusId)
);
CREATE TABLE public.products (
  productId bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  productName character varying NOT NULL,
  productDescription character varying NOT NULL,
  productQuantity bigint NOT NULL,
  statusId bigint,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT products_pkey PRIMARY KEY (productId),
  CONSTRAINT products_statusId_fkey FOREIGN KEY (statusId) REFERENCES public.status(statusId)
);
CREATE TABLE public.history (
  historyId bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  userId bigint,
  actionId bigint,
  productId bigint,
  executionDate timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT history_pkey PRIMARY KEY (historyId),
  CONSTRAINT history_userId_fkey FOREIGN KEY (userId) REFERENCES public.users(userId),
  CONSTRAINT history_productId_fkey FOREIGN KEY (productId) REFERENCES public.products(productId),
  CONSTRAINT history_actionId_fkey FOREIGN KEY (actionId) REFERENCES public.actions(actionId)
);
CREATE TABLE public.actions (
  actionId bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  actionName character varying NOT NULL,
  actionDescription character varying NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT actions_pkey PRIMARY KEY (actionId)
);
```

## 10. Formato de la consulta a la base de datos de forma generica.

Para poder comunicarse a supabase mediante la capa REST, se crea un conector independiente al servicio.

```java
@Override
public <T, R> SupabaseApiResponse<R> executeInsert(SupabaseApiRequest<T> request, Class<R[]> responseType) {
R[] responseBody = supabaseRestClient.post()
.uri(uriBuilder -> {
uriBuilder.path("/" + request.tableName());
if (request.queryParams() != null) {
request.queryParams().forEach(uriBuilder::queryParam);
}
return uriBuilder.build();
})
.body(request.body())
.retrieve()
.body(responseType);

        return new SupabaseApiResponse<>(responseBody != null ? List.of(responseBody) : List.of());
    }

    @Override
    public <R> SupabaseApiResponse<R> executeSelect(SupabaseApiRequest<Void> request, Class<R[]> responseType) {
        R[] responseBody = supabaseRestClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/" + request.tableName());
                    if (request.queryParams() != null) {
                        request.queryParams().forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(responseType);

        return new SupabaseApiResponse<>(responseBody != null ? List.of(responseBody) : List.of());
    }
```

Ejemplo de la clase a usar para el body y response de la request.

```java
public record actionTableRecord(
String actionId,
String actionName,
String actionDescription,
String created_at
) {
}
```