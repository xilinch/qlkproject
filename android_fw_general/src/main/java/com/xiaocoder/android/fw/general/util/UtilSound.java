package com.xiaocoder.android.fw.general.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by xiaocoder on 2015/11/11.
 */
public class UtilSound {

    /**
     * 扬声器 听筒
     */
    public static void setSpeakerphoneOn(Activity activity, boolean on) {

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        if (on) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            //关闭扬声器
            audioManager.setSpeakerphoneOn(false);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }
}
