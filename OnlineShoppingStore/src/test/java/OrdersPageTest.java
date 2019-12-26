import business.OrderImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ActionName;

import java.sql.SQLException;
import java.util.Random;

import static common.Constants.PASS;

public class OrdersPageTest extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignIn();
    }

    @Test
    private void verifyPageContentWhenLoggedInAsManager() {
        signIn.signInAsManager();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        navigationBar.clickOrders();
        String result = orders.verifyPageElements(1, 10);
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void verifyPageContentWhenLoggedInAsEmployee() {
        signIn.signInAsEmployee();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        navigationBar.clickOrders();
        String result = orders.verifyPageElements(1, 10);
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void cancelOrderByManager() {
        try {
            OrderImpl order = (OrderImpl) orderImpl.getOrderWithNewStatus();
            if (order.getOrderId() == null) {
                Assert.fail("No orders with new status in db");
            }
            signIn.signInAsManager();
            synchronized (driver) {
                try {
                    driver.wait(500);
                } catch (InterruptedException e) {
                    Assert.fail("failed to wait");
                }
            }
            navigationBar.initializeSpecialElements();
            navigationBar.clickOrders();
            int index = orders.getOrderIndex(order.getOrderId() + "");
            orders.clickActionOfNthOrder(index, ActionName.CANCEL);
            Thread.sleep(2000);
            OrderImpl updatedOrder = (OrderImpl) orderImpl.getSpecificOrder(order.getOrderId());
            int status = updatedOrder.getOrderStatus();
            orderImpl.updateOrderStatus(updatedOrder.getOrderId(), 0); //revert order status to new
            Assert.assertEquals(status, 2);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get order with new status");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get order with new status");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to get order with new status");
        }
    }

    @Test
    private void cancelOrderByEmployee() {
        try {
            OrderImpl order = (OrderImpl) orderImpl.getOrderWithNewStatus();
            if (order.getOrderId() == null) {
                Assert.fail("No orders with new status in db");
            }
            signIn.signInAsEmployee();
            synchronized (driver) {
                try {
                    driver.wait(500);
                } catch (InterruptedException e) {
                    Assert.fail("failed to wait");
                }
            }
            navigationBar.initializeSpecialElements();
            navigationBar.clickOrders();
            int index = orders.getOrderIndex(order.getOrderId() + "");
            orders.clickActionOfNthOrder(index, ActionName.CANCEL);
            Thread.sleep(2000);
            OrderImpl updatedOrder = (OrderImpl) orderImpl.getSpecificOrder(order.getOrderId());
            int status = updatedOrder.getOrderStatus();
            orderImpl.updateOrderStatus(updatedOrder.getOrderId(), 0); //revert order status to new
            Assert.assertEquals(status, 2);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get order with new status");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get order with new status");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to get order with new status");
        }
    }

    @Test
    private void finishOrderByManager() {
        try {
            OrderImpl order = (OrderImpl) orderImpl.getOrderWithNewStatus();
            if (order.getOrderId() == null) {
                Assert.fail("No orders with new status in db");
            }
            signIn.signInAsManager();
            synchronized (driver) {
                try {
                    driver.wait(500);
                } catch (InterruptedException e) {
                    Assert.fail("failed to wait");
                }
            }
            navigationBar.initializeSpecialElements();
            navigationBar.clickOrders();
            int index = orders.getOrderIndex(order.getOrderId() + "");
            orders.clickActionOfNthOrder(index, ActionName.FINISH);
            Thread.sleep(2000);
            OrderImpl updatedOrder = (OrderImpl) orderImpl.getSpecificOrder(order.getOrderId());
            int status = updatedOrder.getOrderStatus();
            orderImpl.updateOrderStatus(updatedOrder.getOrderId(), 0); //revert order status to new
            Assert.assertEquals(status, 1);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get order with new status");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get order with new status");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to get order with new status");
        }
    }

    @Test
    private void finishOrderByEmployee() {
        try {
            OrderImpl order = (OrderImpl) orderImpl.getOrderWithNewStatus();
            if (order.getOrderId() == null) {
                Assert.fail("No orders with new status in db");
            }
            signIn.signInAsEmployee();
            synchronized (driver) {
                try {
                    driver.wait(500);
                } catch (InterruptedException e) {
                    Assert.fail("failed to wait");
                }
            }
            navigationBar.initializeSpecialElements();
            navigationBar.clickOrders();
            int index = orders.getOrderIndex(order.getOrderId() + "");
            orders.clickActionOfNthOrder(index, ActionName.FINISH);
            Thread.sleep(2000);
            OrderImpl updatedOrder = (OrderImpl) orderImpl.getSpecificOrder(order.getOrderId());
            int status = updatedOrder.getOrderStatus();
            orderImpl.updateOrderStatus(updatedOrder.getOrderId(), 0); //revert order status to new
            Assert.assertEquals(status, 1);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get order with new status");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get order with new status");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to get order with new status");
        }
    }

    @Test
    private void goToOrderDetailUsingManager() {
        signIn.signInAsManager();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        navigationBar.clickOrders();
        int size = orders.getOrdersSize();
        int randomIndex = new Random().nextInt(size - 1) + 1;
        String orderIdToBeClicked = orders.getOrderAtIndex(randomIndex - 1);
        orders.clickActionOfNthOrder(randomIndex, ActionName.SHOW);
        System.out.println(driver.getCurrentUrl());
        System.out.println("http://localhost:4200/order/" + orderIdToBeClicked);
        Assert.assertEquals(driver.getCurrentUrl(),
                "http://localhost:4200/order/" + orderIdToBeClicked);
    }

    @Test
    private void goToOrderDetailUsingEmployee() {
        signIn.signInAsEmployee();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        navigationBar.clickOrders();
        int size = orders.getOrdersSize();
        int randomIndex = new Random().nextInt(size - 1) + 1;
        String orderIdToBeClicked = orders.getOrderAtIndex(randomIndex - 1);
        orders.clickActionOfNthOrder(randomIndex, ActionName.SHOW);
        System.out.println(driver.getCurrentUrl());
        System.out.println("http://localhost:4200/order/" + orderIdToBeClicked);
        Assert.assertEquals(driver.getCurrentUrl(),
                "http://localhost:4200/order/" + orderIdToBeClicked);
    }
}
