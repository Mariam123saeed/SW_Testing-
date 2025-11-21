package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage {

    WebDriver driver;

    // Constructor: initialize driver
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================
    // Locators
    // ============================

    By productName = By.className("inventory_item_name");
    By productPrice = By.className("inventory_item_price");
    By addToCartBtns = By.xpath("//button[contains(text(), 'Add to cart')]");
    By removeFromCartBtns = By.xpath("//button[contains(text(), 'Remove')]");
    By productImages = By.className("inventory_item_img");
    By cartBadge = By.className("shopping_cart_badge");
    By sortDropdown = By.className("product_sort_container");
    By cartIcon = By.className("shopping_cart_link");

    // ============================
    // Methods (Page Functions)
    // ============================

    public List<WebElement> getAllProductNames() {
        return driver.findElements(productName);
    }

    public List<WebElement> getAllPrices() {
        return driver.findElements(productPrice);
    }

    public void addItemToCart(int index) {
        driver.findElements(addToCartBtns).get(index).click();
    }

    public void removeItemFromCart(int index) {
        driver.findElements(removeFromCartBtns).get(index).click();
    }

    public List<WebElement> getAllProductImages() {
        return driver.findElements(productImages);
    }

    public String getCartCount() {
        // Check if cart badge exists to avoid exception when empty
        List<WebElement> badge = driver.findElements(cartBadge);
        return badge.isEmpty() ? "0" : badge.get(0).getText();
    }

    public WebElement getSortDropdown() {
        return driver.findElement(sortDropdown);
    }

    public boolean isCartIconDisplayed() {
        return driver.findElement(cartIcon).isDisplayed();
    }
}
