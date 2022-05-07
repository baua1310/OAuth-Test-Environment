package de.uniregensburg.oauthclient.util;

public enum ResponseType {
    CODE("code"),
    CODE_AND_ID_TOKEN("code+id_token");

    private final String type;

    ResponseType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
