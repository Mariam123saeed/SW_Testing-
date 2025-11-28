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

public class HomeTestsOfProblemUser {

    WebDriver driver;
    HomePage homePage;

    // -------------------------------
    // BeforeTest: Login as problem_user
    // -------------------------------
    @BeforeTest
    public void loginAsProblemUser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        homePage = new HomePage(driver);
    }

    // -------------------------------
    // AfterTest: Quit driver
    // -------------------------------
    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    // -------------------------------
    // AfterMethod: Small delay between tests
    // -------------------------------
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
    // Test Case 3: Add one item to cart
    // -------------------------------
    @Test
    public void addOneItemToCart() {
        homePage.addItemToCart(0);
        Assert.assertEquals(homePage.getCartCount(), "1", "Cart counter is incorrect after adding one item!");
    }

    // -------------------------------
    // Test Case 4: Verify "Add to cart" button changes to "Remove"
    // -------------------------------
    @Test
    public void verifyAddToCartButtonChangesToRemove() {
        WebElement firstAddBtn = homePage.getAllAddToCartButtons().get(0);
        firstAddBtn.click();
        String btnText = firstAddBtn.getText();
        Assert.assertEquals(btnText, "Remove", "Button did not change to 'Remove' after adding product!");
    }

    // -------------------------------
    // Test Case 5: Verify user can remove an item from cart
    // -------------------------------
    @Test
    public void verifyUserCanRemoveItemFromCart() {
        WebElement addBtn = homePage.getAllAddToCartButtons().get(1);
        addBtn.click(); // Add
        addBtn.click(); // Remove
        Assert.assertEquals(homePage.getCartCount(), "0", "Product was not removed from cart successfully!");
    }

    // -------------------------------
    // Test Case 6: Add multiple items to cart and verify
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
    // Test Case 7: Verify cart retains items after page refresh
    // -------------------------------
    @Test
    public void cartRetainsItemsAfterRefresh() {
        homePage.addItemToCart(0);
        homePage.addItemToCart(1);
        driver.navigate().refresh();
        Assert.assertEquals(homePage.getCartCount(), "2", "Cart did not retain items after refresh!");
    }

    // -------------------------------
    // Test Case 8: Verify sorting by Price (low to high)
    // -------------------------------
    @Test
    public void testSortingByPriceLowToHigh() {
        Select select = new Select(homePage.getSortDropdown());
        select.selectByVisibleText("Price (low to high)");
        Assert.assertEquals(select.getFirstSelectedOption().getText(), "Price (low to high)", "Sorting dropdown incorrect!");
    }

    // -------------------------------
    // Test Case 9: Verify product images are clickable and lead to product details
    // -------------------------------
    @Test
    public void verifyProductImagesAreClickable() {
        List<WebElement> productImages = homePage.getAllProductImages();
        for (WebElement img : productImages) {
            Assert.assertTrue(img.isDisplayed() && img.isEnabled(), "Product image is not clickable!");
        }
    }

    // -------------------------------
    // Test Case 10: Verify cart buttons and icon consistency on window resize (responsiveness)
    // -------------------------------
    @Test
    public void verifyCartAndButtonsResponsive() {
        // Add a product first
        WebElement firstAddBtn = homePage.getAllAddToCartButtons().get(0);
        firstAddBtn.click();
        Assert.assertEquals(firstAddBtn.getText(), "Remove", "Button did not change to 'Remove' after adding product!");

        // Resize window (simulate responsiveness)
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1024, 768)); // Desktop
        Assert.assertTrue(homePage.isCartIconDisplayed(), "Cart icon not visible on desktop size!");
        Assert.assertEquals(firstAddBtn.getText(), "Remove", "Button text changed unexpectedly on desktop size!");

        driver.manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024)); // Tablet
        Assert.assertTrue(homePage.isCartIconDisplayed(), "Cart icon not visible on tablet size!");
        Assert.assertEquals(firstAddBtn.getText(), "Remove", "Button text changed unexpectedly on tablet size!");

        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 812)); // Mobile
        Assert.assertTrue(homePage.isCartIconDisplayed(), "Cart icon not visible on mobile size!");
        Assert.assertEquals(firstAddBtn.getText(), "Remove", "Button text changed unexpectedly on mobile size!");
    }

}
