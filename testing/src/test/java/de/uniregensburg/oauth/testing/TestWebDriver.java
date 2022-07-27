package de.uniregensburg.oauth.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestWebDriver {

    /**
     * Test selenium web driver browsing google
     */
    @Test
    public void visitGoogle() {
        RemoteWebDriver driver = null;
        try  {
            // get web driver
            driver = WebDriver.getChromeRemoteWebDriver();

            // browse to google
            driver.get("https://google.com");
            // get title as actual
            String actual = driver.getTitle();
            String expected = "Google";

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