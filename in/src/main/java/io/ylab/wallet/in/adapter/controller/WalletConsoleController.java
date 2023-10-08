package io.ylab.wallet.in.adapter.controller;

import io.ylab.wallet.domain.port.input.controller.WalletController;

import java.io.*;

public class WalletConsoleController implements WalletController {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String getInput() {
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода!");
            ;
        }
        return input;
    }

}
