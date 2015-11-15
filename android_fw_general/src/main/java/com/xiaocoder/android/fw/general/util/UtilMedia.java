package com.xiaocoder.android.fw.general.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaocoder on 2015/9/17.
 * <p/>
 * 查询手机中的多媒体资源 （音频 视频 图片）
 */
public class UtilMedia {


    // 结束播放
    public static void finishPlaying(MediaPlayer mediaplayer) {
        if (mediaplayer != null) {
            mediaplayer.stop();
            mediaplayer.release();
        }
    }

    // 开始播放
    public static MediaPlayer openVoice(String url) {
        try {
            final MediaPlayer mp = new MediaPlayer();
            // 置为初始状态
            mp.reset();
            // 设置文件路径
            mp.setDataSource(url);
            // 准备(缓冲) ,但是不知道什么时候缓冲完成
            mp.prepare();
            // 设置缓冲完成监听(当缓冲完成的时候,调用该监听器)
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    finishPlaying(mp);
                }
            });
            return mp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<XCAudioModel> getAudioList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        ArrayList<XCAudioModel> list = new ArrayList<XCAudioModel>();
        XCAudioModel bean = null;
        int index = 1;
        while (cursor.moveToNext()) {
            bean = new XCAudioModel();
            bean.index = index;
            bean._ID = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            bean.url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            bean.displayname = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            bean.length = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));
            bean.MIME_TYPE = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));
            bean.DURATION = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            list.add(bean);
            index++;
        }
        return list;
    }


    public static ArrayList<XCImageModel> getImageList(Context context) {
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        ArrayList<XCImageModel> list = new ArrayList<XCImageModel>();

        XCImageModel bean = null;
        int index = 1;
        Cursor thumbCursor;
        while (cursor.moveToNext()) {
            bean = new XCImageModel();
            bean.index = index;
            bean._ID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            bean.url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            bean.displayname = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            bean.length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));

            bean.MIME_TYPE = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            bean.MINI_THUMB_MAGIC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC));

            thumbCursor = resolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + "=?", new String[]{bean._ID + ""}, null);

            if (thumbCursor.moveToNext()) {
                bean.thumbnail = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            }
            thumbCursor.close();
            list.add(bean);
            index++;
        }

        return list;
    }

    public static ArrayList<XCVideoModel> getVideoList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        ArrayList<XCVideoModel> list = new ArrayList<XCVideoModel>();
        XCVideoModel bean = null;
        int index = 1;
        Cursor cursorThubnail;
        while (cursor.moveToNext()) {
            bean = new XCVideoModel();
            bean.index = index;
            bean._ID = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Video.Media._ID));
            bean.url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.DATA));
            bean.displayname = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            bean.length = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Video.Media.SIZE));
            bean.MIME_TYPE = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
            bean.DURATION = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Video.Media.DURATION));
            // 查找对应视频的缩略表
            cursorThubnail = resolver.query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null,
                    " video_id=?", new String[]{bean._ID + ""}, null);
            if (cursorThubnail.moveToNext()) {
                bean.thumbnail = cursorThubnail.getString(cursorThubnail
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            }
            cursorThubnail.close();
            list.add(bean);
            index++;
        }
        cursor.close();
        return list;
    }

    public static class XCAudioModel implements Serializable {

        private static final long serialVersionUID = 8288878050393530019L;

        public String displayname;
        public long length;
        public String url;
        public int index = 0;
        public int _ID;
        public String MIME_TYPE;
        public long DURATION;

        @Override
        public String toString() {
            return "SongMessage [displayname=" + displayname + ", length=" + length
                    + ", url=" + url + ", index=" + index + ", isMenuShow="
                    + ", _ID=" + _ID + ", MIME_TYPE=" + MIME_TYPE + ", DURATION="
                    + DURATION + "]";
        }
    }

    public static class XCImageModel implements Serializable {
        private static final long serialVersionUID = -6847612368978583756L;

        public String displayname;
        public long length;
        public String url;

        public int index = 0;

        public int _ID;
        public String MIME_TYPE;
        public String MINI_THUMB_MAGIC;
        public String thumbnail;

        @Override
        public String toString() {
            return "ImageMessage [displayname=" + displayname + ", length="
                    + length + ", url=" + url + ", index=" + index + ", _ID=" + _ID
                    + ", MIME_TYPE=" + MIME_TYPE + ", MINI_THUMB_MAGIC="
                    + MINI_THUMB_MAGIC + ", thumbnail=" + thumbnail + "]";
        }

    }

    public static class XCVideoModel implements Serializable {

        private static final long serialVersionUID = 1410038234462714175L;

        public String displayname;
        public long length;
        public String url;
        public int index = 0;
        public int _ID;
        public String MIME_TYPE;
        public long DURATION;
        public String thumbnail;

        @Override
        public String toString() {
            return "VideoMessage [displayname=" + displayname + ", length="
                    + length + ", url=" + url + ", index=" + index + ", _ID=" + _ID
                    + ", MIME_TYPE=" + MIME_TYPE + ", DURATION=" + DURATION
                    + ", thumbnail=" + thumbnail + "]";
        }

    }
}
