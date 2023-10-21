package io.ylab.wallet.domain.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Date;

/**
 * Response for user after successful authentication.
 */
@Builder(toBuilder = true)
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenDetails {
    private long userId;
    private String token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issuedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiresAt;
}
