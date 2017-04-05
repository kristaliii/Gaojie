package com.seuic.gaojie;

/**
 * Created by Dell on 2017/3/13.
 */

public interface Constant {
    // 扫描工具内应用设置的广播名称action;
     String BROADCAST_SETTING = "com.android.scanner.service_settings";

    // 扫描工具内开发者项，广播名称的 key；
     String BROADCAST_VALUE = "action_barcode_broadcast";

    // 自定义扫描工具内开发者项内广播名称value；
     String CUSTOM_NAME = "com.seuic.gaojie";

    // 自定义扫描工具内开发者项内键值名字key
    String BROADCAST_KEY = "key_barcode_broadcast";

    // 自定义扫描工具内开发者项内键值名字value
    String CUSTOM_KEY = "scannerdata";

    // 扫描工具内应用设置下的条码发送方式的广播名称key；
     String SEND_KEY = "barcode_send_mode";

    // 扫描工具内应用设置下的条码结束符广播名称key；
    String END_KEY = "endchar";

    // 扫描震动 key
    String VIBERATE = "viberate";
    // 扫描声音
    String SOUND_PLAY = "sound_play";

    // 扫描工具下的连续扫描的key  intent.putExtra(name,value)中的name;
    String SCAN_CONTINUE = "scan_continue";

    // 导出excel表的表名字前缀
    String EXPORT_EXCEL_NAME = "export"; // .xls

    // 在导入界面导出excel表名字前缀
    String NO_MATCH_NAME = "noMatch"; // .xls


    String MATHCH_NAME = "match"; // .xls


    String IMPORT_EXCEL_NAME = "book.xls";

    String FILE_DIR = "GaoJie";
}
