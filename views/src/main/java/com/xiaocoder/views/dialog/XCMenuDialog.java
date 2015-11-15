package com.xiaocoder.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.function.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCMenuDialog extends Dialog {

    public interface OnDialogItemClickListener {
        void onClick(View view, String hint);
    }

    public static int TRAN_STYLE = R.style.xc_s_dialog;

    /*
     * 如果这里使用getLayoutInflater(),则获取不到双圈的dialog，用LayoutInflater.from可以
     */
    public LayoutInflater dialogInflater;

    public ViewGroup dialogLayout;

    public Context mContext;

    OnDialogItemClickListener listener;

    public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    ListView listview;
    TextView title_textview;

    public XCMenuDialog(Context context) {
        super(context, TRAN_STYLE);
        dialogInflater = LayoutInflater.from(context);
        mContext = context;
        adapter = new ItemsAdapter(mContext, null);
        initDialog();
    }

    public void update(String title, String[] items) {

        if (UtilString.isBlank(title)) {
            title_textview.setVisibility(View.GONE);
        } else {
            title_textview.setText(title);
            title_textview.setVisibility(View.VISIBLE);
        }

        adapter.update(getList(items));
        listview.setAdapter(adapter);
    }

    public class ItemsAdapter extends XCBaseAdapter<String> implements View.OnClickListener{

        public ItemsAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onClick(view, ((Button) view).getText().toString().trim());
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            bean = list.get(position);
            ItemHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.xc_l_dialog_item_of_items, null);
                holder = new ItemHolder(convertView);
                holder.item_button.setOnClickListener(this);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolder) convertView.getTag();
            }
            holder.item_button.setText(bean);
            return convertView;
        }

        class ItemHolder {
            Button item_button;

            public ItemHolder(View convertView) {
                item_button = (Button) convertView.findViewById(R.id.item_button);
            }
        }
    }

    public ItemsAdapter adapter;

    public void initDialog() {
        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_items, null);
        title_textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_items_title);
        listview = (ListView) dialogLayout.findViewById(R.id.xc_id_dialog_items_listview);
        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public List<String> getList(String[] items) {
        ArrayList<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item);
        }
        return list;
    }


    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.95f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }


}



