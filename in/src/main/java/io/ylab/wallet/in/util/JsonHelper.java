package io.ylab.wallet.in.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.wallet.in.exception.ApiError;
import lombok.experimental.UtilityClass;

/**
 * Helper class for json parsing and building json responses.
 */
@UtilityClass
public class JsonHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String buildErrorResponse(String message, int status) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ApiError.builder()
                .message(message)
                .status(status)
                .build());
    }
}
