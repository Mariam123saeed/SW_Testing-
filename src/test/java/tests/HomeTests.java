package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;

public class HomeTests {

    WebDriver driver;
    HomePage homePage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Open login page
        driver.get("https://www.saucedemo.com/");

        // Manual login (Login page automation is not part of task)
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Initialize HomePage after successful login
        homePage = new HomePage(driver);
    }

    // Add delay between tests so results can be seen clearly
    @AfterMethod
    public void delayBetweenTests() throws InterruptedException {
        Thread.sleep(2000); // wait 2 seconds between test cases
    }

    // -------------------------------
    // Test Case 1: Check product names
    // -------------------------------
    @Test
    public void checkAllProductNamesDisplayed() {
        Assert.assertFalse(
                homePage.getAllProductNames().isEmpty(),
                "No products found on the Home page!"
        );
    }

    // -------------------------------
    // Test Case 2: Add first item to cart
    // -------------------------------
    @Test
    public void addItemToCartTest() {
        homePage.addItemToCart(0);
        Assert.assertEquals(
                homePage.getCartCount(),
                "1",
                "Cart counter did not update correctly!"
        );
    }

    // -------------------------------
    // Test Case 3: Verify sorting Z → A
    // -------------------------------
    @Test
    public void testSortingZtoA() {
        Select select = new Select(homePage.getSortDropdown());
        select.selectByVisibleText("Name (Z to A)");

        Assert.assertEquals(
                select.getFirstSelectedOption().getText(),
                "Name (Z to A)",
                "Sorting dropdown value is incorrect!"
        );
    }

    // -------------------------------
    // Test Case 4: Check product prices exist
    // -------------------------------
    @Test
    public void checkPricesDisplayed() {
        Assert.assertFalse(
                homePage.getAllPrices().isEmpty(),
                "Prices are not displayed on the Home page!"
        );
    }

    // -------------------------------
    // Test Case 5: Verify product count is 6
    // -------------------------------
    @Test
    public void checkProductCount() {
        Assert.assertEquals(
                homePage.getAllProductNames().size(),
                6,
                "Expected 6 products on inventory page!"
        );
    }

    // -------------------------------
    // Test Case 6: Verify cart icon is displayed
    // -------------------------------
    @Test
    public void cartIconExists() {
        Assert.assertTrue(
                homePage.isCartIconDisplayed(),
                "Cart icon is NOT displayed on the page!"
        );
    }


    // -------------------------------
// Test Case 7: Check product images are displayed
// -------------------------------
    @Test
    public void checkProductImagesDisplayed() {
        Assert.assertFalse(
                homePage.getAllProductImages().isEmpty(),
                "Product images are not displayed on the Home page!"
        );
    }

    // -------------------------------
// Test Case 8: Add and remove item from cart
// -------------------------------
    @Test
    public void addAndRemoveItemFromCart() {
        // Add first item
        homePage.addItemToCart(0);
        Assert.assertEquals(homePage.getCartCount(), "1", "Cart counter did not update after adding!");

        // Remove the same item
        homePage.removeItemFromCart(0);
        Assert.assertEquals(homePage.getCartCount(), "0", "Cart counter did not update after removing!");
    }

    // -------------------------------
// Test Case 9: Verify sorting A → Z
// -------------------------------
    @Test
    public void testSortingAtoZ() {
        Select select = new Select(homePage.getSortDropdown());
        select.selectByVisibleText("Name (A to Z)");

        Assert.assertEquals(
                select.getFirstSelectedOption().getText(),
                "Name (A to Z)",
                "Sorting dropdown value is incorrect!"
        );
    }


    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
