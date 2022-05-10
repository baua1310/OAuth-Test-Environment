package de.uniregensburg.oauthclient.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class State {
   
    @Id
    @GeneratedValue
    private Long id;
    private String redirectUri;
    private String codeVerifier;

    protected State() {}

    public State(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public State(String redirectUri, String codeVerifier) {
        this.redirectUri = redirectUri;
        this.codeVerifier = codeVerifier;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Long getId() {
        return id;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

}
