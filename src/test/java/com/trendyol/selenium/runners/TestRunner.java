package com.trendyol.selenium.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        plugin = {"pretty", "html:results/cucumber-reports.html", "json:results/cucumber.json", "junit:results/cucumber.xml"},
        glue = "com.trendyol.selenium",
        tags = "not @wip")

public class TestRunner {

}
