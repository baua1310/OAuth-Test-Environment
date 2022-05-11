package de.uniregensburg.oauthclient.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Token {
    
    @Id
    @GeneratedValue
    private long id;
    @JsonProperty("token_type")
    private String type;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("access_token")
    @Column(length = 1000)
    private String accessToken;
    @JsonProperty("refresh_token")
    @Column(length = 1000)
    private String refreshToken;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("id_token")
    @Column(length = 1000)
    private String idToken;
    private String username;
    private Date created  = new Date();

    protected Token() {}

    public Token(String type, int expiresIn, String accessToken, String refreshToken, String scope, String idToken) {
        this.type = type;
        this.expiresIn = expiresIn;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.idToken = idToken;
    }

    public String getType() {
        return type;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public Long getId() {
        return id;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isExpired() {
        Date now = new Date();
        boolean expired = (now.getTime() - created.getTime()) / 1000 > expiresIn;
        return expired;
    }

}
