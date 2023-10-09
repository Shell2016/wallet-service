package io.ylab.wallet.domain.port.input.controller;

/**
 * Controller interface that gets user input.
 * Acts as input port in onion architecture.
 */
public interface WalletController {
    String getInput();
}
