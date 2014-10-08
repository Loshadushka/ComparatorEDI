package Records;

/**
 * Created by ipopovich on 14.08.2014.
 */
public class ComponentData {
    String ProductID;
    String Price;
    String Quantity;


    public ComponentData(String productID, String price, String quantity) {
        ProductID = productID;
        Price = price;
        Quantity = quantity;
    }



        @Override
        public String toString() {
            return

                            "\nProductID: " + ProductID +
                            "\nQuantity: " + Quantity +
                            "\nPrice: " + Price ;


        }


    }


