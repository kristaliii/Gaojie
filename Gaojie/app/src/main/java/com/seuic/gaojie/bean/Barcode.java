package com.seuic.gaojie.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Dell on 2017/3/13.
 * 条码类
 */
public class Barcode {
    String barcode;        // 条码
    String barcodeType;    // 条码类型

    String date;           // 条码扫描日期
    Drawable color;        // 新增一个背景属性

    boolean choice;  // 是否已选择

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Drawable getColor() {
        return color;
    }

    public void setColor(Drawable color) {
        this.color = color;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Barcode other = (Barcode) obj;
        if (barcode == null) {
            if (other.barcode != null)
                return false;
        } else if (!barcode.equals(other.barcode))
            return false;
        if (barcodeType == null) {
            if (other.barcodeType != null)
                return false;
        } else if (!barcodeType.equals(other.barcodeType))
            return false;
        if (choice != other.choice)
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result
                + ((barcodeType == null) ? 0 : barcodeType.hashCode());
        result = prime * result + (choice ? 1231 : 1237);
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }
    @Override
    public String toString() {
        return "Barcode{" +
                "barcode='" + barcode + '\'' +
                ", barcodeType='" + barcodeType + '\'' +
                ", date='" + date + '\'' +
                ", color=" + color +
                ", choice=" + choice +
                '}';
    }
}
