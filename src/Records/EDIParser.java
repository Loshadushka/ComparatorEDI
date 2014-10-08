package Records;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EDIParser {


    public List<PurchaseOrder> parserMain(InputStream inputStream) throws XMLStreamException {

        PurchaseOrder currPur = null;
        List<PurchaseOrder> purchaseOrdersList = null;
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        boolean flag = false;
        String missedLevel;

        // = "xml/testing1.xml";
        //The preceding  "/" is dependendant on wheterh
        //you are going to be giving a relative or absolute location

        //InputStream is = EDIParser.class.getResourceAsStream(PROPS_FILE);
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        try {
            while (reader.hasNext()) {
                int event = reader.getEventType();
                switch (event) {

                    case XMLStreamConstants.START_DOCUMENT:
                        purchaseOrdersList = new ArrayList<>();
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        if ("PurchaseOrder".equals(reader.getLocalName())) {
                            currPur = new PurchaseOrder();
                        }
                        if ("Routing".equals(reader.getLocalName())) {
                            missedLevel = "Routing";
                            ignoreLevel(reader, flag, missedLevel);
                        }

                        if ("Bundle".equals(reader.getLocalName())) {
                            currPur.curBundle = new Bundle();
                        }

                        if ("ItemsStructure".equals(reader.getLocalName())) {
                            currPur.curIS = new ItemsStructure(null, null, null);
                        }
                        break;


                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "ItemsStructure":
                                currPur.itemsStructureList.add(currPur.curIS);
                                break;
                            case "Bundle":
                                currPur.bundlesList.add(currPur.curBundle);
                                break;
                            case "BundleSKU":
                                currPur.curBundle.BundleSKU = tagContent;
                                break;
                            case "StartDate":
                                currPur.curBundle.StartDate = tagContent;
                                break;
                            case "WileyAccountNoPart1":
                                currPur.WileyAccountNoPart1 = tagContent;
                                break;
                            case "WileyAccountNoPart2":
                                currPur.WileyAccountNoPart2 = tagContent;
                                break;
                            case "CustomerName":
                                currPur.CustomerName = tagContent;
                                break;
                            case "CustomerName2":
                                currPur.CustomerName2 = tagContent;
                                break;
                            case "AddressLine1":
                                currPur.AddressLine1 = tagContent;
                                break;
                            case "AddressLine2":
                                currPur.AddressLine2 = tagContent;
                                break;
                            case "CityName":
                                currPur.CityName = tagContent;
                                break;
                            case "Country":
                                currPur.Country = tagContent;
                                break;
                            case "StateProvince":
                                currPur.StateProvince = tagContent;
                                break;
                            case "ZipPostCode":
                                currPur.ZipPostCode = tagContent;
                                break;
                            case "ShippingChargeAmount":
                                currPur.ShippingChargeAmount = tagContent;
                                break;
                            case "ShipMethod":
                                currPur.ShipMethod = tagContent;
                                break;
                            case "CreditCardNumber":
                                currPur.CreditCardNumber = tagContent;
                                break;
                            case "PromotionCode":
                                currPur.PromotionCode = tagContent;
                                break;
                            case "EmailAddress":
                                currPur.EmailAddress = tagContent;
                                break;
                            case "TransactionID":
                                currPur.TransactionID = tagContent;
                                break;
                            case "SalespersonCode":
                                currPur.SalespersonCode = tagContent;
                                break;
                            case "MarketOutlet":
                                currPur.MarketOutlet = tagContent;
                                break;
                            case "TaxAmount":
                                currPur.TaxAmount = tagContent;
                                break;
                            case "PurchaseOrderNumber":
                                currPur.curIS.PurchaseOrderNumber = tagContent;
                                break;
                            case "ProductID":
                                currPur.curIS.ProductID = tagContent;
                                break;
                            case "Quantity":
                                currPur.curIS.Quantity = tagContent;
                                break;
                            case "Price":
                                currPur.curIS.Price = tagContent;
                                break;
                            case "Discount":
                                currPur.curIS.Discount = tagContent;
                                break;
                            case "PurchaseOrder":
                                purchaseOrdersList.add(currPur);
                                break;
                        }
                        break;
                }
                event = reader.next();
            }

/*
             //Print the Orders from the list populated from XML
             for (PurchaseOrder cur : purchaseOrdersList) {
                 System.out.println("///////////////////////////////////////////");
                 System.out.println(cur);
                 for (Bundle cur2 : cur.bundlesList) {
                     System.out.println("************************************");
                     System.out.println(cur2);

                 }

                 for (ItemsStructure cur2 : cur.itemsStructureList) {
                     System.out.println("------------------------------------------");
                     System.out.println(cur2);
                 }

             }

*/


        } catch (XMLStreamException e) {
            System.err.println("XMLStreamException: " + e.getMessage());
        }


        try {
            inputStream.close();
//            System.out.println(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return purchaseOrdersList;

    }


    static void ignoreLevel(XMLStreamReader inreader, boolean inflag, String missedLevel) throws XMLStreamException {


        while (!((inreader.getEventType() == XMLStreamConstants.END_ELEMENT) && (inflag))) {
            inreader.next();
            if ((inreader.getEventType() == XMLStreamConstants.END_ELEMENT)) {
                inflag = missedLevel.equals(inreader.getLocalName());
            }
        }
    }


    public boolean findOrder(String orderNumber, List<PurchaseOrder> purOrdersList) {

        boolean orderExists = false;

        for (PurchaseOrder current : purOrdersList) {
            for (ItemsStructure current2 : current.itemsStructureList) {
                if (orderNumber.equals(current2.PurchaseOrderNumber)) {
                    orderExists = true;
                }
            }

        }


        return orderExists;

    }


}

