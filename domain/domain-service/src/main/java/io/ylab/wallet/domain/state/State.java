package io.ylab.wallet.domain.state;

import io.ylab.wallet.domain.service.ApplicationService;

/**
 * Base class for State pattern implementation.
 */
public abstract class State {
    /**
     * Class with several states.
     */
    protected ApplicationService app;
    /**
     * Using for storing necessary data between user inputs(simple version of session managing).
     */
    public static final StateContext CONTEXT = new StateContext();

    protected State(ApplicationService app) {
        this.app = app;
    }

    /**
     * This method is part of template method run() in ApplicationService class.
     * Must be implemented by concrete state classes.
      */
    public abstract void showMenu();
    /**
     * This method is part of template method run() in ApplicationService class.
     * Must be implemented by concrete state classes.
     */
    public abstract String processRequest();

    /**
     * Clears context.
     */
    public static void clearContext() {
        CONTEXT.clear();
    }

    /**
     * Sets data in context.
     * @param context data that will be stored between user inputs
     */
    public static void setContext(String context) {
        CONTEXT.setContext(context);
    }

    /**
     * Gets data from context.
     * @return data that was stored between user inputs
     */
    public static String getContext() {
        String context = CONTEXT.getContext();
        return context;
    }

    /**
     * Gets data from context and clears it.
     * @return data that was stored between user inputs
     */
    public static String getContextAndClear() {
        String context = CONTEXT.getContext();
        clearContext();
        return context;
    }
}
