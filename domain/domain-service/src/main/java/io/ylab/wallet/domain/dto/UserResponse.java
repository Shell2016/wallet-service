package io.ylab.wallet.domain.dto;

import lombok.Builder;

/**
 * User response dto that serves as response after successful user creation.
 * @param id of newly created user. This id must be used in next interactions with API.
 * @param username of newly created user.
 */
@Builder
public record UserResponse(long id,
                           String username) {
}
