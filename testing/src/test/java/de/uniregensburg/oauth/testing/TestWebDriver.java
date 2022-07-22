package de.uniregensburg.oauth.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestWebDriver {

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

}