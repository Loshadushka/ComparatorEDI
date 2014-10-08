package Records;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by ipopovich on 23.07.2014.
 */
public class Excel {
    InputStream is;
    Workbook workbook;
    Sheet sheet;
    Cell cell1;



    public void openExcel(String path, String fileName, String[] ftpSettings ) {
        try {

            is = new Stream().inputStream(path, fileName, ftpSettings);
            workbook = Workbook.getWorkbook(is);
            sheet = workbook.getSheet(0);

        } catch (BiffException e) {
            System.out.println("BiffException occurs when get workbook"+e);
        } catch (IOException e) {
            System.out.println("IOException occurs when get workbook"+e);
        }
    }

    public String getCell(int sheetnum, int column, int row) {
        sheet = workbook.getSheet(sheetnum);
        cell1 = sheet.getCell(column, row);

        return cell1.getContents();

    }

    public void closeExcel() {
        this.workbook.close();

        try {
            this.is.close();
      //      System.out.println(this.is.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[] getStartPoint(String productName) {

        // sheet = workbook.getSheet(sheetNumber);
        //cell1 = sheet.getCell(column, row);
        int x = 0;
        boolean flag = false;
        int[] anArray = new int[3];

        while ((x < workbook.getNumberOfSheets()) && !flag) {
            if (workbook.getSheet(x).findCell(productName) == null) {
                ++x;
            } else {
                flag = true;
                cell1 = workbook.getSheet(x).findCell(productName);
                anArray[0] = x;
                anArray[1] = cell1.getColumn();
                anArray[2] = cell1.getRow();
            }
        }
        return anArray;
    }


}


