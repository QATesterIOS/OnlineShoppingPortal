package common;

import org.openqa.selenium.WebElement;

public class WebElementVerifierImpl implements WebElementVerifier {
    @Override
    public void isDisplayed(WebElement element, String elementName, StringBuilder result) {
        if(!element.isDisplayed())
            result.append(elementName).append("is not being displayed\n");
    }

    @Override
    public void isElementHidden(WebElement element, String elementName, StringBuilder result) {
        if(element.isDisplayed())
            result.append(elementName).append("is being displayed while it should be hidden\n");
    }

    @Override
    public void isEnabled(WebElement element, String elementName, StringBuilder result) {
        if(!element.isEnabled())
            result.append(elementName).append("appears disabled while it should be enabled\n");
    }

    @Override
    public void isDisabled(WebElement element, String elementName, StringBuilder result) {
        if(element.isEnabled())
            result.append(elementName).append("appears enabled while it should be disabled\n");
    }

    @Override
    public void isDisplayedAndEnabled(WebElement element, String elementName, StringBuilder result) {
        isEnabled(element, elementName, result);
        isDisplayed(element, elementName, result);
    }

    @Override
    public void isTextCorrectlyDisplayed(WebElement element, String elementName,
                                         StringBuilder result, String expectedText) {
        if (!element.getText().trim().equals(expectedText.trim()))
            result.append(elementName).append("text is incorrect\n");
    }

    @Override
    public void isCheckBox(WebElement element, StringBuilder result, String elementName) {
        if(!(element.getTagName().equals("input")
        && element.getAttribute("type").equals("checkbox")))
            result.append("element: ").append(elementName).append("expected to be checkbox")
            .append("but is being displayed as").append(element.getTagName()).append("\n");
    }

    @Override
    public void isAnchor(WebElement element, StringBuilder result, String elementName) {
        if(!element.getTagName().equals("a"))
            result.append("element: ").append(elementName).append("expected to be anchor")
                    .append("but is being displayed as").append(element.getTagName()).append("\n");
    }

    @Override
    public void isButton(WebElement element, StringBuilder result, String elementName) {
        if(!element.getTagName().equals("button"))
            result.append("element: ").append(elementName).append("expected to be button")
                    .append("but is being displayed as").append(element.getTagName()).append("\n");
    }

    @Override
    public void isSubmitButton(WebElement element, StringBuilder result, String elementName) {
        if(!(element.getTagName().equals("button")
                && element.getAttribute("type").equals("submit")))
            result.append("element: ").append(elementName).append("expected to be button")
                    .append("but is being displayed as").append(element.getTagName()).append("\n");
    }

    @Override
    public void isTextBox(WebElement element, StringBuilder result, String elementName) {
        if(!element.getTagName().equals("input"))
            result.append("element: ").append(elementName).append("expected to be text box")
                    .append("but is being displayed as").append(element.getTagName()).append("\n");
    }

    @Override
    public void isCorrectPlaceHolder(WebElement element, StringBuilder result,
                                     String elementName, String expectedPlaceHolder) {
        if(!element.getAttribute("placeholder").trim().equals(expectedPlaceHolder))
            result.append("Incorrect placeholder of element").append(elementName).append(" expected to be")
            .append(expectedPlaceHolder).append(" while it appears as ").append(element.getAttribute("placeholder"));

    }
}
