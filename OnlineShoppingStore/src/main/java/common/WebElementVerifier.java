package common;

import org.openqa.selenium.WebElement;

public interface WebElementVerifier {
    void isDisplayed(WebElement element, String elementName, StringBuilder result);
    void isElementHidden(WebElement element, String elementName, StringBuilder result);
    void isEnabled(WebElement element, String elementName, StringBuilder result);
    void isDisabled(WebElement element, String elementName, StringBuilder result);
    void isDisplayedAndEnabled(WebElement element, String elementName, StringBuilder result);
    void isTextCorrectlyDisplayed(WebElement element, String elementName,
                                  StringBuilder result, String expectedText);

    void isAnchor(WebElement element, StringBuilder result, String elementName);
    void isCheckBox(WebElement element, StringBuilder result, String elementName);
    void isButton(WebElement element, StringBuilder result, String elementName);
    void isSubmitButton(WebElement element, StringBuilder result, String elementName);
    void isTextBox(WebElement element, StringBuilder result, String elementName);
    void isCorrectPlaceHolder(WebElement element, StringBuilder result,
                              String elementName, String expectedPlaceHolder);

}
