package ru.kmikhails.accountcare.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.kmikhails.accountcare.entity.Account;

import javax.xml.ws.Holder;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class ExcelExporter {
    private static final String SHEET_NAME = "Счета без фактур ЧЦСМ";
    private static final short TITLE_FONT_SIZE = 14;
    private static final short CELL_FONT_SIZE = 12;
    private static final String FONT_NAME = "Times New Roman";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public static void export(List<Account> accounts) {
        String[] headers = "Номер счёта,Дата,Сумма с НДС,Организация".split(",");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setFontName(FONT_NAME);
            font.setFontHeightInPoints(TITLE_FONT_SIZE);
            font.setBold(true);
            headerStyle.setFont(font);

            CellStyle cellStyle = workbook.createCellStyle();
            XSSFFont cellFont = (XSSFFont) workbook.createFont();
            cellFont.setFontName(FONT_NAME);
            cellFont.setFontHeightInPoints(CELL_FONT_SIZE);
            cellStyle.setFont(cellFont);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue(SHEET_NAME);
            headerCell.setCellStyle(headerStyle);

            addTableHeader(sheet, headers, cellStyle);
            addTabelData(accounts, sheet, cellStyle);


            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "счета_без_фактур_цсм.xlsx";
            File exportFile = new File(fileLocation);
            if (exportFile.exists()) {
                exportFile.delete();
            }

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(Sheet sheet, String[] headers, CellStyle cellStyle) {
        Row tableHeader = sheet.createRow(2);
        Holder<Integer> columnNumber = new Holder<>(1);
        Stream.of(headers)
                .forEach(columnTitle -> {
                    Cell cell = tableHeader.createCell(columnNumber.value++);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(columnTitle);
                });
    }

    private static void addTabelData(List<Account> accounts, Sheet sheet, CellStyle cellStyle) {
        Holder<Integer> rowNumber = new Holder<>(3);
        accounts.forEach(account -> {
            Row accountRow = sheet.createRow(rowNumber.value++);
            Cell cell = accountRow.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(account.getAccountNumber());
            sheet.autoSizeColumn(1);

            cell = accountRow.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(FORMATTER.format(account.getAccountDate()));
            sheet.autoSizeColumn(2);

            cell = accountRow.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(account.getAmountWithNDS());
            sheet.autoSizeColumn(3);

            cell = accountRow.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(account.getCompany().getCompany());
            sheet.autoSizeColumn(4);
        });
    }
}
