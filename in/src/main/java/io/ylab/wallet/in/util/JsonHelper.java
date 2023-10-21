package io.ylab.wallet.in.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.wallet.domain.exception.ApiError;
import io.ylab.wallet.domain.exception.ValidationException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Helper class for json parsing and building json responses.
 */
public class JsonHelper {

    private final ObjectMapper objectMapper;

    public JsonHelper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public String buildErrorResponse(String message, int status) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ApiError.builder()
                .message(message)
                .status(status)
                .build());
    }

    public String buildJsonFromObject(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    public <T> T getRequestBody(HttpServletRequest req, Class<T> clazz) {
        try (ServletInputStream inputStream = req.getInputStream()) {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new ValidationException("Невалидный формат запроса!");
        }
    }
}
