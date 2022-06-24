package de.uniregensburg.oauth.testing;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriver {

    public static RemoteWebDriver getChromeRemoteWebDriver() throws MalformedURLException {

        ChromeOptions options = new ChromeOptions(); // configure browser
        options.addArguments("headless"); // run headless
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model
        
        URL serverurl = new URL("http://chrome:4444/wd/hub"); // remote url of standalone selenium grid instance (chrome is the hostname of docker container)
        RemoteWebDriver driver = new RemoteWebDriver(serverurl,options); // get remote web driver
        
        return driver; // return it
    }

}
