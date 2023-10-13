package io.ylab.wallet.domain.dto;

import lombok.Builder;

@Builder
public record UserResponse(long id,
                           String username,
                           long accountId) {
}
