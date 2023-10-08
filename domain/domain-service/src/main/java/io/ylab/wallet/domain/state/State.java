package io.ylab.wallet.domain.state;


import io.ylab.wallet.domain.service.ApplicationService;

public abstract class State {
    protected ApplicationService app;

    public static final StateContext CONTEXT = new StateContext();

    protected State(ApplicationService app) {
        this.app = app;
    }

    public abstract void showMenu();
    public abstract String processRequest();

    public static void clearContext() {
        CONTEXT.clear();
    }

    public static void setContext(String context) {
        CONTEXT.setContext(context);
    }

    public static String getContext() {
        String context = CONTEXT.getContext();
        return context;
    }

    public static String getContextAndClear() {
        String context = CONTEXT.getContext();
        clearContext();
        return context;
    }

}
