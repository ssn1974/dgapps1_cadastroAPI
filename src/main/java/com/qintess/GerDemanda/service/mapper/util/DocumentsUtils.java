package com.qintess.GerDemanda.service.mapper.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class DocumentsUtils {

    public static byte[] exportToExcell(List<?> list, String tabName, List<String> headers) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tabName);
        AtomicInteger rowKey = new AtomicInteger(-1);
        generateHeaderXlsx(headers, workbook, sheet, rowKey);
        generateRowsXlsx(list, sheet, rowKey, workbook);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            workbook.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = b.toByteArray();
        Base64.encodeBase64String(bytes);
        return bytes;
    }

    public static byte[] exportToExcell(List<?> list, String tabName, List<String> headers, String subtotal) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(tabName);
        AtomicInteger rowKey = new AtomicInteger(-1);
        generateHeaderXlsx(headers, workbook, sheet, rowKey);
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            workbook.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = b.toByteArray();
        Base64.encodeBase64String(bytes);
        return bytes;
    }

    public static void generateHeaderXlsx(List<String> headers, Workbook workbook, Sheet sheet, AtomicInteger rowKey) {
        Row header = sheet.createRow(rowKey.incrementAndGet());
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        AtomicInteger index = new AtomicInteger(-1);
        headers.stream().forEach(obj -> {
            header.createCell(index.incrementAndGet()).setCellValue(obj);
            sheet.getRow(rowKey.get()).getCell(index.get()).setCellStyle(headerStyle);
        });
        header.setHeight((short) 500);
    }

    public static void generateRowsXlsx(List<?> list, Sheet sheet, AtomicInteger rowKey, Workbook workbook) {
        list.stream().forEach(obj -> {
            Row row = sheet.createRow(rowKey.incrementAndGet());
            AtomicInteger cellKey = new AtomicInteger(-1);
            ReflectionUtils.doWithFields(obj.getClass(), field -> {
                field.setAccessible(true);
                String value = "";
                if (Objects.nonNull(field.get(obj))) {
                    value = field.get(obj).toString();
                }
                row.createCell(cellKey.incrementAndGet()).setCellValue(value);
                sheet.autoSizeColumn(cellKey.get());
            });
        });
    }
}