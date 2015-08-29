package com.xiaocoder.test.http2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.xiaocoder.android.fw.general.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.base.abslist.XCBaseAbsListFragment.OnAbsListItemClickListener;
import com.xiaocoder.android.fw.general.base.abslist.XCBaseAbsListFragment.OnRefreshNextPageListener;
import com.xiaocoder.android.fw.general.dialog.XCBaseDialog;
import com.xiaocoder.android.fw.general.dialog.XCSystemVDialog;
import com.xiaocoder.android.fw.general.fragment.XCListViewFragment;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android.fw.general.util.UtilCommon;
import com.xiaocoder.buffer.QlkActivity;
import com.xiaocoder.buffer.function.QlkMainActivity;
import com.xiaocoder.buffer.parse.QlkResponseHandlerBean;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestBean;

import org.apache.http.Header;

import java.util.List;


public class ListActivity extends QlkActivity {
    XCListViewFragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
    }

    public void request() {

        RequestParams params = new RequestParams();
//        XCHttpAsyn.getAsyn(true, this, "http://" + MainActivity.TEST_HOST + ":8080/qlktest/listdata.json", params, new QlkHttpResponseHandler(this, list_fragment) {
        XCHttpAsyn.getAsyn(true, this,
                "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95"
                , params,
                new QlkResponseHandlerBean<TestBean>(this, TestBean.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);
                        if (result_boolean) {

                            if (!list_fragment.checkGoOn()) {
                                return;
                            }

                            TestBean testBean = result_bean.getModel(result_bean.data);

                            List<TestBean> testBeans = testBean.getList(testBean.result);

                            if (!UtilCommon.isListBlank(testBeans)) {
                                TestBean bean = testBeans.get(0);

                                XCApplication.printi(bean.getString(bean.commission));
                                XCApplication.printi(bean.getString(bean.imgUrl));
                                XCApplication.printi(bean.getString(bean.marketPrice));
                                XCApplication.printi(bean.getString(bean.rebate));
                                XCApplication.printi(bean.getString(bean.proudctId));


                                XCApplication.printi(bean.getModel(bean.share).toString());
                                XCApplication.printi(bean.getModel(bean.share).getString(bean.title));
                                XCApplication.printi(bean.getModel(bean.share).getString(bean.icon));
                                XCApplication.printi(bean.getModel(bean.share).getString(bean.content));
                            }

                            // grid_fragment.setTotalNum("100");// 或者setTotalPage也可以
                            list_fragment.setTotalPage("3");
                            list_fragment.updateList(testBeans);
                        }
                    }

                    @Override
                    public void finish() {
                        super.finish();
                        if (list_fragment != null) {
                            list_fragment.completeRefresh();
                        }
                    }

                    // 更换一个dialog样式
                    @Override
                    public void showHttpDialog() {
                        if (httpDialog == null) {
                            httpDialog = new XCSystemVDialog(mContext, XCBaseDialog.TRAN_STYLE);
                            httpDialog.setCanceledOnTouchOutside(false);
                            httpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        closeHttpDialog();
                                        XCHttpAsyn.resetNetingStatus();
                                        if (!(mContext instanceof QlkMainActivity)) {
                                            ((XCBaseActivity) mContext).myFinish();
                                        }
                                    }
                                    return false;
                                }
                            });
                            httpDialog.show();
                            XCApplication.printi("showHttpDialog()");
                        }
                    }
                });
    }

    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {
        request();
    }

    class TestAdatpter extends XCBaseAdapter<TestBean> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            bean = list.get(position);
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_test_item, null);
                holder = new ViewHolder();
                holder.xc_id_adapter_test_textview = (TextView) convertView.findViewById(R.id.xc_id_adapter_test_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 获取和设置控件的显示值
            holder.xc_id_adapter_test_textview.setText(bean.getString(bean.commission));
            // 加载图片
            return convertView;
        }

        public TestAdatpter(Context context, List<TestBean> list) {
            super(context, list);
        }

        class ViewHolder {
            TextView xc_id_adapter_test_textview;
        }
    }

    @Override
    public void initWidgets() {
        list_fragment = new XCListViewFragment();
        list_fragment.setAdapter(new TestAdatpter(this, null));
        // list_fragment.setAdapter(new XCAdapterTest(this, null));
        // 可以不设置Mode， 默认是不可以拉的listview
        list_fragment.setMode(XCListViewFragment.MODE_UP_DOWN);
        list_fragment.setBgZeroHintInfo("数据为0", "重新加载", R.drawable.xc_d_chat_face);
        list_fragment.setListViewStyleParam(false);
        addFragment(R.id.xc_id_model_content, list_fragment);
        request();
    }

    @Override
    public void listeners() {
        // 设置背景为0的监听
        list_fragment.setOnBgZeroButtonClickToDoListener(new XCListViewFragment.OnBgZeroButtonClickToDoListener() {

            @Override
            public void onBgZeroButtonClickToDo() {
                request();
            }
        });

        list_fragment.setOnRefreshNextPageListener(new OnRefreshNextPageListener() {

            @Override
            public void onRefreshNextPageListener(int current_page) {
                request();
            }
        });

        list_fragment.setOnListItemClickListener(new OnAbsListItemClickListener() {

            @Override
            public void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                XCApplication.dShortToast(arg2 + "");
                XCApplication.printi(arg2 + "");
            }

        });
    }

}
