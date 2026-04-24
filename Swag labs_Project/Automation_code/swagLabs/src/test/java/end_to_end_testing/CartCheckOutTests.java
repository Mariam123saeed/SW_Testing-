package end_to_end_testing;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartCheckOutTests extends BaseTest {

    // ---------------- Test 1: Valid Checkout ----------------
    @Test
    public void checkoutFlowValidUser() {
        openCart();
        clickCheckout();
        fillUserInformation("Zeyad", "Lotfy", "12345");
        confirmOrder();
        checkOut();
        validateCompletePage();
    }

    // ---------------- Test 2: Missing Last Name ----------------
    @Test
    public void checkoutFlowMissingLastName() {
        openCart();
        clickCheckout();
        fillUserInformation("Zeyad", "", "12345"); // Last name فارغ
        driver.findElement(By.id("continue")).click();

        String errorMsg = driver.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: Last Name is required", "Error message is WRONG!");
    }

    // ---------------- Test 3: Missing Postal Code ----------------
    @Test
    public void checkoutFlowMissingPostalCode() {
        openCart();
        clickCheckout();
        fillUserInformation("Zeyad", "Lotfy", ""); // Postal code فارغ
        driver.findElement(By.id("continue")).click();

        String errorMsg = driver.findElement(By.className("error-message-container")).getText();
        Assert.assertEquals(errorMsg, "Error: Postal Code is required", "Error message is WRONG!");
    }

    // ---------------- Helper Methods ----------------
    private void openCart() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_link")));
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Cart page did NOT open!");
    }

    private void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"), "Checkout Step One page did NOT open!");
    }

    private void fillUserInformation(String firstName, String lastName, String postalCode) {
        driver.findElement(By.id("first-name")).clear();
        driver.findElement(By.id("first-name")).sendKeys(firstName);

        driver.findElement(By.id("last-name")).clear();
        driver.findElement(By.id("last-name")).sendKeys(lastName);

        driver.findElement(By.id("postal-code")).clear();
        driver.findElement(By.id("postal-code")).sendKeys(postalCode);
    }

    private void confirmOrder() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        driver.findElement(By.id("continue")).click();
    }

    private void checkOut(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        driver.findElement(By.id("finish")).click();

    }

    private void validateCompletePage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        String completeMsg = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(completeMsg, "Thank you for your order!", "Order confirmation message is WRONG!");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"), "Checkout complete page did NOT load!");
        System.out.println("✔ Checkout completed successfully!");
    }
}