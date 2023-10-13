package io.ylab.wallet.domain.dto;

import io.ylab.wallet.domain.entity.Account;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(UUID id, Account account) {
}
