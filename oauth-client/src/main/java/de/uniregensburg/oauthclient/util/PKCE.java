package de.uniregensburg.oauthclient.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class PKCE {
    private static String unreservedChars = "ABCDEFGHIJKLMNOPQRSTUVWXZYabcdefghijklmnopqrstuvwxyz0123456789-._~";
    
    public static String getCodeVerifier() throws PKCEException {
        return getCodeVerifier(128);
    }

    public static String getCodeVerifier(int length) throws PKCEException {
        if (length < 43) {
            throw new PKCEException("Length must be longer than 42 chars");
        }
        if (length > 128) {
            throw new PKCEException("Length must be shorter than 129 chars");
        }
        return generateRandomString(length);
    }

    private static String generateRandomString(int length) {
        Random random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) { 
            code.append(unreservedChars.charAt(random.nextInt(unreservedChars.length())));
        }
        return code.toString();
    }

    public static String generateCodeChallenge(String code) throws PKCEException {
        return generateCodeChallenge(code, CodeChallengeMethod.S256);
    }

    public static String generateCodeChallenge(String code, CodeChallengeMethod transformation) throws PKCEException {
        if (transformation == null) {
            throw new PKCEException("Please select one of the available transformations 'S256' or 'plain'");
        }
        if (!transformation.equals(CodeChallengeMethod.S256) && !transformation.equals(CodeChallengeMethod.PLAIN)) {
            throw new PKCEException("Please select one of the available transformations 'S256' or 'plain'");
        }
        if (code == null || code.isEmpty()) {
            throw new PKCEException("Empty code entered");
        }
        if (transformation.equals(CodeChallengeMethod.PLAIN)) {
            return code;
        }
        if (transformation.equals(CodeChallengeMethod.S256)) {
            byte[] sha256 = sha256(code);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sha256);
        }
        return null;
    }

    public static byte[] sha256(String input) throws PKCEException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new PKCEException(e.getMessage());
        }
        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
        return md.digest(bytes);
    }
}
