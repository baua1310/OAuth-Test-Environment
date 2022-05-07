package de.uniregensburg.oauthclient;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.uniregensburg.oauthclient.util.CodeChallangeMethod;
import de.uniregensburg.oauthclient.util.PKCE;
import de.uniregensburg.oauthclient.util.PKCEException;

public class PKCETests {

    @Test
    public void testGenerateCodeVerifier() {
        assertDoesNotThrow(() -> {
            String code = PKCE.getCodeVerifier();
            System.out.println(code);
        });
    }

    @Test
    public void testGenerateCodeVerifierShort() {
        Exception exception = assertThrows(PKCEException.class, () -> {
            PKCE.getCodeVerifier(42);
        });
    
        String expectedMessage = "Length must be longer than 42 chars";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGenerateCodeVerifierMiddle() {
        assertDoesNotThrow(() -> {
            String code = PKCE.getCodeVerifier(96);
            System.out.println(code);
        });
    }

    @Test
    public void testGenerateCodeVerifierLong() {
        Exception exception = assertThrows(PKCEException.class, () -> {
            PKCE.getCodeVerifier(129);
        });
    
        String expectedMessage = "Length must be shorter than 129 chars";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGenerateCodeChallangeTransformationNull() {
        Exception exception = assertThrows(PKCEException.class, () -> {
            String code = null;
            CodeChallangeMethod transformation = null;
            PKCE.generateCodeChallange(code, transformation);
        });
    
        String expectedMessage = "Please select one of the available transformations 'S256' or 'plain'";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGenerateCodeChallangeCodeEmpty() {
        Exception exception = assertThrows(PKCEException.class, () -> {
            String code = "";
            PKCE.generateCodeChallange(code);
        });
    
        String expectedMessage = "Empty code entered";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGenerateCodeChallangeEmptyNull() {
        Exception exception = assertThrows(PKCEException.class, () -> {
            String code = null;
            PKCE.generateCodeChallange(code);
        });
    
        String expectedMessage = "Empty code entered";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGenerateCodeChallangePlain() throws PKCEException {
        String code = "Test";
        CodeChallangeMethod transformation = CodeChallangeMethod.PLAIN;
    
        String codeChallange = PKCE.generateCodeChallange(code, transformation);

        assertTrue(codeChallange.equals(code));
    }

    @Test
    public void testGenerateCodeChallangeS256() throws PKCEException {
        String code = "No2e8.K7lBhdO2GaRhfLkyj-ayHSrKY9aCt.-IzQiJgSnwNWf9Whk0EKZ42j04~2nf0ubUyu~YU8Ea7q2HB5cvSVmAdOw8dfCWTQbt_gFgDXxFJAGCFLNUxgogTuYylS";
        CodeChallangeMethod transformation = CodeChallangeMethod.S256;
        String expectedString = "9WvZ3Irc0Wokcvb_Y-PwSCrMO6MRwVYXL4I-rvZrqnw";
    
        String codeChallange = PKCE.generateCodeChallange(code, transformation);

        assertTrue(codeChallange.equals(expectedString));
    }

    @Test
    public void testSha256() throws PKCEException {
        String input = "No2e8.K7lBhdO2GaRhfLkyj-ayHSrKY9aCt.-IzQiJgSnwNWf9Whk0EKZ42j04~2nf0ubUyu~YU8Ea7q2HB5cvSVmAdOw8dfCWTQbt_gFgDXxFJAGCFLNUxgogTuYylS";
        
        byte[] expectedBytes = {-11,107,-39,-36,-118,-36,-47,106,36,114,-10,-1,99,-29,-16,72,42,-52,59,-93,17,-63,86,23,47,-126,62,-82,-10,107,-86,124};

        byte[] actualBytes = PKCE.sha256(input);
        for (byte b : actualBytes) {
            System.out.print(b + ",");
        }

        assertArrayEquals(actualBytes,expectedBytes);
    }

}
