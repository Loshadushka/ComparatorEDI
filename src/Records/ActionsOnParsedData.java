package Records;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ipopovich on 23.07.2014.
 */

public class ActionsOnParsedData {

    ArrayList<String> requestedData = null;
    //  ArrayList orderPrices = null;
    //   ArrayList bundleQuantity = null;

    //we get bundles Products' id by using orderNumber
    public ArrayList<String> getRequestedArray(List<PurchaseOrder> purchaseOrdersList, String orderNumber, String gather) {

        requestedData = new ArrayList<String>();
        boolean flag = false;

        for (PurchaseOrder cur : purchaseOrdersList) {
            for (ItemsStructure cur2 : cur.itemsStructureList) {
                if (cur2.PurchaseOrderNumber.equals(orderNumber)) {
                    switch (gather) {
                        case "id":
                            requestedData.add(cur2.ProductID);
                            break;
                        case "price":
                            requestedData.add(cur2.Price);
                            break;
                        case "quantity":
                            requestedData.add(cur2.Quantity);
                            break;
                        case "SKU":
                            for (Bundle cur3 : cur.bundlesList) {
                                requestedData.add(cur3.BundleSKU);
                            }

                            flag = true;
                            break;

                    }
                }
                if (flag) {
                    break;
                }
            }
        }
        return requestedData;
    }


    public ExcelReportData getRequestedArray(List<PurchaseOrder> purchaseOrdersList, String orderNumber) {

        ExcelReportData excelReportData = new ExcelReportData();

        for (PurchaseOrder cur : purchaseOrdersList) {
            for (ItemsStructure cur2 : cur.itemsStructureList) {
                if (cur2.PurchaseOrderNumber.equals(orderNumber)) {
                    excelReportData.orderID = cur2.PurchaseOrderNumber;
                    excelReportData.EmailAddress = cur.EmailAddress;
                    excelReportData.CustomerName = cur.CustomerName;
                    excelReportData.CustomerName2 = cur.CustomerName2;
                    excelReportData.AddressLine1 = cur.AddressLine1;
                    excelReportData.AddressLine2 = cur.AddressLine2;
                    excelReportData.CityName = cur.CityName;
                    excelReportData.Country = cur.Country;
                    excelReportData.StateProvince = cur.StateProvince;
                    excelReportData.ZipPostCode = cur.ZipPostCode;
                    excelReportData.ShipMethod = cur.ShipMethod;
                    excelReportData.ShippingChargeAmount = cur.ShippingChargeAmount;
                    excelReportData.CreditCardNumber = cur.CreditCardNumber;
                    excelReportData.PromotionCode = cur.PromotionCode;
                    excelReportData.TaxAmount = cur.TaxAmount;
                }
            }
        }

        return excelReportData;
    }



    public LinkedList<ComponentData> getRequestedLinkedList(List<PurchaseOrder> purchaseOrdersList, String orderNumber) {

       LinkedList <ComponentData> list = new LinkedList<ComponentData>();
        boolean flag = false;

        for (PurchaseOrder cur : purchaseOrdersList) {
            for (ItemsStructure cur2 : cur.itemsStructureList) {
                if (cur2.PurchaseOrderNumber.equals(orderNumber)) {


                    for ( int x = 0; x < Integer.parseInt(cur2.Quantity) ; x++) {

                        list.add(new ComponentData(cur2.ProductID, cur2.Price, "1"));
                    }

                    }
                }

            }

        return list;
    }






}
