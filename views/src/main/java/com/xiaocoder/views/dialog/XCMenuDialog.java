package com.xiaocoder.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.xiaocoder.views.R;

import java.util.ArrayList;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCMenuDialog extends Dialog implements View.OnClickListener {
    public static int TRAN_STYLE = R.style.xc_s_dialog;

    /*
     * 如果这里使用getLayoutInflater(),则获取不到双圈的dialog，用LayoutInflater.from可以
     */
    public LayoutInflater dialogInflater;

    public ViewGroup dialogLayout;

    public Context mContext;

    public interface OnDialogItemClickListener {
        void onClick(View view);
    }

    OnDialogItemClickListener listener;

    public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    String[] items;

    public String[] getItems() {
        return items;
    }

    public XCMenuDialog(Context context, String title, String[] items) {
        super(context, TRAN_STYLE);
        dialogInflater = LayoutInflater.from(context);
        mContext = context;

        initDialog(title, items);
    }

    public void initDialog(String title, String[] items) {
        this.items = items;

        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_items, null);

        TextView title_textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_items_title);

        title_textview.setText(title);

        if (items != null) {
            ArrayList<Button> list = new ArrayList<Button>();
            for (String item : items) {
                ViewGroup viewgroup = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_item_of_items, null);
                Button button = ((Button) viewgroup.getChildAt(1));
                button.setText(item);
                button.setOnClickListener(this);
                list.add(button);
                dialogLayout.addView(viewgroup);
            }
        }

        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }


    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.95f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }


}



