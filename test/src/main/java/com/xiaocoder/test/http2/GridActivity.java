package com.xiaocoder.test.http2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.function.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.http.XCHttper;
import com.xiaocoder.android.fw.general.util.UtilCollections;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.parse.MResponseHandlerModel;
import com.xiaocoder.pulltorefresh.XCGridViewFragment;
import com.xiaocoder.pulltorefresh.XCListViewFragment;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;


public class GridActivity extends MActivity {
    XCGridViewFragment grid_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
    }

    public static String url = "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95";

    public void request() {

        HashMap<String,Object> params =  new HashMap<String,Object>();
        XCHttper.getAsyn(true, url, params,
                new MResponseHandlerModel<TestModel>(null, this, TestModel.class) {
                    @Override
                    public void success(int code, Header[] headers, byte[] arg2) {
                        super.success(code, headers, arg2);

                        if (result_boolean) {
                            if (!grid_fragment.checkGoOn()) {
                                return;
                            }

                            TestModel.DataEntity data = result_bean.getData();

                            List<TestModel.DataEntity.ResultEntity> result = data.getResult();

                            XCLog.i(result_bean.toString());
                            XCLog.i(result_bean.getMsg());
                            XCLog.i(result_bean.getCode() + "");
                            XCLog.i(result_bean.getData().getTotalCount() + "");
                            XCLog.i(result_bean.getData().getTotalPages() + "");

                            if (!UtilCollections.isListBlank(result)) {
                                XCLog.i(result_bean.getData().getResult().toString() + "");
                                XCLog.i(result_bean.getData().getResult().get(0).getCommission() + "");
                                XCLog.i(result_bean.getData().getResult().get(0).getImgUrl() + "");
                                XCLog.i(result_bean.getData().getResult().get(0).getMarketPrice() + "");
                                XCLog.i(result_bean.getData().getResult().get(0).getName() + "");
                                XCLog.i(result_bean.getData().getResult().get(1).getRebate() + "");
                                XCLog.i(result_bean.getData().getResult().get(2).getType() + "");
                                XCLog.i(result_bean.getData().getResult().get(2).getShare().getBaseUrl() + "");
                                XCLog.i(result_bean.getData().getResult().get(2).getShare().getContent() + "");
                                XCLog.i(result_bean.getData().getResult().get(2).getShare().getTitle() + "");
                                XCLog.i(result_bean.getData().getResult().get(2).getShare().getIcon() + "");
                                XCLog.i(result_bean.getData().getResult().get(1).getImgUrl());
                            }

                            // grid_fragment.setTotalNum("100");// 或者setTotalPage也可以
                            // grid_fragment.setTotalPage(totalnum % PER_PAGE_NUM == 0 ? totalnum/PER_PAGE_NUM :(totalnum / PER_PAGE_NUM) + 1)
                            grid_fragment.setTotalPage("3");
                            grid_fragment.updateList(result);
                        }

                    }

                    @Override
                    public void finish() {
                        super.finish();
                        if (grid_fragment != null) {
                            grid_fragment.completeRefresh();
                        }
                    }
                });
    }

    class TestAdatpter extends XCBaseAdapter<TestModel.DataEntity.ResultEntity> {
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
            holder.xc_id_adapter_test_textview.setText(bean.getCommission());
            // 加载图片
            return convertView;
        }

        public TestAdatpter(Context context, List<TestModel.DataEntity.ResultEntity> list) {
            super(context, list);
        }

        class ViewHolder {
            TextView xc_id_adapter_test_textview;

        }
    }

    @Override
    public void initWidgets() {
        grid_fragment = new XCGridViewFragment();
        grid_fragment.setAdapter(new TestAdatpter(this, null));
//        grid_fragment.setAdapter(new XCAdapterTest(this, null));
        // 可以不设置Mode， 默认是不可以拉的listview
        grid_fragment.setMode(XCListViewFragment.MODE_UP_DOWN);
        grid_fragment.setBgZeroHintInfo("数据为0", "重新加载", R.drawable.ic_launcher);
        grid_fragment.setGridViewStyleParam(false, 1, 1, 2);
        addFragment(R.id.xc_id_model_content, grid_fragment);
        request();
    }

    @Override
    public void listeners() {
        // 设置背景为0的监听
        grid_fragment.setOnBgZeroButtonClickToDoListener(new XCListViewFragment.OnBgZeroButtonClickToDoListener() {

            @Override
            public void onBgZeroButtonClickToDo() {
                request();
            }
        });

        grid_fragment.setOnRefreshNextPageListener(new XCGridViewFragment.OnRefreshNextPageListener() {

            @Override
            public void onRefreshNextPageListener(int current_page) {
                request();
            }
        });

        grid_fragment.setOnListItemClickListener(new XCGridViewFragment.OnAbsListItemClickListener() {

            @Override
            public void onAbsListItemClickListener(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                XCLog.dShortToast(arg2 + "");
                XCLog.i(arg2 + "");
            }
        });
    }

}
