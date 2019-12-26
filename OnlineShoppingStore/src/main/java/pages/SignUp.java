package pages;

import business.User;
import business.UserImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static common.Constants.PASS;

public class SignUp implements NonPaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[1]/label/b")
    private WebElement emailAddressLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[2]/label/b")
    private WebElement nameLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[3]/label/b")
    private WebElement passwordLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-login/div/strong")
    private WebElement sampleUsersLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[4]/label/b")
    private WebElement phoneLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[5]/label/b")
    private WebElement addressLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-signup/div/form/div[6]/button")
    private WebElement signUpButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"email\"]")
    private WebElement emailTextBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"name\"]")
    private WebElement nameTextBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"password\"]")
    private WebElement passwordTextBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"phone\"]")
    private WebElement phoneTextBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"address\"]")
    private WebElement addressTextBox;

    public SignUp(NavigationBar navigationBar, WebElementVerifier webElementVerifier) {
        this.webElementVerifier = webElementVerifier;
        this.navigationBar = navigationBar;
    }

    @Override
    public String verifyPageElements() {
        StringBuilder result = new StringBuilder();
        verifyPageLabels(result);
        verifyPageControls(result);
        result.append(navigationBar.verifyNavigationBarElements()).append("\n");
        verifyPagePlaceHolders(result);
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyPagePlaceHolders(StringBuilder result) {
        webElementVerifier.isCorrectPlaceHolder(emailTextBox, result, "emailTextBox", "Enter email");
        webElementVerifier.isCorrectPlaceHolder(nameTextBox, result, "nameTextBox", "Your name");
        webElementVerifier.isCorrectPlaceHolder(addressTextBox, result, "addressTextBox", "Address");
        webElementVerifier.isCorrectPlaceHolder(passwordTextBox, result, "passwordTextBox", "Password");
        webElementVerifier.isCorrectPlaceHolder(phoneTextBox, result, "phoneTextBox", "Phone");
    }

    private void verifyPageControls(StringBuilder result) {
        webElementVerifier.isDisplayedAndEnabled(emailTextBox, "emailTextBox", result);
        webElementVerifier.isDisplayedAndEnabled(nameTextBox, "nameTextBox", result);
        webElementVerifier.isDisplayedAndEnabled(passwordTextBox, "passwordTextBox", result);
        webElementVerifier.isDisplayedAndEnabled(phoneTextBox, "phoneTextBox", result);
        webElementVerifier.isDisplayedAndEnabled(addressTextBox, "addressTextBox", result);
        webElementVerifier.isDisplayed(signUpButton, "signUpButton", result);
        webElementVerifier.isDisabled(signUpButton, "signUpButton", result);

        webElementVerifier.isTextBox(emailTextBox, result, "emailTextBox");
        webElementVerifier.isTextBox(nameTextBox, result, "nameTextBox");
        webElementVerifier.isTextBox(passwordTextBox, result, "passwordTextBox");
        webElementVerifier.isTextBox(phoneTextBox, result, "phoneTextBox");
        webElementVerifier.isTextBox(addressTextBox, result, "addressTextBox");
        webElementVerifier.isSubmitButton(signUpButton, result, "signUpButton");
    }

    private void verifyPageLabels(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(title, "sign up title", result, "Sign Up");
        webElementVerifier.isTextCorrectlyDisplayed(emailAddressLabel, "sign up emailLabel",
                result, "Email address");
        webElementVerifier.isTextCorrectlyDisplayed(nameLabel, "sign up nameLabel",
                result, "Name");
        webElementVerifier.isTextCorrectlyDisplayed(passwordLabel, "sign up passwordLabel",
                result, "Password");
        webElementVerifier.isTextCorrectlyDisplayed(phoneLabel, "sign up phoneLabel",
                result, "Phone");
        webElementVerifier.isTextCorrectlyDisplayed(addressLabel, "sign up addressLabel",
                result, "Address");
    }
    public void clickSignUp() {
        signUpButton.click();
    }

    public void signUpNewUser(User newUser) {
        UserImpl newUserImpl = (UserImpl) newUser;
        emailTextBox.clear();
        emailTextBox.sendKeys(newUserImpl.getEmail());
        nameTextBox.clear();
        nameTextBox.sendKeys(newUserImpl.getName());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(newUserImpl.getPassword());
        phoneTextBox.clear();
        phoneTextBox.sendKeys(newUserImpl.getPhone());
        addressTextBox.clear();
        addressTextBox.sendKeys(newUserImpl.getAddress());
        clickSignUp();
    }
}
