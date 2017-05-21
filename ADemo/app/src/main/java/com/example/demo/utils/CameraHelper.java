package com.example.demo.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraHelper
{
//  private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
//  private static final String LOG_TAG = "CameraHelper";
  private static Uri fileUri;

  private static Uri getOutputMediaFileUri()
  {
    Object localObject = null;
    try
    {
      File localFile1 = new File(Environment.getExternalStorageDirectory(), "国家兽药");
      localObject = localFile1;
      if (!((File) localObject).exists())
        ((File) localObject).mkdirs();
      if (((File) localObject).exists())
      {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA);
        Date localDate = new Date();
        String str = localSimpleDateFormat.format(localDate);
        File localFile2 = new File(((Uri) localObject).getPath() + File.separator + String.format("%s.jpg", new Object[] { str }));
        Uri localUri = Uri.fromFile(localFile2);
        return localUri;
      }
      return null;
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        Log.d("CameraHelper", "Error in Creating mediaStorageDir: " + localObject);
      }
    }
  }

  public static boolean takePhoto(Activity paramActivity)
  {
    Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    fileUri = getOutputMediaFileUri();
    if (fileUri != null);
    for (int i = 1; ; i = 0)
    {
      int j = i;
      localIntent.putExtra("output", fileUri);
      paramActivity.startActivityForResult(localIntent, 100);
      return true;
    }
  }
}
