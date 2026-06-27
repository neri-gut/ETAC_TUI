package com.etacind.tui.config.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.JdkClientHttpRequestFactory;

/**
 * Configuración del cliente RestClient para comunicación con Supabase/PostgREST.
 * Define los headers fijos requeridos para todas las peticiones a la API.
 */
@Configuration
public class SupabaseClientConfig {

    @Value("${supabase.url}")
    private String url;

    @Value("${supabase.key}")
    private String apiKey;

    @Bean
    public RestClient supabaseRestClient() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
                java.net.http.HttpClient.newBuilder()
                        .connectTimeout(java.time.Duration.ofSeconds(5))
                        .build()
        );

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(url + ConstantSupabase.REST_V1.getValue())
                .defaultHeader(ConstantSupabase.API_KEY.getValue(), apiKey)
                .defaultHeader(ConstantSupabase.CONTENT_TYPE.getValue(), ConstantSupabase.APLICATION_JSON.getValue())
                .defaultHeader(ConstantSupabase.PREFER.getValue(), ConstantSupabase.RETURN_REPRESENTATION.getValue())
                .requestInterceptor((request, body, execution) -> {
                    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("SupabaseRequestLogger");
                    logger.info("=== PETICION RESTCLIENT ===");
                    logger.info("URI: {}", request.getURI());
                    logger.info("Metodo: {}", request.getMethod());
                    logger.info("Body: {}", new String(body, java.nio.charset.StandardCharsets.UTF_8));
                    logger.info("===========================");
                    return execution.execute(request, body);
                })
                .build();
    }
}
