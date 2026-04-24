package end_to_end_testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dataProvider = "users",dataProviderClass = DataProviders.class)
public class LoginHomeTest extends BaseTest {

    // -----------------------------
    // SIDEBAR METHODS
    // -----------------------------
    public void openSideBar() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("bm-menu")));
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-menu")));
        Assert.assertTrue(driver.findElement(By.className("bm-menu")).isDisplayed(), "Menu did NOT open!");
    }

    public void closeSideBar() {
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

    @Test(priority = 1)
    public void login(String username, String password) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();

        // ASSERT: Login successful
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html", "Login failed for user: " + username);
    }

    @Test(priority = 2)
    public void testSideBarList(String username, String password) {
        login(username, password);
        openSideBar();
        closeSideBar();
    }

    @Test(priority = 3)
    public void testAllItemsLink(String username, String password) {
        login(username, password);
        openSideBar();

        driver.findElement(By.id("inventory_sidebar_link")).click();

        Assert.assertEquals(driver.getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "All Items link did NOT navigate correctly!");
    }

    @Test(priority = 4)
    public void testResetLink(String username, String password) {

        login(username, password);
        openSideBar();

        driver.findElement(By.id("reset_sidebar_link")).click();

        int cartCount = driver.findElements(By.className("shopping_cart_badge")).size();
        Assert.assertEquals(cartCount, 0, "Reset did NOT clear cart or state!");
    }

    @Test(priority = 5)
    public void testAboutLink(String username, String password) {
        login(username, password);
        openSideBar();

        driver.findElement(By.id("about_sidebar_link")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucelabs.com"),
                "About link did NOT open Sauce Labs site!");
        driver.navigate().to("https://www.saucedemo.com/inventory.html");
    }
    @Test(dependsOnMethods = "testAboutLink")
    public void delay(String username, String password) throws InterruptedException {
        Thread.sleep(5000);   // 3-second delay BEFORE second test starts
    }
}