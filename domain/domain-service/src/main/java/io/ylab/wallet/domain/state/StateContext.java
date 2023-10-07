package io.ylab.wallet.domain.state;

public class StateContext {
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
