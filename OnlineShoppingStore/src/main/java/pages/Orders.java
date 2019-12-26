package pages;

import business.Order;
import business.OrderImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import components.PaginationControl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static common.Constants.PASS;

public class Orders implements PaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Order orderImpl;
    private PaginationControl paginationControl;
    private WebDriver driver;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/thead/tr/*")
    private List<WebElement> tableHeaders;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/th")
    private List<WebElement> orderIds;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[1]")
    private List<WebElement> customerNames;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[2]")
    private List<WebElement> customerEmails;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[3]")
    private List<WebElement> customerPhones;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[4]")
    private List<WebElement> shippingAddresses;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[5]")
    private List<WebElement> ordersTotal;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[6]")
    private List<WebElement> orderDates;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-order/table/tbody/*/td[7]")
    private List<WebElement> orderStatuses;

    public Orders(WebElementVerifier webElementVerifier, NavigationBar navigationBar,
                  Order orderImpl, PaginationControl paginationControl, WebDriver driver) {
        this.navigationBar = navigationBar;
        this.orderImpl = orderImpl;
        this.webElementVerifier = webElementVerifier;
        this.paginationControl = paginationControl;
        this.driver = driver;
    }

    @Override
    public String verifyPageElements(int pageNumber, int pageSize) {
        StringBuilder result = new StringBuilder();
        result.append(navigationBar.verifyNavigationBarElements());
        result.append(paginationControl.verifyPaginationControlStatus(pageNumber, pageSize));
        verifyTableHeaders(result);
        verifyLinksBasedOnOrderStatus(result);
        try {
            verifyFirstProductInfo(result);
        } catch (SQLException e) {
            result.append("SQLException, failed to orders from db\n").append(e.getMessage()).append("\n");
        } catch (ClassNotFoundException e) {
            result.append("ClassNotFoundException, failed to get order info from db\n")
                    .append(e.getMessage()).append("\n");
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyFirstProductInfo(StringBuilder result) throws SQLException, ClassNotFoundException {
        OrderImpl actualOrder = getFirstActualOrder(); // get first one only for sake of simplicity
        OrderImpl expectedOrder = (OrderImpl) orderImpl.getSpecificOrder(actualOrder.getOrderId());
        if (!(expectedOrder.getOrderId().equals(actualOrder.getOrderId())
                && expectedOrder.getCustomerAddress().equals(actualOrder.getCustomerAddress())
                && expectedOrder.getCustomerEmail().equals(actualOrder.getCustomerEmail())
                && expectedOrder.getCustomerName().equals(actualOrder.getCustomerName())
                && expectedOrder.getCustomerPhone().equals(actualOrder.getCustomerPhone())
                && expectedOrder.getOrderAmount() == actualOrder.getOrderAmount()
                && expectedOrder.getOrderStatus() == actualOrder.getOrderStatus()
        )) {
            result.append("Incorrect order info is being displayed in orders page\n");
        }
    }

    private void verifyLinksBasedOnOrderStatus(StringBuilder result) {
        for (int i = 0; i < orderStatuses.size(); i++) {
            int index = i + 1;
            String orderStatus = orderStatuses.get(i).getText();
            List<String> availableLinks = driver.findElements(
                    By.xpath("/html/body/app-root/div/app-order/table/tbody/tr[" + index + "]/td[8]/*")).stream()
                    .map(WebElement::getText).collect(Collectors.toList());
            if (orderStatus.equals("New") &&
                    (availableLinks.size() != 3
                            || availableLinks.indexOf("Show") == -1
                            || availableLinks.indexOf("Cancel") == -1
                            || availableLinks.indexOf("Finish") == -1))
                result.append("Incorrect actions appear for order at index").append(i).append("\n");
            else if ((orderStatus.equals("Finished") || orderStatus.equals("Cenceled")) &&
                    (availableLinks.size() != 1 || availableLinks.indexOf("Show") == -1))
                result.append("Incorrect actions appear for order at index").append(i).append("\n");
        }
    }

    public OrderImpl getFirstActualOrder() {
        OrderImpl order = new OrderImpl();
        order.setCustomerAddress(shippingAddresses.get(0).getText());
        order.setCustomerEmail(customerEmails.get(0).getText());
        order.setCustomerName(customerNames.get(0).getText());
        order.setCustomerPhone(customerPhones.get(0).getText());
        order.setOrderAmount(Double.parseDouble(ordersTotal.get(0).getText()
                .substring(ordersTotal.get(0).getText().indexOf("$") + 1)));
        order.setOrderId(Long.valueOf(orderIds.get(0).getText()));
        order.setOrderStatus(mapOrderStatus(orderStatuses.get(0).getText()));
        return order;
    }

    // ToDO: move this into orders interface
    private int mapOrderStatus(String status) {
        if (status.equals("New"))
            return 0;
        else if (status.equals("Finished"))
            return 1;
        else
            return 2;
    }

    private void verifyTableHeaders(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(0),
                "OrderIdHeader", result, "Order #");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(1),
                "NameHeader", result, "Customer Name");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(2),
                "EmailHeader", result, "Customer Email");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(3),
                "Phoneeader", result, "Customer phone");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(4),
                "AddressHeader", result, "Shipping Address");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(5),
                "TotalHeader", result, "Total");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(6),
                "DateHeader", result, "Order Data");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(7),
                "StatusHeader", result, "Status");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(8),
                "ActionHeader", result, "Action");
    }

    // order index starts from 1
    public boolean clickActionOfNthOrder(int orderIndex, ActionName actionName) {
        if (orderIndex > orderIds.size())
            return false;
        else {
            List<WebElement> neededAction = driver.findElements(
                    By.xpath("//body/app-root/div/app-order/table/tbody/tr[" + orderIndex + "]/td[8]/*"))
                    .stream()
                    .filter(element -> element.getText().equals(String.valueOf(actionName.getName())))
                    .collect(Collectors.toList());
            if (neededAction.size() > 0) {
                neededAction.get(0).click();
                return true;
            } else {
                return false;
            }
        }
    }

    public int getOrderIndex(String orderId) {
        return orderIds.stream().map(WebElement::getText)
                .collect(Collectors.toList()).indexOf(orderId) + 1;
    }

    public int getOrdersSize() {
        return orderIds.size();
    }

    public String getOrderAtIndex(int index) {
        return orderIds.get(index).getText();

    }
}
