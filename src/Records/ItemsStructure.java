package Records;

/**
 * Created by ipopovich on 21.07.2014.
 */
public class ItemsStructure extends ComponentData{
    String PurchaseOrderNumber;
    String Discount;

    public ItemsStructure(String productID, String price, String quantity) {
        super(productID, price, quantity);

    }


    @Override
    public String toString() {
        return
                "PurchaseOrderNumber: " + PurchaseOrderNumber +
                        "\nProductID: " + ProductID +
                        "\nQuantity: " + Quantity +
                        "\nPrice: " + Price +
                        "\nDiscount: " + Discount;

    }


}
