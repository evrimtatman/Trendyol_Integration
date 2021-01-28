package com.trendyol.selenium.pages;

import com.trendyol.selenium.runners.Hook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class HomePage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    @Autowired
    Hook hook;

    @FindBy(how = How.CSS, using = "#search_form_input_homepage")
    private WebElement searchInput;

    @FindBy(how = How.CSS, using = "#account-navigation-container > div > div.account-nav-item.user-login-container > div.link.account-user > p")
    private WebElement userLogin;

    @FindBy(how = How.CSS, using = "#login-email")
    private WebElement usernameInput;

    @FindBy(how = How.ID, using = "login-password-input")
    private WebElement passwordInput;

    @FindBy(how = How.CSS, using = "button.q-primary.q-fluid.q-button-medium.q-button.submit")
    private WebElement login;

    @FindBy(how = How.XPATH, using = "//p[contains(.,'Hesabım')]")
    private WebElement loginIcon;

    @FindBy(how = How.XPATH, using = "//p[contains(.,'Sepetim')]")
    private WebElement basketIcon;

    @FindBy(how = How.XPATH, using = "//div[@class='add-to-bs-tx']")
    private WebElement addBasketButton;


    public String getIconText() {
        hook.getWebDriverWait(10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(.,'Hesabım')]")));
        return loginIcon.getText();
    }

    public void addToBasket() {
        addBasketButton.click();
    }


    public void selectRandomShop() {
        clickRandom("component-item");
    }

    public void selectRandomProduct() {
        clickRandom("boutique-product");
    }

    public void gotoBasket() {
        basketIcon.click();
    }

    public boolean isAddedProduct(String oldUrl) {
        List<WebElement> productInBasket = hook.getDriver().findElements(By.tagName("a"));
        Optional<WebElement> element = productInBasket.stream().filter(webElement ->
                oldUrl.contains(webElement.getAttribute("href"))).findFirst();
        return element.isPresent();

    }

    public void clickRandom(String target) {
        List<WebElement> shops = hook.getDriver().findElements(By.className(target));
        Random rand = new Random();
        int index = rand.nextInt(shops.size());
        WebElement element = shops.get(index);
        element.click();
    }

    public void clickBoutiqueButtonWithName(String name) {
        hook.getWebDriverWait(10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='" + name + "']")));
        hook.getDriver().findElement(new By.ByXPath("//*[text()='" + name + "']")).click();
    }


    public List<String> checkImages() {
        List<WebElement> links = hook.getDriver().findElements(By.tagName("img"));
        List<String> brokenLinks = new ArrayList<>();

        links.stream().forEach(webElement -> {
            String linkURL = webElement.getAttribute("src");
            log.info("Checking image with url {}", linkURL);

            try {
                URL url = new URL(linkURL);
                HttpURLConnection http = getHttpURLConnection(url);
                int statusCode = http.getResponseCode();
                if (statusCode == 404 || statusCode == 500)
                    brokenLinks.add(linkURL);
            } catch (IOException exception) {
                brokenLinks.add(linkURL);
                log.error("Exception occurred while getting http response code {}", exception);
            }
        });
        return brokenLinks;

    }

    public void waitForLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        hook.getWebDriverWait(30).until(pageLoadCondition);
    }

    private HttpURLConnection getHttpURLConnection(URL url) {
        HttpURLConnection http = null;
        try {

            http = (HttpURLConnection) url.openConnection();

        } catch (Exception e) {
            log.error("Exception occurred while creating url {} ", e);
        }
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        http.setConnectTimeout(10000);
        http.setReadTimeout(20000);
        return http;
    }

    public String getCurrentUrl() {
        return hook.getDriver().getCurrentUrl();
    }

    public void inputUsername(String name) {
        usernameInput.sendKeys(name);
    }

    public void inputPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void goToHomePage() {
        hook.goToBaseUrl();
    }

    public void pressLoginButton() {
        hook.getWebDriverWait(10).
                until(ExpectedConditions.elementToBeClickable(login));
        login.click();
    }

    public void pressHomePageLoginButton() {
        hook.getWebDriverWait(10)
                .until(ExpectedConditions.elementToBeClickable(userLogin));
        userLogin.click();
    }

    public void executeJavaScriptCommand(String command) {
        JavascriptExecutor js = null;
        if (hook.getDriver() instanceof JavascriptExecutor) {
            js = (JavascriptExecutor) hook.getDriver();
        }
        js.executeScript(command);
    }

    public void removeFancyBoxWithCommand() {
        String removeFancyBoxCommand = "return document.getElementsByClassName('fancybox-overlay fancybox-overlay-fixed')[0].remove();";
        hook.getWebDriverWait(10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("homepage-popup")));
        executeJavaScriptCommand(removeFancyBoxCommand);
    }

    public void removeModalWithCommand() {
        String removeFancyBoxCommand = "return document.getElementById('modal-root').remove();";
        hook.getWebDriverWait(10).
                until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-root")));
        executeJavaScriptCommand(removeFancyBoxCommand);
    }
}