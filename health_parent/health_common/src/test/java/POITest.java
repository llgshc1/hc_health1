import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {
    @Test
    public void run1() throws IOException {
//        创建一个xlsx 工作簿
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("d:\\test\\a.xlsx")));
        XSSFSheet sheetAt = excel.getSheetAt(0);
//        遍历sheet标签页
        for (Row row : sheetAt) {
//            遍历行 获取每个单元格对象
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }
    @Test
    public void run2() throws IOException{

        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("d:\\test\\a.xlsx")));
        XSSFSheet sheetAt = excel.getSheetAt(0);
//        获取当前工作表 最后一个行号 从0开始
        int index = sheetAt.getLastRowNum();
        System.out.println(index);
        for (int i = 0; i <= index; i++) {
            XSSFRow row = sheetAt.getRow(i);//根据行号获取
            short lastCell = row.getLastCellNum();
            for (int j = 0; j < lastCell; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
    }
//    使用POI向EXCEL文件写入数据 将文件保存到磁盘
    @Test
    public void run3()throws IOException{
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("test");
        XSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");
        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("黄灿");
        dataRow.createCell(1).setCellValue("宣城");
        dataRow.createCell(2).setCellValue("22");
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:\\test\\test.xlsx"));
        excel.write(fileOutputStream);
        excel.close();
        fileOutputStream.close();
    }
}
