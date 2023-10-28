package io.ylab.wallet.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * Dto for user create and auth actions.
 *
 * @param username must be not empty
 * @param password length must be >= 6
 */
@Builder
public record UserRequest(@ApiModelProperty(required = true)
                          String username,
                          @ApiModelProperty(required = true, notes = "6 characters or more")
                          String password) {
}
