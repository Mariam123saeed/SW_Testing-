package end_to_end_testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected static WebDriver driver;  // SHARED INSTANCE
    WebDriverWait wait;


    @BeforeSuite
    public void setupDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
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

            driver = new ChromeDriver(options);
        }
    }
    @BeforeClass
    public void beforeClass(){
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterSuite
    public void quitDriver() {
        // Quit only after all E2E classes are done
        driver.quit();

    }
}
