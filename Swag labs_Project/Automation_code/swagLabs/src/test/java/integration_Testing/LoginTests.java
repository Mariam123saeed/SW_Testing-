package integration_Testing;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests {
    WebDriver d;

    @BeforeMethod
    public void beforetest() {
        d = new ChromeDriver();
        d.navigate().to("https://www.saucedemo.com/");
        d.manage().window().maximize();
    }

    // TC1: Valid Login - Standard User
    @Test(priority = 1)
    public void validLoginStandardUser() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed - not redirected to inventory page");
    }

    // TC2: Invalid Username with Valid Password
    @Test(priority = 2)
    public void invalidUsername() {
        d.findElement(By.id("user-name")).sendKeys("invalid_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Wrong error message");
    }

    // TC3: Valid Username with Invalid Password
    @Test(priority = 3)
    public void invalidPassword() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("wrong_password");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Username and password do not match"),
                "Wrong error message");
    }

    // TC4: Empty Username and Password
    @Test(priority = 4)
    public void emptyUsernameAndPassword() {
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Username is required"),
                "Wrong error message");
    }

    // TC5: Empty Username with Valid Password
    @Test(priority = 5)
    public void emptyUsername() {
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Username is required"),
                "Wrong error message");
    }

    // TC6: Valid Username with Empty Password
    @Test(priority = 6)
    public void emptyPassword() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Password is required"),
                "Wrong error message");
    }

    // TC7: Locked Out User
    @Test(priority = 7)
    public void lockedOutUser() {
        d.findElement(By.id("user-name")).sendKeys("locked_out_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
        Assert.assertTrue(errorMsg.getText().contains("Sorry, this user has been locked out"),
                "Wrong error message");
    }

    // TC8: Problem User Login
    @Test(priority = 8)
    public void problemUserLogin() {
        d.findElement(By.id("user-name")).sendKeys("problem_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed - not redirected to inventory page");
    }

    // TC9: Performance Glitch User
    @Test(priority = 9)
    public void performanceGlitchUser() {
        d.findElement(By.id("user-name")).sendKeys("performance_glitch_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed - not redirected to inventory page");
    }

    // TC10: Close Error Message Button
    @Test(priority = 10)
    public void closeErrorMessage() {
        d.findElement(By.id("user-name")).sendKeys("invalid_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");

        d.findElement(By.className("error-button")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(d.findElements(By.cssSelector("[data-test='error']")).size() > 0
                        && d.findElement(By.cssSelector("[data-test='error']")).isDisplayed(),
                "Error message still displayed");
    }

    // TC11: Login with Enter Key on Username Field
    @Test(priority = 11)
    public void loginWithEnterOnUsername() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("user-name")).sendKeys(Keys.ENTER);

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed with Enter key");
    }

    // TC12: Login with Enter Key on Password Field
    @Test(priority = 12)
    public void loginWithEnterOnPassword() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce" + Keys.ENTER);

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed with Enter key on password");
    }

    // TC13: Username with Leading Spaces
    @Test(priority = 13)
    public void usernameWithLeadingSpaces() {
        d.findElement(By.id("user-name")).sendKeys("  standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC14: Username with Trailing Spaces
    @Test(priority = 14)
    public void usernameWithTrailingSpaces() {
        d.findElement(By.id("user-name")).sendKeys("standard_user  ");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC15: Password with Leading Spaces
    @Test(priority = 15)
    public void passwordWithLeadingSpaces() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("  secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC16: Case Sensitive Username
    @Test(priority = 16)
    public void caseSensitiveUsername() {
        d.findElement(By.id("user-name")).sendKeys("STANDARD_USER");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC17: Case Sensitive Password
    @Test(priority = 17)
    public void caseSensitivePassword() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("SECRET_SAUCE");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC18: Special Characters in Username
    @Test(priority = 18)
    public void specialCharactersUsername() {
        d.findElement(By.id("user-name")).sendKeys("standard@user#123");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC19: SQL Injection Attempt in Username
    @Test(priority = 19)
    public void sqlInjectionUsername() {
        d.findElement(By.id("user-name")).sendKeys("' OR '1'='1");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "SQL injection not prevented");
    }

    // TC20: Very Long Username
    @Test(priority = 20)
    public void veryLongUsername() {
        String longUsername = "a".repeat(100);
        d.findElement(By.id("user-name")).sendKeys(longUsername);
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");
    }

    // TC21: Verify Login Button is Clickable
    @Test(priority = 21)
    public void verifyLoginButtonClickable() {
        WebElement loginButton = d.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isEnabled(), "Login button is not clickable");
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed");
    }

    // TC22: Verify Username Field Properties
    @Test(priority = 22)
    public void verifyUsernameFieldProperties() {
        WebElement usernameField = d.findElement(By.id("user-name"));
        Assert.assertTrue(usernameField.isEnabled(), "Username field is not enabled");
        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");
        Assert.assertEquals(usernameField.getAttribute("placeholder"), "Username",
                "Wrong placeholder text");
    }

    // TC23: Verify Password Field Properties
    @Test(priority = 23)
    public void verifyPasswordFieldProperties() {
        WebElement passwordField = d.findElement(By.id("password"));
        Assert.assertTrue(passwordField.isEnabled(), "Password field is not enabled");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");
        Assert.assertEquals(passwordField.getAttribute("type"), "password",
                "Password field type is not password");
    }

    // TC24: Verify Page Title
    @Test(priority = 24)
    public void verifyPageTitle() {
        String pageTitle = d.getTitle();
        Assert.assertEquals(pageTitle, "Swag Labs", "Wrong page title");
    }

    // TC25: Multiple Login Attempts with Wrong Credentials
    @Test(priority = 25)
    public void multipleFailedLoginAttempts() {
        for(int i = 1; i <= 3; i++) {
            d.findElement(By.id("user-name")).clear();
            d.findElement(By.id("password")).clear();

            d.findElement(By.id("user-name")).sendKeys("wrong_user" + i);
            d.findElement(By.id("password")).sendKeys("wrong_pass" + i);
            d.findElement(By.id("login-button")).click();

            WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
            Assert.assertTrue(errorMsg.isDisplayed(),
                    "Error message not displayed on attempt " + i);

            d.findElement(By.className("error-button")).click();
        }
    }

    // TC26: Clear Fields and Login
    @Test(priority = 26)
    public void clearFieldsAndLogin() {
        d.findElement(By.id("user-name")).sendKeys("wrong_user");
        d.findElement(By.id("password")).sendKeys("wrong_pass");

        d.findElement(By.id("user-name")).clear();
        d.findElement(By.id("password")).clear();

        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Login failed after clearing fields");
    }

    // TC27: Navigate Back After Login
    @Test(priority = 27)
    public void navigateBackAfterLogin() {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        Assert.assertTrue(d.getCurrentUrl().contains("inventory.html"),
                "Login failed");

        d.navigate().back();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "User was logged out after back navigation");
    }

    // TC28: Refresh Page After Failed Login
    @Test(priority = 28)
    public void refreshAfterFailedLogin() {
        d.findElement(By.id("user-name")).sendKeys("wrong_user");
        d.findElement(By.id("password")).sendKeys("wrong_pass");
        d.findElement(By.id("login-button")).click();

        WebElement errorMsg = d.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message not displayed");

        d.navigate().refresh();

        Assert.assertFalse(d.findElements(By.cssSelector("[data-test='error']")).size() > 0,
                "Error message still displayed after refresh");
    }

    // TC29: Visual User Login
    @Test(priority = 29)
    public void visualUserLogin() {
        d.findElement(By.id("user-name")).sendKeys("visual_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Visual user login failed");
    }

    // TC30: Error User Login
    @Test(priority = 30)
    public void errorUserLogin() {
        d.findElement(By.id("user-name")).sendKeys("error_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();

        String currentUrl = d.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "Error user login failed");
    }

    @AfterMethod
    public void aftertest() {
        if (d != null) {
            d.quit();
        }
    }
}