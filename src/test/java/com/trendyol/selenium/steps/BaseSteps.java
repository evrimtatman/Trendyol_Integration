package com.trendyol.selenium.steps;

import com.trendyol.selenium.pages.HomePage;
import com.trendyol.selenium.runners.Hook;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.openqa.selenium.support.PageFactory.initElements;

public class BaseSteps {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSteps.class);

    @Autowired
    private Hook hooks;

    @Autowired
    HomePage homePage;

    @Before(order = 1)
    public void logBeforeScenario(final Scenario scenario) {
        LOGGER.debug(StringUtils.rightPad("Starting scenario:", 20) + "[{}] - [{}]",
                getFeatureName(scenario),
                scenario.getName());
    }

    @Before(order = 2)
    public void initializeDriver() {
        final WebDriver driver = hooks.getDriver();
        initElements(driver, homePage);

    }

    @After(order = 1)
    public void closeDriver(final Scenario scenario) {
        hooks.tearDown(scenario);
        //hooks.closeDriver();
    }

    @After(order = Integer.MAX_VALUE)
    public void logAfterScenario(final Scenario scenario) {
        LOGGER.debug(StringUtils.rightPad("Finished scenario:", 20) + "[{}] - [{}] [{}]",
                getFeatureName(scenario),
                scenario.getName(),
                scenario.getStatus());
    }

    private String getFeatureName(Scenario scenario) {
        String featureName = scenario.getId();

        featureName = StringUtils.substringBeforeLast(featureName, ".feature");
        featureName = StringUtils.substringAfterLast(featureName, "/");

        return featureName;
    }
}
