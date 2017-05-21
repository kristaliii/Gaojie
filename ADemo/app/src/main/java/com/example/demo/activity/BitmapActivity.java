package com.example.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.demo.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapActivity extends Activity {
	private Button bt_bitmap_photo; // 拍照按钮
	private ImageView iv_bitmap_show;
	private Intent openCameraIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitmap);
		bt_bitmap_photo = (Button) findViewById(R.id.bt_bitmap_photo);
		iv_bitmap_show = (ImageView) findViewById(R.id.iv_bitmap_show);
		takaCamera();
	}

	/**
	 * 拍照，并且原图片保存到本地
	 */
	private String str;
	private String img;
	private void takaCamera() {
		bt_bitmap_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File file = new File(Environment.getExternalStorageDirectory(), "国家");
				if (!file.exists()){
					file.mkdirs();
				}
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'IMG'_yyyyMMddHHmmss", Locale.CHINA);
				str = simpleDateFormat.format(date);
				img = String.format("%s.jpg", new Object[] { str }); //IMG_201604120955.jpg
				File localFile = new File(file.getPath()+ File.separator+ img);

				openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(localFile); // Creates a Uri from a file.
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, 0);
			}
		});
	}

	/**
	 * 把保存到本地种图片显示在imageview上
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			String path = Environment.getExternalStorageDirectory().getPath()+"/国家";
			File pathName = new File(path, img);
			Bitmap bitmap = BitmapFactory.decodeFile(pathName.getPath());
			if (bitmap != null) {
				iv_bitmap_show.setImageBitmap(bitmap);
			}else {
				iv_bitmap_show.setImageResource(R.drawable.ic_launcher);
			}
	}

/****************************************************************************************/
//	@Override
//	protected void onPause() {
//		super.onPause();
//		LogUtil.e(this, "onPause");
//	}
//	
//	@Override
//	protected void onStop() {
//		super.onStop();
//		LogUtil.e(this, "onStop");
//	}
//	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		LogUtil.e(this, "onRestart");
//	}
//	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		LogUtil.e(this, "onStart");
//	}
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		LogUtil.e(this, "onResume");
//	}
}
