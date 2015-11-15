package com.xiaocoder.test.http2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.function.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.tool.XC;
import com.xiaocoder.views.dialog.XCSystemVDialog;
import com.xiaocoder.android.fw.general.util.UtilCollections;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.parse.MResponseHandlerBean;
import com.xiaocoder.pulltorefresh.XCListViewFragment;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestBean;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;


public class ListActivity extends MActivity {
    XCListViewFragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
    }

    public static String url = "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95";

    public void request() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        XC.getAsyn(true, url, params,
                new MResponseHandlerBean<TestBean>(this, this, TestBean.class) {

                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);
                        if (result_boolean) {

                            if (!list_fragment.checkGoOn()) {
                                return;
                            }

                            TestBean testBean = result_bean.getModel(result_bean.data);
                            XC.i(testBean);

                            List<TestBean> testBeans = testBean.getList(testBean.result);

                            if (!UtilCollections.isListBlank(testBeans)) {
                                TestBean bean = testBeans.get(0);

                                XC.i(bean.getString(bean.commission));
                                XC.i(bean.getString(bean.imgUrl));
                                XC.i(bean.getString(bean.marketPrice));
                                XC.i(bean.getString(bean.rebate));
                                XC.i(bean.getString(bean.proudctId));

                                XC.i(bean.getModel(bean.share).toString());
                                XC.i(bean.getModel(bean.share).getString(bean.title));
                                XC.i(bean.getModel(bean.share).getString(bean.icon));
                                XC.i(bean.getModel(bean.share).getString(bean.content));
                            }

                            // grid_fragment.setTotalNum("100");// 或者setTotalPage也可以
                            // grid_fragment.setTotalPage(totalnum % PER_PAGE_NUM == 0 ? totalnum/PER_PAGE_NUM :(totalnum / PER_PAGE_NUM) + 1)

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
                        setDialogAndShow(new XCSystemVDialog(activity));
                    }
                });
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
        list_fragment.setBgZeroHintInfo("数据为0", "重新加载", R.drawable.ic_launcher);
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

        list_fragment.setOnRefreshNextPageListener(new XCListViewFragment.OnRefreshNextPageListener() {

            @Override
            public void onRefreshNextPageListener(int current_page) {
                request();
            }
        });

        list_fragment.setOnListItemClickListener(new XCListViewFragment.OnAbsListItemClickListener() {

            @Override
            public void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                XC.dShortToast(arg2 + "");
                XC.i(arg2 + "");
            }

        });
    }

}
