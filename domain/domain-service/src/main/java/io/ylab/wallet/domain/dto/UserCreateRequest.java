package io.ylab.wallet.domain.dto;

import lombok.Builder;

@Builder
public record UserCreateRequest(String username,
                                String password) {
}
