package io.ylab.wallet.in.controller;

import io.swagger.annotations.ApiOperation;
import io.ylab.wallet.domain.dto.UserRequest;
import io.ylab.wallet.domain.security.SecurityService;
import io.ylab.wallet.domain.security.TokenDetails;
import io.ylab.wallet.in.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that authenticates user and sends jwt token in response.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final SecurityService securityService;

    @PostMapping(produces = "application/json")
    @ApiOperation("authenticates user and sends token in response")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDetails> authenticate(@RequestBody UserRequest userRequest) {
        ValidationUtils.validateUserRequest(userRequest);
        TokenDetails tokenDetails = securityService.authenticate(userRequest.username(), userRequest.password());
        return ResponseEntity.ok(tokenDetails);
    }
}
