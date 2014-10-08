package Records;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.ArrayUtils.indexOf;

/**
 * Created by ipopovich on 16.07.2014.
 */


public class Test {

    public static void mains(String reportRecords) throws XMLStreamException, ParseException, WriteException, IOException, BiffException, InterruptedException {

        long lBegin = System.currentTimeMillis();


        EDIParser ediParser = new EDIParser();

        String[] ftpSettings = {"ftp server address", "login", "password"};
        String[] ftpPath = {"rootFolder/Artifacts/", "rootFolder/Reports/", "rootFolder/Archive/", "rootFolder/Reports/EDIComparation/"};
        String[] artifacts = {"cpa_artifact.xls", "cma_2015_product_list.xls","CFA_Phase1_Product_list.xls"};
        String[] products = {"cpa", "cma", "cfa"};
        Excel excelReport = new Excel();
        Excel excelArt = new Excel();
        ExcelWriter reportExcel = new ExcelWriter();
        ArrayList<String> artSKU = new ArrayList<String>();
        ArrayList<String> filesForParsing,arrayOfSKU;
        ArrayList<String> arrayDate = new ArrayList<String>();
        ArrayList<String> ProductName = new ArrayList<String>();
        LinkedList<ComponentData> artCompData = new LinkedList<ComponentData>();
        List<ExcelReportData> excelReportDataList = new ArrayList<>();
        ExcelReportData excelReportData, ediData;
        int[] startPoint = new int[]{0, 0, 0};
        ActionsOnParsedData actionsOnParsedData = new ActionsOnParsedData();
        boolean flag;
        List<Field> fields1 = new ArrayList<>();
        Stream stream = new Stream();
        excelReportData = new ExcelReportData();
        FTPforEDI ftPforEDI = new FTPforEDI(ftpSettings);
        int x = 1; //Row which with first record


        excelReport.openExcel(ftpPath[1], reportRecords, ftpSettings);
        excelArt.openExcel(ftpPath[0], artifacts[checkingproduct(reportRecords,products)], ftpSettings);

        fields1 = getAllFields(fields1, excelReportData.getClass());


        while (x < excelReport.sheet.getRows()) {

            ProductName.add(excelReport.getCell(startPoint[0], startPoint[1], x));
            arrayDate.add(excelReport.getCell(startPoint[0], startPoint[1] + 1, x));
            excelReportData = new ExcelReportData();
            try {
                for (Field field : fields1) {
                    field.set(excelReportData, excelReport.getCell(startPoint[0], startPoint[1] + fields1.indexOf(field) + 2, x));

                }
            } catch (IllegalAccessException e) {

                System.out.println("Exception thrown  :" + e);
            }

            excelReportDataList.add(excelReportData);
            x++;
        }


        excelReport.closeExcel();


        for (ExcelReportData reportData : excelReportDataList) {
            flag = false;
            String string = arrayDate.get(excelReportDataList.indexOf(reportData));
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(string.replace(".", "/"));
            filesForParsing = ftPforEDI.ftpStream(date, ftpPath[2]);

            for (Object curFile : filesForParsing) {

                List<PurchaseOrder> orderBundleProducts = ediParser.parserMain(stream.inputStream(ftpPath[2], curFile.toString(), ftpSettings));
                if (ediParser.findOrder(reportData.orderID, orderBundleProducts)) {

                    arrayOfSKU = actionsOnParsedData.getRequestedArray(orderBundleProducts, reportData.orderID, "SKU");
                    LinkedList<ComponentData> ediComp = actionsOnParsedData.getRequestedLinkedList(orderBundleProducts,reportData.orderID);
                    ediData = actionsOnParsedData.getRequestedArray(orderBundleProducts, reportData.orderID);


                    String[] parts = ProductName.get(excelReportDataList.indexOf(reportData)).split("/");
                    for (Object cur: parts) {
                        startPoint = excelArt.getStartPoint(cur.toString());
                        artSKU.add(excelArt.getCell(startPoint[0], startPoint[1] + 5, startPoint[2]));
                        int abc = 0;
                        while (excelArt.getCell(startPoint[0], startPoint[1], ++startPoint[2]).equals("")) {

                            artCompData.add(abc,new ComponentData((excelArt.getCell(startPoint[0], (startPoint[1] + 2), startPoint[2])),(excelArt.getCell(startPoint[0], (startPoint[1] + 7), startPoint[2]).replace(",", ".")),"1"));
                            abc++;

                        }
                    }




//Comparing prices for bundle

                    reportExcel.init(ftpPath[3], "order" + reportData.orderID + ".xlsx", ftpSettings);
                    try {
                    reportExcel.createRow(0);
                    reportExcel.writeExcel(0, "Artifact product");
                    reportExcel.writeExcel(1, "EDI product");
                    reportExcel.writeExcel(2, "Art.price");
                    reportExcel.writeExcel(3, "EDI price");
                    reportExcel.writeExcel(4, "Prices are equal");
                    reportExcel.writeExcel(5, "Quantity are 1");


                    int usedCells = comparingData(artCompData,ediComp, reportExcel);



                    try {

                        int a = 0;
                        for (Field field : fields1) {

                            reportExcel.createRow(usedCells + fields1.indexOf(field) + 2);
                            reportExcel.writeExcel(0, field.get(reportData).toString());
                            reportExcel.writeExcel(1, field.get(ediData).toString());
                            reportExcel.writeExcel(2, Boolean.toString(field.get(reportData).toString().equals(field.get(ediData).toString())));
                            if (a<artSKU.size())
                            {reportExcel.writeExcel(3, artSKU.get(a));}
                            if (a<arrayOfSKU.size())
                            {reportExcel.writeExcel(4, arrayOfSKU.get(a));}
                        a++;

                        }

                    } catch (IllegalAccessException e) {

                        System.out.println("Exception thrown  :" + e);
                    }


                    reportExcel.makeHyperlink(10, "ftp://" + ftpSettings[1] + ":" + ftpSettings[2] + "@" + ftpSettings[0] + "/" + ftpPath[2] + curFile.toString());
                    } catch (BiffException | IOException | WriteException e) {
                        e.printStackTrace();
                    }





                    reportExcel.writeAndClose(stream.outputStream(ftpPath[3], "order" + reportData.orderID + ".xlsx", ftpSettings));
                    artCompData.clear();
                    artSKU.clear();
                    flag = true;

                }


                if (flag) {
                    break;
                }

            }
        }


        excelArt.closeExcel();
        ftPforEDI.close();
        long lEnd = System.currentTimeMillis();
        long lDelta = lEnd - lBegin;
        System.out.println(lDelta);


    }




    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
             fields.add(field);
        }

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private static int checkingproduct(String stt , String[] products) throws InterruptedException {

        int indexOfproduct = 0;
        boolean prefixError = true;


    for( Object cur:products)

    {

        indexOfproduct = indexOf(products, cur.toString());

        if (stt.toLowerCase().contains(cur.toString().toLowerCase())) {
            prefixError = false;
            break;
        } else {
            prefixError = true;
        }


    }

        if((prefixError)&&(indexOfproduct==2))

        {
            throw new InterruptedException("Report file " + stt + " has not got product prefix! Report should consist of records for one product (CPA/CMA/CFA) only and be named with appropriate prefix (cpa/cma/cfa)");
        }


        return indexOfproduct;

}

    static void fillInCells(LinkedList<ComponentData> artComp, LinkedList<ComponentData> ediComp, ExcelWriter reportExcel) {

        for (int i = 0; i < artComp.size(); i++)
            try {
                reportExcel.createRow(i+1);
                reportExcel.writeExcel(0, artComp.get(i).ProductID);
                reportExcel.writeExcel(1, ediComp.get(i).ProductID);
                reportExcel.writeExcel(2, artComp.get(i).Price);
                reportExcel.writeExcel(3, ediComp.get(i).Price);
                reportExcel.writeExcel(4, Boolean.toString(artComp.get(i).Price.equals(ediComp.get(i).Price)));
                reportExcel.writeExcel(5, Boolean.toString(artComp.get(i).Quantity.equals(ediComp.get(i).Quantity)));

            } catch (BiffException | IOException | WriteException e) {
                e.printStackTrace();
            }


    }






    static int comparingData (LinkedList<ComponentData> list1, LinkedList<ComponentData> list2, ExcelWriter reportExcel  ) {

        equalizingListSizes(list1, list2);

            int l1pointer = 0;

            while ((l1pointer < list1.size()) && !(list1.get(l1pointer).ProductID.equals("Artifact hasn't got such element in the bundle")))
            {

                boolean notInList = true;
                int l2pointer = l1pointer;

                while ((l2pointer < list2.size()) && (notInList)) {
                    if (list1.get(l1pointer).ProductID.equals(list2.get(l2pointer).ProductID)) {
                        Collections.swap(list2, l1pointer, l2pointer);
                        notInList = false;
                    }
                    if ((l2pointer == (list2.size() - 1)) && notInList) {
                        list2.add(l1pointer, new ComponentData("EDI hasn't got such element","-" ,"-"));
                        l2pointer++;
                    }
                    l2pointer++;

                }
                equalizingListSizes(list1, list2);
                l1pointer++;

                 }

        fillInCells(list1, list2 , reportExcel);


return list1.size();
        }

    static LinkedList<ComponentData> equalizingListSizes (LinkedList<ComponentData> list1, LinkedList<ComponentData> list2){

        int sizeDiff = list2.size()-list1.size();

        if (sizeDiff>0)
        {
            for ( int i = 0; i <sizeDiff; i++)
            {
                list1.add(new ComponentData("Artifact hasn't got such element in the bundle","-","-"));

            }
        }

        return list1;

    }



 }















