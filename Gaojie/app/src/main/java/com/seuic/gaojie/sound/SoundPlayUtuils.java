package com.seuic.gaojie.sound;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class SoundPlayUtuils {


	// 播放声音前的准备
	private static SoundPool mSoundPool;
	private static int soundID;
	public static void initplaySound(Context context,int resId) {
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 20);
		// "/system/media/audio/notifications/Antimony.ogg"
		soundID = mSoundPool.load(context, resId, 1);
	}

/*	// 播放声音前的准备 (重码)
	private static SoundPool mSoundPool;
	private static int soundID;
	public static void initplaySound(Context context) {
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 20);
		// "/system/media/audio/notifications/Antimony.ogg"
		soundID = mSoundPool.load(context, R.raw.error1, 1);
	}*/

	// 播放声音
	public static void playSound(){
		mSoundPool.play(soundID, 1, 1, 0, 0, 1);
	}


/*	// 播放声音前的准备 (问题码)
	private static SoundPool mSoundPool2;
	private static int soundID2;
	public static void initplaySound2(Context context) {
		mSoundPool2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 20);
		// "/system/media/audio/notifications/Antimony.ogg"
		soundID2 = mSoundPool2.load(context, R.raw.error2, 1);
	}*/

	/*// 播放声音（问题码）
	public static void playSound2(){
		mSoundPool2.play(soundID2, 1, 1, 0, 0, 1);
	}*/

}
