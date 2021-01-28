package com.trendyol.selenium.runners;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class Hook {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Hook.class);


    @Value("${selenium.browser.headless}")
    private Boolean headless;

    @Value("${target.application.baseUrl}")
    private String baseUrl;

    private WebDriver driver;

    private WebDriverWait wait;

    @PostConstruct
    public void initialize() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (isDriverLoaded()) {
                LOGGER.info("Shutdown signal detected: Closing opened drivers");
                closeDriver();
                LOGGER.info("Opened drivers closed");
            }
        }));

    }

    private boolean isDriverLoaded() {
        return driver != null;
    }

    public WebDriver getDriver() {
        if (isEmpty(driver)) {
            initializeDriver();
        }
        return driver;
    }

    public WebDriverWait getWebDriverWait(int i) {
        return new WebDriverWait(getDriver(), i);
    }

    public WebDriverWait getWait() {
        if (isEmpty(wait)) {
            initializeDriver();
        }
        return wait;
    }

    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot");
        }
    }

    public void closeDriver() {
        if (driver == null) {
            return;
        }

        driver.quit();
        driver = null;
    }

    private void initializeDriver() {

        if (!isEmpty(driver)) {
            closeDriver();
        }
        System.setProperty("webdriver.chrome.silentOutput", "true");
        String browser = System.getProperty("Browser");
        if (StringUtils.isEmpty(browser) || "chrome".equals(browser)) {
            setChromeDriver();
        } else if ("firefox".equals(browser)) {
            setFirefoxDriver();
        }

        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);

    }

    private void setChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");

        if (TRUE.equals(headless)) {
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("window-size=1920,1080");
        }
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 20, 1000);
    }

    private void setFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxBinary firefoxBinary = new FirefoxBinary();

        if (TRUE.equals(headless)) {
            firefoxBinary.addCommandLineOptions("--headless");
            firefoxOptions.addArguments("--width=1920");
            firefoxOptions.addArguments("--height=1080");
        }

        firefoxOptions.setBinary(firefoxBinary);

        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 20, 1000);
    }

    public void goToBaseUrl() {
        driver.navigate().to(baseUrl);
    }

}