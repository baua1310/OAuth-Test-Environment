package de.uniregensburg.oauth.testing;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestSeleniumTest {

    @Test
    public void visitGoogle() throws MalformedURLException {
        RemoteWebDriver driver = WebDriver.getChromeRemoteWebDriver();

        driver.get("https://google.com");
        String title = driver.getTitle();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        Assertions.assertEquals("Google", title);
        
        driver.quit();
    }
}