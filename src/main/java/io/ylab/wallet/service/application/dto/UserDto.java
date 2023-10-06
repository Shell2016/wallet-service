package io.ylab.wallet.service.application.dto;

import io.ylab.wallet.service.core.entity.Account;

import java.util.UUID;

public record UserDto(UUID id, Account account) {
}
