package integration_Testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Test(dataProvider = "loginData")
public class SideBarTest {

    WebDriver driver;
    WebDriverWait wait;
    ChromeOptions options = new ChromeOptions();

    @BeforeTest
    public void beforeTest() {

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("profile.credentials_enable_autosignin", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordManagerEnabled");
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--disable-features=SafeBrowsingSecurityToken");
        options.addArguments("--disable-features=SafetyTipUI");

    }
    @BeforeMethod
    public void beforeMethod(){
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

    }

    // -----------------------------
    // DATA PROVIDER
    // -----------------------------
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
        };
    }

    // -----------------------------
    // LOGIN METHOD
    // -----------------------------
    public void login(String username, String password) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();

        // ASSERT: Login successful
        Assert.assertEquals(
                driver.getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "Login failed for user: " + username
        );
    }

    // -----------------------------
    // SIDEBAR METHODS
    // -----------------------------
    public void openMenu() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("bm-menu")));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu")));

        Assert.assertTrue(driver.findElement(By.className("bm-menu")).isDisplayed(),
                "Menu did NOT open!");
    }

    public void closeMenu() {
        // Wait until menu is visible before clicking close
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu")));

        // Click close button (use JS click if normal click sometimes fails)
        WebElement closeBtn = driver.findElement(By.className("bm-cross-button"));
        closeBtn.click();

        // Wait until menu is no longer visible
        wait.until(ExpectedConditions.invisibilityOf(menu));

        // Assert menu is closed
//        Assert.assertFalse(isElementDisplayed(By.className("bm-menu")), "Menu did NOT close!");
    }


    // -----------------------------
    // TEST CASES
    // -----------------------------

    @Test(priority = 1)
    public void testSideBarList(String username, String password) {
        login(username, password);
        openMenu();
        closeMenu();
    }

    @Test(priority = 2)
    public void testAllItemsLink(String username, String password) {
        login(username, password);
        openMenu();

        driver.findElement(By.id("inventory_sidebar_link")).click();

        Assert.assertEquals(driver.getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "All Items link did NOT navigate correctly!");
    }

    @Test(priority = 3)
    public void testResetLink(String username, String password) {
        login(username, password);
        openMenu();

        driver.findElement(By.id("reset_sidebar_link")).click();

        int cartCount = driver.findElements(By.className("shopping_cart_badge")).size();
        Assert.assertEquals(cartCount, 0, "Reset did NOT clear cart or state!");
    }

    @Test(priority = 4)
    public void testAboutLink(String username, String password) {
        login(username, password);
        openMenu();
        driver.findElement(By.id("about_sidebar_link")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"),
                "About link did NOT open Sauce Labs site!");
    }



    @Test(priority = 5)
    public void testLogOutLink(String username, String password) {
        login(username, password);
        openMenu();

        driver.findElement(By.id("logout_sidebar_link")).click();

        Assert.assertEquals(driver.getCurrentUrl(),
                "https://www.saucedemo.com/",
                "Logout did NOT navigate back to login page!");
    }

    @AfterMethod
    public void closeBrowser() {


        driver.quit();

    }
}
