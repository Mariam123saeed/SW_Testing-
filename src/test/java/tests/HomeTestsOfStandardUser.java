package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;

import java.util.List;

public class HomeTestsOfStandardUser {

    WebDriver driver;
    HomePage homePage;

    @BeforeTest
    public void loginAsStandardUser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");

        // Login as standard_user
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        homePage = new HomePage(driver);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @AfterMethod
    public void delay() throws InterruptedException {
        Thread.sleep(2000);
    }

    // -------------------------------
    // Test Case 1: Verify all product images are displayed
    // -------------------------------
    @Test
    public void verifyAllProductImagesAreDisplayed() {
        List<WebElement> images = homePage.getAllProductImages();
        Assert.assertFalse(images.isEmpty(), "No product images found on Home page!");
        for (WebElement img : images) {
            Assert.assertTrue(img.isDisplayed(), "Product image is not displayed!");
        }
    }

    // -------------------------------
    // Test Case 2: Verify image matches product name
    // -------------------------------
    @Test
    public void verifyImageMatchesProductName() {
        List<WebElement> productNames = homePage.getAllProductNames();
        List<WebElement> productImages = homePage.getAllProductImages();

        for (int i = 0; i < productNames.size(); i++) {
            String name = productNames.get(i).getText().toLowerCase();
            String alt = productImages.get(i).getAttribute("alt").toLowerCase();
            Assert.assertTrue(alt.contains(name), "Image alt does not match product name!");

        }
    }

    // -------------------------------
    // Test Case 3: Add multiple products to cart
    // -------------------------------
    @Test
    public void addMultipleItemsToCart() {
        int itemsToAdd = 3;
        for (int i = 0; i < itemsToAdd; i++) {
            homePage.addItemToCart(i);
        }
        Assert.assertEquals(homePage.getCartCount(), String.valueOf(itemsToAdd), "Cart counter is incorrect!");
    }

    // -------------------------------
    // Test Case 4: Verify cart icon displayed
    // -------------------------------
    @Test
    public void cartIconExists() {
        Assert.assertTrue(homePage.isCartIconDisplayed(), "Cart icon is not displayed!");
    }

    // -------------------------------
    // Test Case 5: Verify sorting Z → A
    // -------------------------------
    @Test
    public void testSortingZtoA() {
        Select select = new Select(homePage.getSortDropdown());
        select.selectByVisibleText("Name (Z to A)");
        Assert.assertEquals(select.getFirstSelectedOption().getText(), "Name (Z to A)", "Sorting dropdown incorrect!");
    }

}
