package io.ylab.wallet.domain.state;

/**
 * Simple structure for storing necessary data between user inputs.
 */
public class StateContext {
    /**
     * Contains necessary data between user inputs.
     */
    private String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void clear() {
        this.context = null;
    }
}
