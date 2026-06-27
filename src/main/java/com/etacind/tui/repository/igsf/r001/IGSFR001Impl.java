package com.etacind.tui.repository.igsf.r001;

import com.etacind.tui.dto.igsf.c001.request.SupabaseApiRequest;
import com.etacind.tui.dto.igsf.c001.response.SupabaseApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;

@Repository
public class IGSFR001Impl implements IGSFR001 {

    private final RestClient supabaseRestClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(IGSFR001Impl.class);

    public IGSFR001Impl(RestClient supabaseRestClient) {
        this.supabaseRestClient = supabaseRestClient;
    }

    @Override
    public <T, R> SupabaseApiResponse<R> executeInsert(SupabaseApiRequest<T> request, Class<R[]> responseType) {
        LOGGER.info("Inicia executeInsert");
        LOGGER.info("request: {}", request);
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

        LOGGER.info("Response Body: {}", (Object) responseBody);
        LOGGER.info("Termina executeInsert");
        return new SupabaseApiResponse<>(responseBody != null ? java.util.Arrays.asList(responseBody) : List.of());
    }

    @Override
    public <R> SupabaseApiResponse<R> executeSelect(SupabaseApiRequest<Void> request, Class<R[]> responseType) {
        LOGGER.info("Inicia executeSelect");
        LOGGER.info("request: {}", request);
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

        LOGGER.info("Response Body: {}", (Object) responseBody);
        LOGGER.info("Termina executeSelect");
        return new SupabaseApiResponse<>(responseBody != null ? java.util.Arrays.asList(responseBody) : List.of());
    }
}
