package io.ylab.wallet.domain.dto;

import io.ylab.wallet.domain.entity.Account;

import java.util.UUID;

public record UserDto(UUID id, Account account) {
}
