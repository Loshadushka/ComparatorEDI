package Records;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ipopovich on 17.07.2014.
 */
public class PurchaseOrder extends AbstractPurchase {

    String WileyAccountNoPart1;
    String WileyAccountNoPart2;
    String TransactionID;
    String SalespersonCode;
    String MarketOutlet;


    ItemsStructure curIS = new ItemsStructure(null,null,null);
    List<ItemsStructure> itemsStructureList = new ArrayList<>();
    Bundle curBundle = new Bundle();
    List<Bundle> bundlesList = new ArrayList<>();


    @Override
    public String toString() {
        return

                "WileyAccountNoPart1: " + WileyAccountNoPart1 +
                        "\nWileyAccountNoPart2: " + WileyAccountNoPart2 +
                        "\nCustomerName: " + CustomerName +
                        "\nCustomerName2: " + CustomerName2 +
                        "\nAddressLine1: " + AddressLine1 +
                        "\nAddressLine2: " + AddressLine2 +
                        "\nCityName: " + CityName +
                        "\nCountry: " + Country +
                        "\nStateProvince: " + StateProvince +
                        "\nZipPostCode: " + ZipPostCode +
                        "\nShippingChargeAmount: " + ShippingChargeAmount +
                        "\nShipMethod: " + ShipMethod +
                        "\nCreditCardNumber: " + CreditCardNumber +
                        "\nPromotionCode: " + PromotionCode +
                        "\nEmailAddress: " + EmailAddress +
                        "\nTransactionID: " + TransactionID +
                        "\nSalespersonCode: " + SalespersonCode +
                        "\nMarketOutlet: " + MarketOutlet +
                        "\nTaxAmount: " + TaxAmount;


    }


}
