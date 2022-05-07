package de.uniregensburg.oauthclient.util;

public enum CodeChallengeMethod {
    PLAIN("plain"),
    S256("S256");

    private final String method;

    CodeChallengeMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
