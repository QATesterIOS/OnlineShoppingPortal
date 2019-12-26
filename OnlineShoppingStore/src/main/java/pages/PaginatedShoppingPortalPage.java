package pages;

interface PaginatedShoppingPortalPage extends ShoppingPortalPage {
    String verifyPageElements(int pageNumber, int pageSize);
}
