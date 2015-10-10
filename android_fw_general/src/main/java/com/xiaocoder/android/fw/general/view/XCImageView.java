package com.xiaocoder.android.fw.general.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiaocoder.android_fw_general.R;

/**
 * 用于适配用的 ， 图片可以等比例拉伸
 * xmlns:xiaocoder="http://schemas.android.com/apk/res-auto"
 * android:scaleType="fitXY"
 * xiaocoder:xc_radio_for_width_height="1.8902"   -->height = width / 1.8902
 */
public class XCImageView extends ImageView {

    String radio;

    public XCImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取系统解析得到的所有属性值
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.XCImageView);

        radio = typeArray.getString(R.styleable.XCImageView_xc_radio_for_width_height);

        typeArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (radio != null && !"".equals(radio)) {
            // 如果有设置比例
            // 获取比例
            float radio_float = Float.parseFloat(radio);
            // 按照比例计算高
            int height_size = (int) (MeasureSpec.getSize(widthMeasureSpec) / radio_float);
            // 测量 ， 宽度和以前是一个的 ， 长度变化了，重新计算长度的详细测量值
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height_size, MeasureSpec.EXACTLY));
        } else {
            // 没有设置比例
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
