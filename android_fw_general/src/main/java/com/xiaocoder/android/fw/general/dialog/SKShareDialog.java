package com.xiaocoder.android.fw.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class SKShareDialog extends XCBaseDialog {

    public SKShareDialog(Context context, int style) {
        super(context, style);
        initDialog();
    }

    /**
     * 初始化分享dialog
     */
    public void initDialog() {

        View view = getLayoutInflater().inflate(R.layout.sk_l_dialog_share, null);

        TextView sk_id_share_wx_friend_tv;
        TextView sk_id_share_wx_friends_tv;
        TextView sk_id_share_copy_link_tv;
        TextView sk_id_share_cancel_tv;

        sk_id_share_wx_friend_tv = (TextView) view.findViewById(R.id.sk_id_share_wx_friend_tv);
        sk_id_share_wx_friends_tv = (TextView) view.findViewById(R.id.sk_id_share_wx_friends_tv);
        sk_id_share_copy_link_tv = (TextView) view.findViewById(R.id.sk_id_share_copy_link_tv);
        sk_id_share_cancel_tv = (TextView) view.findViewById(R.id.sk_id_share_cancel_tv);

        sk_id_share_wx_friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信好友
                if (shareListener != null) {
                    shareListener.shareFriend();
                }
                dismiss();
            }
        });
        sk_id_share_wx_friends_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享到微信朋友圈
                if (shareListener != null) {
                    shareListener.shareFriends();
                }
                dismiss();

            }
        });
        sk_id_share_copy_link_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制链接
                if (shareListener != null) {
                    shareListener.copyLink();
                }
                dismiss();

            }
        });
        sk_id_share_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dismiss();
            }
        });
        setContentView(view);
        setWindowLayoutStyle();
    }

    public void setWindowLayoutStyle() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
    }

    public interface OnShareListener {

        void shareFriend();

        void shareFriends();

        void copyLink();
    }

    public OnShareListener shareListener;

    public void setOnShareListener(OnShareListener listener) {
        shareListener = listener;
    }

}



