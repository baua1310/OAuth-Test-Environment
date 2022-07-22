package de.uniregensburg.oauth.testing;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestSeleniumTest {

    @Test
    public void visitGoogle() {
        RemoteWebDriver driver = null;
        try  {
            driver = WebDriver.getChromeRemoteWebDriver();

            driver.get("https://google.com");
            String title = driver.getTitle();

            Assertions.assertEquals("Google", title);
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testRedirectToLogin() {
        RemoteWebDriver driver = null;
        try  {

            driver = WebDriver.getChromeRemoteWebDriver();

            driver.get("http://web-client:8080/demo");

            String actual = driver.getTitle();

            String expected = "Please sign in";

            Assertions.assertEquals(expected, actual);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testDemo() {
        RemoteWebDriver driver = null;
        try {

            driver = WebDriver.getChromeRemoteWebDriver();

            driver.get("http://web-client:8080/demo");
            
            // Login

            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement signIn = driver.findElement(By.className("btn-primary"));

            username.sendKeys("user");
            password.sendKeys("password");

            signIn.click();

            if (driver.findElements(By.id("submit-consent")).size() > 0) {
                WebElement demo = driver.findElement(By.id("demo"));
                WebElement submitConsent = driver.findElement(By.id("submit-consent"));

                demo.click();
                submitConsent.click();
            }

            WebElement body = driver.findElement(By.tagName("body"));
            String actual = body.getText();

            String expected = "Demo works!";
    
            Assertions.assertEquals(expected, actual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testInvalidCredentials() {
        RemoteWebDriver driver = null;
        try {

            driver = WebDriver.getChromeRemoteWebDriver();

            driver.get("http://web-client:8080/demo");
            
            // Login

            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement signIn = driver.findElement(By.className("btn-primary"));

            username.sendKeys("user");
            password.sendKeys("wrong");

            signIn.click();

            Assertions.assertTrue(driver.findElements(By.className("alert")).size() > 0);

            WebElement alert = driver.findElement(By.className("alert"));
            String actual = alert.getText();

            String expected = "Bad credentials";
    
            Assertions.assertEquals(expected, actual);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}