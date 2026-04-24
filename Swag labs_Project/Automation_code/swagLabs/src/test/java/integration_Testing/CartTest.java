package integration_Testing;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartTest {

    WebDriver driver;
    WebDriverWait period;



    @BeforeMethod
    public void beforeTest() throws InterruptedException {

        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("credentials_enable_service", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);

        period = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");

        Thread.sleep(1500);

        login("standard_user","secret_sauce");

        period.until(ExpectedConditions.urlContains("inventory.html"));
    }

    public void login(String username , String password){
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();
    }

    public void logout() {
        driver.findElement(By.id("react-burger-menu-btn")).click();

        WebElement logoutBtn = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

        logoutBtn.click();

        period.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    public void openCart() {
        WebElement cartIcon = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        cartIcon.click();

        period.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
    }

    public void goToInventory(){
        driver.findElement(By.id("continue-shopping")).click();
        period.until(ExpectedConditions.urlContains("inventory"));
    }

    public void addProduct(String productId) {
        WebElement element = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(productId)));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        period.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void removeProduct(String removeId) {
        WebElement element = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(removeId)));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        period.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public int getCartCount(){
        List<WebElement> badge = driver.findElements(By.className("shopping_cart_badge"));
        if (badge.size() == 0) { return 0;}

        return Integer.parseInt(badge.get(0).getText());
    }
    @Test(priority = 1)
    public void TC1_AddSingleProductToCart() {
        addProduct("add-to-cart-sauce-labs-backpack");
        openCart();
    }

    @Test(priority = 2)
    public void TC2_AddMultipleDifferentProducts() {
        addProduct("add-to-cart-sauce-labs-backpack");
        addProduct("add-to-cart-sauce-labs-bike-light");
        openCart();
    }

    @Test(priority = 3)
    public void TC3_RemoveProductFromCart() {
        addProduct("add-to-cart-sauce-labs-backpack");
        addProduct("add-to-cart-sauce-labs-bike-light");
        openCart();
        removeProduct("remove-sauce-labs-backpack");
    }

    @Test(priority = 4)
    public void TC4_VerifyButtonChangeAfterAdd() {
        addProduct("add-to-cart-sauce-labs-backpack");

        WebElement removeBtn = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("remove-sauce-labs-backpack")));

        System.out.println("Button changed to: " + removeBtn.getText());
        openCart();
    }

    @Test(priority = 5)
    public void TC5_VerifyTotalPriceCalculation() {
        addProduct("add-to-cart-sauce-labs-backpack");
        addProduct("add-to-cart-sauce-labs-bike-light");

        openCart();

        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Malak");
        driver.findElement(By.id("last-name")).sendKeys("Tester");
        driver.findElement(By.id("postal-code")).sendKeys("12345");

        driver.findElement(By.id("continue")).click();

        WebElement total = period.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("summary_total_label")));

        System.out.println("Total Price: " + total.getText());
    }
    @Test(priority = 6)
    public void TC6_VerifyCartIconUpdatesCount() {

        addProduct("add-to-cart-sauce-labs-backpack");
        Assert.assertEquals(getCartCount(),1);

        addProduct("add-to-cart-sauce-labs-bike-light");
        Assert.assertEquals(getCartCount(),2);

        removeProduct("remove-sauce-labs-backpack");
        Assert.assertEquals(getCartCount(),1);
    }
    @Test(priority = 7)
    public void TC7_CheckCartAfterLogoutAndLogin() {

        addProduct("add-to-cart-sauce-labs-backpack");
        Assert.assertEquals(getCartCount(),1);

        logout();

        login("standard_user","secret_sauce");

        Assert.assertEquals(getCartCount(),1);
    }
    @Test(priority = 8)
    public void TC8_CheckCartPersistenceAcrossPages() {

        addProduct("add-to-cart-sauce-labs-backpack");
        addProduct("add-to-cart-sauce-labs-bike-light");

        Assert.assertEquals(getCartCount(),2);

        logout();
        login("standard_user","secret_sauce");

        Assert.assertEquals(getCartCount(),2);
    }
    @Test(priority = 9)
    public void TC9_AddSameProductMultipleTimes() {

        addProduct("add-to-cart-sauce-labs-backpack");

        WebElement btn = driver.findElement(By.id("remove-sauce-labs-backpack"));
        Assert.assertTrue(btn.isDisplayed());
    }
    @Test(priority = 10)
    public void TC10_RemoveAllItems() {

        addProduct("add-to-cart-sauce-labs-backpack");
        addProduct("add-to-cart-sauce-labs-bike-light");

        removeProduct("remove-sauce-labs-backpack");
        removeProduct("remove-sauce-labs-bike-light");

        Assert.assertEquals(getCartCount(),0);
    }
    @Test(priority = 11)
    public void TC11_AddProductThenCheckout() {

        addProduct("add-to-cart-sauce-labs-backpack");

        openCart();

        driver.findElement(By.id("checkout")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"));
    }
    @Test(priority = 12)
    public void TC12_AddProductAndNavigateBackToInventory() {

        addProduct("add-to-cart-sauce-labs-backpack");
        openCart();

        goToInventory();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test(priority = 13)
    public void TC13_VerifyCartUIElements() {

        openCart();

        Assert.assertTrue(driver.findElement(By.className("cart_list")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("continue-shopping")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("checkout")).isDisplayed());
    }

    @Test(priority = 14)
    public void TC14_VerifyEmptyCartMessage() {

        openCart();

        List<WebElement> items = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(items.size(),0);
    }
    @Test(priority = 15)
    public void TC15_AddMaximumQuantity() {

        List<WebElement> addButtons = driver.findElements(By.xpath("//button[contains(text(),'Add to cart')]"));

        for(WebElement btn : addButtons){
            btn.click();
        }

        Assert.assertTrue(getCartCount() > 0);
    }

    @AfterMethod
    public void afterTest() {
        if (driver != null) {
            driver.quit();
        }
    }
}