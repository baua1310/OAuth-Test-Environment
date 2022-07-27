package de.uniregensburg.oauth.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestAuthorizationCodeWithPKCEGrant {

    /**
     * Test 1
     * Client frägt Daten des Resource Owners beim Resource Server an.
     * Der Resource Owner meldet sich erfolgreich am Authorization Server an und autorisiert den Zugriff auf die Daten.
     
     * @result  Authorization Server stellt dem Client einen Access Token aus. Der Client erhält unter Angabe des Access Token die Daten vom Resource Server.
     */
    @Test
    public void test1() {
        RemoteWebDriver driver = null;
        try {
            // get web driver
            driver = WebDriver.getChromeRemoteWebDriver();
            // visit web client
            driver.get("http://web-client:8080/demo");
            
            // get login form fields
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement signIn = driver.findElement(By.className("btn-primary"));
            // fill login form
            username.sendKeys("user");
            password.sendKeys("password");
            // submit login form
            signIn.click();

            // give consent
            // is submit form shown?
            Assertions.assertTrue(driver.findElements(By.id("submit-consent")).size() > 0);
            if (driver.findElements(By.id("submit-consent")).size() > 0) {
                // get consent form fields
                WebElement demo = driver.findElement(By.id("demo"));
                WebElement submitConsent = driver.findElement(By.id("submit-consent"));
                // fill consent form
                demo.click();
                // submit consent form
                submitConsent.click();
            }

            // get html body
            WebElement body = driver.findElement(By.tagName("body"));
            // get html body text
            String actual = body.getText();
            // expected text
            String expected = "Demo works!";
            // validate 
            Assertions.assertEquals(expected, actual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // close browser
                driver.quit();
            }
        }
    }

    /**
     * Test 2
     * Client frägt Daten des Resource Owners beim Resource Server an.
     * Der Resource Owner verwendet zur Anmeldung am Authorization Server falsche Anmeldedaten.
     * 
     * @result  Anmeldung am Authorization Server schlägt fehl. Der Authorization Server zeigt Bad Credentials an.
     */
    @Test
    public void test2() {
        RemoteWebDriver driver = null;
        try {
            // get web driver
            driver = WebDriver.getChromeRemoteWebDriver();
            // visit web client
            driver.get("http://web-client:8080/demo");
            
            // get login form fields
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement signIn = driver.findElement(By.className("btn-primary"));
            // fill login form
            username.sendKeys("user");
            password.sendKeys("wrong");
            // submit login form
            signIn.click();

            // is alert shown?
            Assertions.assertTrue(driver.findElements(By.className("alert")).size() > 0);

            // get alert form
            WebElement alert = driver.findElement(By.className("alert"));3
            // get alert text
            String actual = alert.getText();
            // expected alert text
            String expected = "Bad credentials";
            // validate 
            Assertions.assertEquals(expected, actual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // close browser
                driver.quit();
            }
        }
    }

    /**
     * Test 3
     * Client frägt Daten des Resource Owners beim Resource Server an.
     * Der Resource Owner meldet sich erfolgreich am Authorization Server an und lehnt den Zugriff auf die Daten ab.
     * 
     * @result  Zugriff wird nicht gewährt, Authorization Server gibt dem Client die Fehlermeldung access_denied zurück.
     */
    @Test
    public void test3() {
        RemoteWebDriver driver = null;
        try {
            // get web driver
            driver = WebDriver.getChromeRemoteWebDriver();
            // visit web client
            driver.get("http://web-client:8080/demo");
            
            // get login form fields
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement signIn = driver.findElement(By.className("btn-primary"));
            // fill login form
            username.sendKeys("user");
            password.sendKeys("wrong");
            // submit login form
            signIn.click();

            // refuse consent
            // is submit form shown?
            Assertions.assertTrue(driver.findElements(By.id("submit-consent")).size() > 0);
            if (driver.findElements(By.id("submit-consent")).size() > 0) {
                // get consent form fields
                WebElement cancel = driver.findElement(By.id("cancel-consent"));
                // cancel
                cancel.click();
            }

            // get html body
            WebElement body = driver.findElement(By.tagName("body"));
            // get body text
            String bodyText = body.getText();
            // get error message
            String actual = bodyText.substring(bodyText.indexOf("Error:") + 7, bodyText.indexOf("Error:") + 20);
            // expected error message
            String expected = "access_denied";
            // validate 
            Assertions.assertEquals(expected, actual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // close browser
                driver.quit();
            }
        }
    }

}
