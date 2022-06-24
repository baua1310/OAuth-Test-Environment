package de.uniregensburg.oauth.testing;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriver {

    public static RemoteWebDriver getChromeRemoteWebDriver() {

        ChromeOptions options = new ChromeOptions(); // configure browser
        //options.addArguments("headless"); // run headless
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        
        // remote url of standalone selenium grid instance (chrome is the hostname of docker container)
        URL serverurl = null;
        try {
            serverurl = new URL("http://chrome:4444/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } 
        RemoteWebDriver driver = new RemoteWebDriver(serverurl,options); // get remote web driver

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // set timeout of 10 seconds
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10)); // set timeout of 10 seconds
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10)); // set timeout of 10 seconds
        
        return driver; // return it
    }

}
