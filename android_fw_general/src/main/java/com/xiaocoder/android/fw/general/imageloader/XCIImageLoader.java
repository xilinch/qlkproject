package com.xiaocoder.android.fw.general.imageloader;

import android.widget.ImageView;

/**
 * Created by xiaocoder on 2015/10/15.
 */
public interface XCIImageLoader {

    void display(String url, ImageView imageview, Object... obj);

    void display(String url, ImageView imageview);

}
