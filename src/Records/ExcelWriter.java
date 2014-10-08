package Records;

/**
 * Created by ipopovich on 29.07.2014.
 */


import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


public class ExcelWriter {


    File file;
    FileInputStream fileInputStream;
    InputStream inputStream;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFRow row;
    XSSFCell cell;



    public void init(String directPath, String fileName, String[] ftpSettings) {


        try {


            file = new File(fileName);
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("order number");


            } else {

                fileInputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileInputStream);
                sheet = workbook.getSheet("order number");


            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found" + e);
        } catch (IOException e) {
            System.out.println("IOexception:" + e);
        }

    }


    public void createRow(int row1) throws BiffException, IOException, WriteException {
        row = sheet.createRow(row1);
        }



    public void writeExcel(int column, String string) throws BiffException, IOException, WriteException {


        cell = row.createCell(column);
        cell.setCellValue(string);


    }


    public void writeAndClose(OutputStream os) {
        try {
           rules();
           workbook.write(os);
           os.close();

           } catch (IOException e) {System.out.println ("IOExeption WriteAndClose"+e);}
    }


    public void rules() {
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"false\"");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.RED.index);
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("A1:S150")
        };
        ConditionalFormattingRule[] cfRules =
                {
                        rule1
                };

        sheetCF.addConditionalFormatting(regions, cfRules);
    }

public void makeHyperlink(int column, String string) throws BiffException, IOException, WriteException  {
    writeExcel(column, string);
    CreationHelper createHelper = workbook.getCreationHelper();
    Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
    CellStyle hlink_style = workbook.createCellStyle();
    Font hlink_font = workbook.createFont();
    hlink_font.setUnderline(Font.U_SINGLE);
    hlink_font.setColor(IndexedColors.BLUE.getIndex());
    hlink_style.setFont(hlink_font);
    link.setAddress(string);
    cell.setHyperlink(link);
    cell.setCellStyle(hlink_style);
}



}




