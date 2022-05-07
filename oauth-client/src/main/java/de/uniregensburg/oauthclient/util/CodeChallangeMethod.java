package de.uniregensburg.oauthclient.util;

public enum CodeChallangeMethod {
    PLAIN("plain"),
    S256("S256");

    private final String method;

    CodeChallangeMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
