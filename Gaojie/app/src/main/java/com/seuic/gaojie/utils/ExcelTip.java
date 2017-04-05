package com.seuic.gaojie.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.seuic.gaojie.bean.Barcode;
import com.seuic.gaojie.utils.ui.DialogUtils;
import com.seuic.gaojie.utils.ui.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Dell on 2017/3/13.
 */

public class ExcelTip {
    private static String LOGTAG = "ExcelTip...";
    /**
     * 导出为excel表格  (导出界面)
     * @param context
     * @param outputFilePath  表格名字
     * @param outputFileSheetName  工作表名字
     * @param list
     */
    public static void writeExcelUseFormat(Context context, String outputFilePath,
                                           String outputFileSheetName, ArrayList<Barcode> list) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/GaoJie/export");// mtt/sdcard
            if (!dir.exists()) {
                dir.mkdir(); // 来测试一下export后面可没有/,  发现这样是成功的
            }
            File file = new File(dir, outputFilePath);
            OutputStream os = new FileOutputStream(file.getPath());// 输出的Excel文件URL
            WritableWorkbook wwb = Workbook.createWorkbook(os);// 创建可写工作薄
            WritableSheet sheet = wwb.createSheet(outputFileSheetName, 0);// 创建可写工作表

            sheet.addCell(new Label(0,0,"快递单号")); // 0列1行--A1
            sheet.addCell(new Label(1, 0, "类型")); // 1列1行--B1
            for (int i = 0; i < list.size(); i++) {
                Barcode item = list.get(i);
                sheet.addCell(new Label(0, i+1, item.getBarcode())); // 0列放条码 （i+1）+1 行
                sheet.addCell(new Label(1, i+1, item.getBarcodeType())); // 1列放类型 （i+1）+1行
            }
            sheet.getSettings().setProtected(true); // 设置xls的保护，单元格为只读的
//            sheet.getSettings().setPassword("123"); // 设置xls的密码
            sheet.setColumnView(0, 20);// 设置第1列宽度，
            wwb.write();
            wwb.close();
            os.close();
            ToastUtils.showToast(context, "导出成功");
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"FileNotFundException");
        } catch (IOException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"IOException");
        } catch (RowsExceededException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"RowsExceededException");
        } catch (WriteException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"WriteException");
        }
    }

    /**
     * 导入excel表格
     * @param path  目录地址
     * @return
     */
    public static ArrayList<Barcode> readExcel(String path) {
        ArrayList<Barcode> list = new ArrayList<>();
        try {
            // 第 1 步 选取Excel文件得到工作薄Workbook
            Workbook workbook = Workbook.getWorkbook(new File(path));
            // 第 2 步 通过Workbook的getSheet方法选择第一个工作表（从0开始）-- 也可以通过工作的名称来得到Sheet
            Sheet sheet = workbook.getSheet(0);
            // 第 3 步 读取单元格 通过Sheet的getCell方法选择位置为C2的单元格（两个参数都从0开始）
            int rows = sheet.getRows();
            if (rows == 0) {
                return list;
            }
            Log.d("tag", "ExcelTip...表名字："+ sheet.getName());
            Log.d("tag", "ExcelTip..."+"总行数："+rows);
            Log.d("tag",  "ExcelTip..."+"第0列2行："+sheet.getCell(0,1).getContents()); // 0列2行的数据
            for (int i = 1; i < rows; i++) {
                Barcode item = new Barcode();
                String barcode = sheet.getCell(0, i).getContents();
                String type = sheet.getCell(1, i).getContents();
                item.setBarcode(barcode);
                item.setBarcodeType(type);
                list.add(item);
            }
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 在导入界面导出excel表
     * @param context
     * @param outputFilePath
     * @param outputFileSheetName
     * @param list
     */
    public static void writeExcelInImport(Context context, String outputFilePath,
                                           String outputFileSheetName, ArrayList<Barcode> list) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/GaoJie/import");// mtt/sdcard
            if (!dir.exists()) {
                dir.mkdir(); // 来测试一下export后面可没有/,  发现这样是成功的
            }
            File file = new File(dir, outputFilePath);
            OutputStream os = new FileOutputStream(file.getPath());// 输出的Excel文件URL
            WritableWorkbook wwb = Workbook.createWorkbook(os);// 创建可写工作薄
            WritableSheet sheet = wwb.createSheet(outputFileSheetName, 0);// 创建可写工作表

            sheet.addCell(new Label(0,0,"快递单号")); // 0列1行--A1
            sheet.addCell(new Label(1, 0, "类型")); // 1列1行--B1
            for (int i = 0; i < list.size(); i++) {
                Barcode item = list.get(i);
                sheet.addCell(new Label(0, i+1, item.getBarcode())); // 0列放条码 （i+1）+1 行
                sheet.addCell(new Label(1, i+1, item.getBarcodeType())); // 1列放类型 （i+1）+1行
            }
            sheet.getSettings().setProtected(true); // 设置xls的保护，单元格为只读的
//            sheet.getSettings().setPassword("123"); // 设置xls的密码
            sheet.setColumnView(0, 20);// 设置第1列宽度，
            wwb.write();
            wwb.close();
            os.close();

//            ToastUtils.showToast(context, "导出成功");
//            ToastUtils.setNull();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"FileNotFundException");
        } catch (IOException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"IOException");
        } catch (RowsExceededException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"RowsExceededException");
        } catch (WriteException e) {
//            e.printStackTrace();
            DialogUtils.hintOneDialog(context,"WriteException");
        }
    }



    public static ArrayList<Barcode> testList() {
        ArrayList<Barcode> list = new ArrayList<>();
        for (int i=0;i<3;i++) {
            Barcode item = new Barcode();
            item.setBarcodeType("测试"+i);
            item.setBarcode("条码"+i);
            list.add(item);
        }
        return list;
    }
}
