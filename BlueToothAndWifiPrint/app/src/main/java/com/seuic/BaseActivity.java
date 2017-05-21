package com.seuic;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.seuic.bluetoothmain.DialogUtils;
import com.seuic.bluetoothmain.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.BarCodeType;
import com.snbc.sdk.barcode.enumeration.HRIPosition;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.QRLevel;
import com.snbc.sdk.barcode.enumeration.QRMode;
import com.snbc.sdk.barcode.enumeration.Rotation;

/**
 * Created by bgl on 2017/4/25.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
    }

    /**
     * 打印条码
     */
    public class BarcodePrinterListener implements View.OnClickListener{


        int BarCode_UPCE = BarCodeType.UPCE.getBarCodeType();
        int BARCODE_PDF417 = BarCode_UPCE+1;
        int BARCODE_QR = BarCode_UPCE+2;
        int BARCODE_DATAMATRIX = BarCode_UPCE+3;
        int BARCODE_MAXICODE =  BarCode_UPCE+4;

        Activity activity;
        public BarcodePrinterListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            BarPrinter printer = null;;
            InstructionType mType = null;
            int barCode = 0;
            try {
//                String barcodeType = null;
                String barcodeType = "Code 128";
                SNBCApplication application = (SNBCApplication)getApplication();
                printer = application.getPrinter();
                mType = printer.labelQuery().getPrinterLanguage();
//                barcodeType = sp_print_barcode.getItemAtPosition(sp_print_barcode.getSelectedItemPosition()).toString();

                if ("Code 128".equals(barcodeType)) {
                    barCode = BarCodeType.Code128.getBarCodeType(); // 3
                }else if("Code 39".equals(barcodeType)){
                    barCode = BarCodeType.Code39.getBarCodeType();
                }
                else if("Code 93".equals(barcodeType)){
                    barCode = BarCodeType.Code93.getBarCodeType();
                }
                else if("EAN-8".equals(barcodeType)){
                    barCode = BarCodeType.CodeEAN8.getBarCodeType();
                }
                else if("EAN-13".equals(barcodeType)){
                    barCode = BarCodeType.CodeEAN13.getBarCodeType();
                }
                else if("Codebar".equals(barcodeType)){
                    barCode = BarCodeType.CODABAR.getBarCodeType();
                }
                else if("ITF25".equals(barcodeType)){
                    barCode = BarCodeType.ITF25.getBarCodeType();
                }
                else if("UPC-A".equals(barcodeType)){
                    barCode = BarCodeType.UPCA.getBarCodeType();
                }
                else if("UPC-E".equals(barcodeType)){
                    barCode = BarCodeType.UPCE.getBarCodeType();
                }
                else if("PDF417".equals(barcodeType)){
                    barCode = BARCODE_PDF417;
                }
                else if("QR".equals(barcodeType)){
                    barCode = BARCODE_QR;
                }
                else if("DataMatrix".equals(barcodeType)){
                    barCode = BarCodeType.UPCE.getBarCodeType()+3;
                }
                else if("MaxiCode".equals(barcodeType)){
                    barCode = BarCodeType.UPCE.getBarCodeType()+4;
                }
                switch (mType) {
                    case BPLA:
                        doPrintBarcodeBPLA(barCode, application);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtils.showOneButton(activity,""+e);
            }
        }

        /**
         *
         * @param barCode
         * @param application
         * @throws Exception
         */
        private void doPrintBarcodeBPLA(int barCode, SNBCApplication application) throws Exception {
            BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
            ILabelEdit labelEdit = printer.labelEdit();
            ILabelQuery iLabelQuery = printer.labelQuery();

            labelEdit.setColumn(1, 8);
            labelEdit.setLabelSize(800, 1000);
            if(BarCodeType.Code128.getBarCodeType() == barCode){
                Log.d("tag", "barCode:"+barCode);
                byte[] barcodeData = "123456".getBytes(application.getDecodeType());
                /**
                 * i: x轴位移
                 * i1：y轴位移
                 */
                labelEdit.printBarcode1D(300, 350, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
//                labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
//                labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
//                labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.Code39.getBarCodeType() == barCode){
                byte[] barcodeData = "123ABC".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.Code93.getBarCodeType() == barCode){
                byte[] barcodeData = "12345ABCDE".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
                //data is limited to exactly 7 characters
                byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.CodeEAN13.getBarCodeType() == barCode){
                //data is limited to exactly 12 characters, printer automatically truncates or pads on the left with zeros to achieve the required number of characters
                byte[] barcodeData = "012345678901".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
                byte[] barcodeData = "A1234567A".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }else if(BarCodeType.ITF25.getBarCodeType() == barCode){
                byte[] barcodeData = "123456".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }
            else if(BarCodeType.UPCA.getBarCodeType() == barCode){
                //data is limited to exactly 11 characters
                byte[] barcodeData = "01234567890".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }
            else if(BarCodeType.UPCE.getBarCodeType() == barCode){
                //data is limited to exactly 10 characters
                byte[] barcodeData = "0123456".getBytes(application.getDecodeType());
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
                labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
            }
            else if(BARCODE_PDF417 == barCode){
                String barcodeData = "www.snbc.com 0631-5675888 PDF417";
                labelEdit.printBarcodePDF417(10, 150, Rotation.Rotation0, barcodeData, 3, 4, 4, 18, 3);
                labelEdit.printBarcodePDF417(600, 350, Rotation.Rotation180, barcodeData, 3, 4, 4, 18, 3);
                labelEdit.printBarcodePDF417(10, 990, Rotation.Rotation90, barcodeData, 3, 5, 5, 20, 4);
                labelEdit.printBarcodePDF417(750, 300, Rotation.Rotation270, barcodeData, 3, 5, 5,20, 4);
            }
            else if(BARCODE_QR == barCode){
                String barcodeData = "www.snbc.com 0631-5675888 QR";
                labelEdit.printBarcodeQR(10, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
                labelEdit.printBarcodeQR(310, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_Q.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
                labelEdit.printBarcodeQR(10, 310, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_M.getLevel(), 5, QRMode.QR_MODE_ORIGINAL.getMode());
                labelEdit.printBarcodeQR(310, 310, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_L.getLevel(), 6, QRMode.QR_MODE_ORIGINAL.getMode());
            }else if(BARCODE_DATAMATRIX == barCode){
                String barcodeData = "www.snbc.com 0631-5675888 Data Matrix";
                labelEdit.printBarcodeDataMatrix(10, 10, Rotation.Rotation0, barcodeData, 16, 48, 5);
                labelEdit.printBarcodeDataMatrix(310, 10, Rotation.Rotation90, barcodeData, 16, 48, 5);
                labelEdit.printBarcodeDataMatrix(10, 310, Rotation.Rotation180, barcodeData, 16, 48, 5);
                labelEdit.printBarcodeDataMatrix(310, 310, Rotation.Rotation270, barcodeData, 16, 48, 5);
            }else if(BARCODE_MAXICODE == barCode){
                String barcodeData = "www.snbc.com 0631-5675888 maxicode";
                labelEdit.printBarcodeMaxiCode(10, 10, barcodeData,2);
            }
            printer.labelControl().print(1, 1);
        }

    }

    public void applicationClean(Activity activity) {
        SNBCApplication application = (SNBCApplication) activity.getApplication();
        application.setConnect(null);
        application.setFlashDiskSymbol(null);
        application.setOsFontFileArray(null);
        application.setOsFormatFileArray(null);
        application.setOsImageFileArray(null);
        application.setOsImageFileForPrintArray(null);
        application.setPrinter(null);
        application.setRamDiskSymbol(null);
        application.setStoredBuildinFontArray(null);
        application.setStoredCustomFontArray(null);
        application.setStoredFormatArray(null);
        application.setStoredImageArray(null);
    }
}
