package com.trendyol.selenium.steps;

import com.trendyol.selenium.pages.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class HomeSteps {

    private static final Logger log = LoggerFactory.getLogger(HomeSteps.class);

    @Autowired
    private HomePage homePage;

    @Given("User navigate to home page")
    public void userNavigateToHomePage() {
        homePage.goToHomePage();
    }

    @When("User Click to login button in the home page")
    public void userClickToLoginButtonInTheHomePage() {
        homePage.removeFancyBoxWithCommand();
        homePage.pressHomePageLoginButton();
    }

    @Then("User enters username {string} and password {string} to inputs")
    public void userEntersUsernameAndPasswordToInputs(String username, String password) {
        homePage.inputUsername(username);
        homePage.inputPassword(password);
    }

    @When("User press the login button")
    public void userPressTheLoginButton() {
        homePage.pressLoginButton();
    }

    @Then("User logged in successfully")
    public void userLoggedInSuccessfully() {
        homePage.removeModalWithCommand();
        String loginResult = homePage.getIconText();
        Assert.assertEquals("Hesabım", loginResult);

    }

    @When("user click {string} section")
    public void userClickSection(String sectionName) throws InterruptedException {
        homePage.clickBoutiqueButtonWithName(sectionName);
        homePage.waitForLoad();
    }

    @Then("user goto url contains {string} successfully")
    public void userGotoUrlContainsSuccessfully(String urlPostfix) {
        String currentUrl = homePage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(urlPostfix));
    }

    @And("check broken images on boutique")
    public void checkBrokenImagesOnBoutique() {
        List<String> brokenLinks = homePage.checkImages();
        if (brokenLinks.size() > 0)
            log.error("Broken images found {}", brokenLinks);
    }


    @Given("User click to product boutique")
    public void userClickToProductBoutique() {
        homePage.selectRandomShop();
        homePage.waitForLoad();
    }

    @Then("User click a product")
    public void userClickAProduct() {
        homePage.selectRandomProduct();
        homePage.waitForLoad();
    }

    @When("Add product to basket")
    public void addProductToBasket() {
        homePage.addToBasket();
    }

    @Then("Product added to basket successfully")
    public void productAddedToBasketSuccessfully() {
        String url = homePage.getCurrentUrl();
        homePage.gotoBasket();
        homePage.waitForLoad();
        Assert.assertEquals(true, homePage.isAddedProduct(url));
    }
}
