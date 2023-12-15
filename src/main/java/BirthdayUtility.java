
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BirthdayUtility {
    static String ToemailAddress;
    EmailUtility emailUtility = new EmailUtility();

    public Map<String, ArrayList<String>> readXls(String xlsPath, String sheetName) {
        Map<String, ArrayList<String>> name = null;

        try {
            File file = new File(xlsPath);
            FileInputStream fis = new FileInputStream(file);
            Workbook userWorkBook = new XSSFWorkbook(fis);
            Sheet sheet = userWorkBook.getSheet(sheetName);

            name = new HashMap<String, ArrayList<String>>();

            ArrayList<String> nameValue = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();
            int rowCount = sheet.getLastRowNum();

            for (int i =1; i<= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int k=0; k<row.getLastCellNum(); k++) {
                    // 0-> Name, 1-> DOB, 2-> EmailID

                    if (formatter.formatCellValue(row.getCell(1)).equals(getCurrentDate())) {
                        nameValue.clear();
                        nameValue.add(row.getCell(2).getStringCellValue());
                        name.put(row.getCell(0).getStringCellValue(), nameValue);
                        ToemailAddress = row.getCell(2).getStringCellValue(); // emailID
                        emailUtility.setMailServerProperties();
                        emailUtility.createEmailMessage(row.getCell(0).getStringCellValue()); // name
                        emailUtility.sendEmail();
                        if (fis != null) {
                            fis.close();
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        String d = formatter.format(date);
        return d;
    }

    public static void main(String[] args) {

        BirthdayUtility obj = new BirthdayUtility();
        String ExcelFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "main" + File.separator + "resources" + File.separator + "BirthDaySheet.xlsx";

        Map<String, ArrayList<String>> val = obj.readXls(ExcelFilePath, "UserList");

    }


}
