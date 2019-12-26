package pages;

import common.WebElementVerifier;
import components.NavigationBar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static common.Constants.PASS;

public class SignIn implements NonPaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private WebDriver driver;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/form/div[1]/label")
    private WebElement emailLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/form/div[2]/label")
    private WebElement passwordLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/form/div[3]/div/label")
    private WebElement rememberMeLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/strong")
    private WebElement sampleUsersLabel;

    @FindBy(how = How.XPATH, using = "//*[@id=\"remember_me\"]")
    private WebElement rememberMeCheckBox;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/form/div[3]/div/a")
    private WebElement sinUpLink;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/form/div[4]/button")
    private WebElement signInButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"sampleLoginTable\"]/tbody/tr/td[1]/a")
    private WebElement customerLink;

    @FindBy(how = How.XPATH, using = "//*[@id=\"sampleLoginTable\"]/tbody/tr/td[2]/a")
    private WebElement employeeLink;

    @FindBy(how = How.XPATH, using = "//*[@id=\"sampleLoginTable\"]/tbody/tr/td[3]/a")
    private WebElement managerLink;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/div")
    private WebElement loggedOutLabel;

    @FindBy(how = How.XPATH, using = "//*[@id=\"email\"]")
    private WebElement emailTextBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"password\"]")
    private WebElement passwordTextBox;

    public SignIn(NavigationBar navigationBar, WebElementVerifier webElementVerifier, WebDriver driver) {
        this.webElementVerifier = webElementVerifier;
        this.navigationBar = navigationBar;
        this.driver = driver;
    }

    @Override
    public String verifyPageElements() {
        StringBuilder result = new StringBuilder();
        result.append(navigationBar.verifyNavigationBarElements()).append("\n");
        verifyPageLabels(result);
        verifyPageControlsType(result);
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyPageControlsType(StringBuilder result) {
        webElementVerifier.isCheckBox(rememberMeCheckBox, result, "rememberMeCheckBox");
        webElementVerifier.isAnchor(sinUpLink, result, "sing in sinUpLink");
        webElementVerifier.isSubmitButton(signInButton, result, "sign in button");
        if (emailTextBox.getAttribute("ng-reflect-model").isEmpty() ||
                passwordTextBox.getAttribute("ng-reflect-model").isEmpty())
            webElementVerifier.isDisabled(signInButton, "signInButton", result);
        else
            webElementVerifier.isEnabled(signInButton, "signInButton", result);
        webElementVerifier.isAnchor(customerLink, result, "customerLink");
        webElementVerifier.isAnchor(employeeLink, result, "employeeLink");
        webElementVerifier.isAnchor(managerLink, result, "managerLink");
    }

    private void verifyPageLabels(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(title, "sign in title", result, "Sign In");
        webElementVerifier.isTextCorrectlyDisplayed(emailLabel, "sign in emailLabel",
                result, "Email address");
        webElementVerifier.isTextCorrectlyDisplayed(passwordLabel, "sign in emailLabel",
                result, "Password");
        webElementVerifier.isTextCorrectlyDisplayed(rememberMeLabel, "sign in remember me",
                result, "Remember me");
        webElementVerifier.isTextCorrectlyDisplayed(sinUpLink, "sign in sinUpLink",
                result, "Sign Up");
        webElementVerifier.isTextCorrectlyDisplayed(customerLink, "sign in customerLink",
                result, "customer1");
        webElementVerifier.isTextCorrectlyDisplayed(employeeLink, "sign in employeeLink",
                result, "employee1");
        webElementVerifier.isTextCorrectlyDisplayed(managerLink, "sign in managerLink",
                result, "manager1");
        webElementVerifier.isTextCorrectlyDisplayed(signInButton, "singInButton",
                result, "Sign In");
        webElementVerifier.isTextCorrectlyDisplayed(sampleUsersLabel, "sampleUsersLabel",
                result, "Sample Users");
    }

    public void clickSignIn() {
        signInButton.click();
    }

    public void setUserName(String email) {
        emailTextBox.clear();
        emailTextBox.sendKeys(email);
    }

    public void setPassword(String password) {
        passwordTextBox.clear();
        passwordTextBox.sendKeys(password);
    }

    public void singInAsCustomer() {
        customerLink.click();
    }

    public void signInAsEmployee() {
        employeeLink.click();
    }

    public void signInAsManager() {
        managerLink.click();
    }

    public boolean verifyInvalidUserNameOrPassword() {
        WebElement invalidUserNameOrPassword = driver.findElement(By.xpath("//body/app-root/div/app-login/div/div"));
        StringBuilder localResult = new StringBuilder();
        webElementVerifier.isTextCorrectlyDisplayed(invalidUserNameOrPassword,
                "invalidUserNameOrPassword", localResult, " Invalid username and password. ");
        return localResult.toString().isEmpty();
    }
}
