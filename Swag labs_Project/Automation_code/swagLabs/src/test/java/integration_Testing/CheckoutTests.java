package integration_Testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class CheckoutTests {

    WebDriver driver;

    @BeforeTest
    public void setUp() throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("credentials_enable_service", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.navigate().to("https://www.saucedemo.com");
       
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(500);
    

       driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(500);
        driver.findElement(By.id("checkout")).click();
        Thread.sleep(500);
    }

    @Test
    public void testCheckout1() throws InterruptedException {
        driver.findElement(By.id("first-name")).clear();
        driver.findElement(By.id("first-name")).sendKeys("yyuyu");

        driver.findElement(By.id("last-name")).clear();
        driver.findElement(By.id("last-name")).sendKeys("ukh");

        driver.findElement(By.id("postal-code")).clear();
        driver.findElement(By.id("postal-code")).sendKeys("n86876");

        Thread.sleep(500);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(500);
    }

    @Test
    public void testCheckout2() throws InterruptedException {
        driver.findElement(By.id("first-name")).clear();
        driver.findElement(By.id("first-name")).sendKeys("3434");

        driver.findElement(By.id("last-name")).clear();
        driver.findElement(By.id("last-name")).sendKeys("57665");

        driver.findElement(By.id("postal-code")).clear();
        driver.findElement(By.id("postal-code")).sendKeys("768767");

        Thread.sleep(500);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(500);
    }
     @Test
    public void testCheckout3() throws InterruptedException {
        driver.findElement(By.id("first-name")).clear();
        driver.findElement(By.id("first-name")).sendKeys("     ");

        driver.findElement(By.id("last-name")).clear();
        driver.findElement(By.id("last-name")).sendKeys("      ");

        driver.findElement(By.id("postal-code")).clear();
        driver.findElement(By.id("postal-code")).sendKeys("     ");

        Thread.sleep(500);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(500);
    }
  @Test
    public void testCheckout4() throws InterruptedException {
       
        Thread.sleep(500);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(500);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
