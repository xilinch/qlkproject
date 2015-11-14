package com.xiaocoder.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.util.UtilAnim;
import com.xiaocoder.views.R;

/**
 * Created by xiaocoder on 2015/10/24.
 * version: 1.2.0
 * description:
 */
public class XCRotateDialog extends Dialog {
    public static int TRAN_STYLE = R.style.xc_s_dialog;

    /*
     * 如果这里使用getLayoutInflater(),则获取不到双圈的dialog，用LayoutInflater.from可以
     */
    public LayoutInflater dialogInflater;

    public ViewGroup dialogLayout;

    public Context mContext;

    ImageView imageview;
    Animation anim;
    TextView textview;

    public ImageView getImageview() {
        return imageview;
    }

    public TextView getTextview() {
        return textview;
    }

    public Animation getAnim() {
        return anim;
    }

    public XCRotateDialog(Context context, int imageViewId) {
        super(context, TRAN_STYLE);
        dialogInflater = LayoutInflater.from(context);
        mContext = context;

        initDialog(imageViewId);
    }

    public void initDialog(int imageViewId) {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_rotate_imageview, null);
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();

        textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_rotate_textview);

        anim = UtilAnim.getRatoteAnimation();

        imageview = (ImageView) dialogLayout.findViewById(R.id.xc_id_dialog_rotate_imageview);
        imageview.setImageResource(imageViewId);

    }

    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.2f;
        window.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        imageview.startAnimation(anim);
    }
}
