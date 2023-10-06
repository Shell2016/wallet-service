package io.ylab.wallet.service.in;

import io.ylab.wallet.service.application.controller.WalletController;

import java.io.*;

public class WalletConsoleController implements WalletController {

    private final BufferedReader reader;

    public WalletConsoleController() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getInput() {
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода!");;
        }
        return input;
    }

}
