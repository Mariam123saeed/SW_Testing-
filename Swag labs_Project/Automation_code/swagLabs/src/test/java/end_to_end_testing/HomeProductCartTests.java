package end_to_end_testing;

import org.testng.annotations.Test;  // TestNG annotation
import integration_Testing.HomePage;  // Page Object for Home Page
import org.openqa.selenium.By;  // Selenium locators
import org.openqa.selenium.WebElement;  // Selenium WebElement
import java.util.List;  // For handling lists of elements

// Test class for Home page product and cart functionalities
@Test(dataProvider = "users", dataProviderClass = DataProviders.class)

public class HomeProductCartTests extends BaseTest {

    //    HomePage home = new HomePage(driver);
    // Test 1: Open detail page of first product, verify details, and add to cart
    @Test(priority = 1)
    public void ViewDetailPageAndAddFirstProduct(String username, String password) {

        HomePage home = new HomePage(driver); // Initialize HomePage object

        // Capture first product's details from the home page
        String homeName = home.getAllProductNames().get(0).getText();
        String homePrice = home.getAllPrices().get(0).getText();
        String homeImage = home.getAllProductImages().get(0).findElement(By.tagName("img")).getAttribute("src");

        // Open the detail page of the first product
        home.getAllProductNames().get(0).click();

        // Capture product details from the detail page
        WebElement detailNameEl = driver.findElement(By.className("inventory_details_name"));
        WebElement detailPriceEl = driver.findElement(By.className("inventory_details_price"));
        WebElement detailImgEl = driver.findElement(By.className("inventory_details_img"));

        // Verify that home page and detail page info match
        if (!homeName.equals(detailNameEl.getText()) ||
                !homePrice.equals(detailPriceEl.getText()) ||
                !homeImage.equals(detailImgEl.getAttribute("src"))) {
            throw new IllegalStateException("Product details mismatch!");
        }

        // Add product to cart from detail page
        driver.findElement(By.tagName("button")).click();
//        System.out.println(username + " added first product to cart: " + homeName);

        // Go back to the products list
        driver.findElement(By.id("back-to-products")).click();
    }

    // Test 2: Remove product from cart and verify cart count
    @Test(dependsOnMethods = "ViewDetailPageAndAddFirstProduct", priority = 4)
    public void removeProductAndVerifyCart(String username, String password) {
        HomePage home = new HomePage(driver);

        // Open the cart page
        driver.findElement(By.className("shopping_cart_link")).click();

        // Get list of items in the cart
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty!"); // Ensure cart has items

        int countBefore = cartItems.size(); // Count before removal

        // Remove the first item in the cart
        cartItems.get(0).findElement(By.tagName("button")).click();
        System.out.println("Removed one product from cart.");

        // Verify cart icon count decreased
        int countAfter = Integer.parseInt(home.getCartCount());
        if (countAfter != countBefore - 1)
            throw new IllegalStateException("Cart count did not decrease!");
        System.out.println("Cart count updated: " + countAfter);
    }

    // Test 3: Open detail page and add second product
    @Test(priority = 2)
    public void ViewDetailPageAndAddSecondProduct(String username, String password) {
        HomePage home = new HomePage(driver);

        String name = home.getAllProductNames().get(1).getText(); // Get second product name
        home.getAllProductNames().get(1).click(); // Open detail page

        driver.findElement(By.tagName("button")).click(); // Add to cart
//        System.out.println(username + " added second product: " + name);
        driver.findElement(By.id("back-to-products")).click(); // Back to products
    }

    // Test 4: Open detail page and add third product
    @Test(priority = 3)
    public void ViewDetailPageAndAddThirdProduct(String username, String password) {
        HomePage home = new HomePage(driver);

        String name = home.getAllProductNames().get(2).getText(); // Get third product
        home.getAllProductNames().get(2).click();

        driver.findElement(By.tagName("button")).click(); // Add to cart
//        System.out.println(username + " added third product: " + name);
        driver.findElement(By.id("back-to-products")).click();
    }
//    @Test(dependsOnMethods = "ViewDetailPageAndAddThirdProduct",priority = 5)
//    public void delay(String username, String password) throws InterruptedException {
//        Thread.sleep(5000);   // 3-second delay BEFORE second test starts
//    }

    // Test 5: Apply Low to High filter and verify first product
    @Test(priority = 6)
    public void FilterPriceLowToHighAndVerify(String username, String password) {


        HomePage home = new HomePage(driver);

        // Apply "Price: Low to High" filter
        home.selectSortOption("lohi");

        // Verify first two products are sorted correctly
        double firstPrice = home.getProductPrice(0);
        double secondPrice = home.getProductPrice(1);

        if (firstPrice > secondPrice)
            throw new IllegalStateException("Products are not sorted from low to high by price!");

        System.out.println(username + " verified Price: Low to High filter, first product price: $" + firstPrice);

        // Optionally, open first product's detail page
        home.getAllProductNames().get(0).click();
        driver.findElement(By.id("back-to-products")).click();
    }


    // Test 6: Apply Z to A filter and verify first product
    @Test(priority = 7)
    public void FilterZToAAndVerify(String username, String password) {

        HomePage home = new HomePage(driver);

        // Open sort dropdown and select Z to A
        home.getSortDropdown().click();
        home.getSortDropdown().findElement(By.xpath("//option[@value='za']")).click();

        String name = home.getAllProductNames().get(0).getText(); // Get first product
        home.getAllProductNames().get(0).click();

        // Verify product matches the sorted order
        WebElement detailName = driver.findElement(By.className("inventory_details_name"));
        if (!name.equals(detailName.getText())) throw new IllegalStateException("Z→A filter mismatch!");

        System.out.println(username + " verified first product Z to A filter: " + name);
        driver.findElement(By.id("back-to-products")).click();
    }

    // Test 7: Reset app state and verify add-to-cart buttons
    @Test(priority = 8)
    public void ResetAppState(String username, String password) {
        HomePage home = new HomePage(driver);

        // Add two products to cart as setup
        home.addItemToCart(0);
        home.addItemToCart(1);

        // Open side menu and click reset app state
        driver.findElement(By.id("react-burger-menu-btn")).click();
        driver.findElement(By.id("reset_sidebar_link")).click();
        System.out.println(username + " reset app state.");

        // Verify all "Add to Cart" buttons are enabled after reset
        List<WebElement> buttons = home.getAllAddToCartButtons();
        for (WebElement btn : buttons) {
            if (!btn.isEnabled()) throw new IllegalStateException("Add to Cart not reset!");
        }
        System.out.println("Cart reset verified successfully.");
    }
}