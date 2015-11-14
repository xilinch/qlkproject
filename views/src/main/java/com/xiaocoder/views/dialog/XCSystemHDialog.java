package com.xiaocoder.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.views.R;


/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCSystemHDialog extends Dialog {
    public static int TRAN_STYLE = R.style.xc_s_dialog;

    /*
     * �������ʹ��getLayoutInflater(),���ȡ����˫Ȧ��dialog����LayoutInflater.from����
     */
    public LayoutInflater dialogInflater;

    public ViewGroup dialogLayout;

    public Context mContext;
    TextView textview;

    public TextView getTextview() {
        return textview;
    }

    public XCSystemHDialog(Context context) {
        super(context, TRAN_STYLE);
        dialogInflater = LayoutInflater.from(context);
        mContext = context;

        initDialog();
    }

    public void initDialog() {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_system_circle_h, null);
        textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_sys_h_textview);
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

}



