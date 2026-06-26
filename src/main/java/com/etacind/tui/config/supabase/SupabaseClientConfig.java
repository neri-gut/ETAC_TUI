package com.etacind.tui.config.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

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
        return RestClient.builder()
                .baseUrl(url + ConstantSupabase.REST_V1.getValue())
                .defaultHeader(ConstantSupabase.API_KEY.getValue(), apiKey)
                .defaultHeader(ConstantSupabase.CONTENT_TYPE.getValue(), String.valueOf(ConstantSupabase.APLICATION_JSON))
                .defaultHeader(ConstantSupabase.PREFER.getValue(), String.valueOf(ConstantSupabase.RETURN_REPRESENTATION))
                .build();
    }
}
