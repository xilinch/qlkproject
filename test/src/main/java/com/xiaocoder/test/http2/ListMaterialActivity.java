package com.xiaocoder.test.http2;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.function.adapter.XCBaseAdapter;
import com.xiaocoder.android.fw.general.http.XCHttper;
import com.xiaocoder.ptrrefresh.XCIRefreshHandler;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.middle.parse.MResponseHandlerModel;
import com.xiaocoder.ptrrefresh.XCMaterialListPinRefreshLayout;
import com.xiaocoder.test.R;
import com.xiaocoder.test.bean.TestModel;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;


public class ListMaterialActivity extends MActivity {

    TestAdatpter adapter;
    XCMaterialListPinRefreshLayout xcListRefreshLayout;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_materail);
        super.onCreate(savedInstanceState);
        // 未配置autorefresh属性
        reqeust();
    }

    @Override
    public void initWidgets() {

        adapter = new TestAdatpter(this, null);
        xcListRefreshLayout = getViewById(R.id.xc_id_refreshlayout);

        listview = (ListView) xcListRefreshLayout.getListView();

        xcListRefreshLayout.getListView().setAdapter(adapter);
        // http请求中获取，这里为模拟数据
        xcListRefreshLayout.setBgZeroHintInfo("无数据", "点击刷新", R.drawable.icon);
    }

    public static String url = "http://yyf.7lk.com/api/goods/category-goods-list?userId=399&token=c2a623a6f3c7d6e1a126f1655c13b3f0&_m=&catId=515&_v=1.0.0&page=1&num=20&ts=1438155912203&_c=&_p=android&sig=96702f0846e8cb5d2701f5e39f28ba95";

    public void reqeust() {
        XCHttper.getAsyn(true, url, new HashMap(), new MResponseHandlerModel<TestModel>(this, this, TestModel.class) {

            @Override
            public void success(int code, Header[] headers, byte[] arg2) {
                super.success(code, headers, arg2);
                if (result_boolean) {
                    List<TestModel.DataEntity.ResultEntity> result = result_bean.getData().getResult();
                    xcListRefreshLayout.setTotalPage("4");
                    result.addAll(result);
                    xcListRefreshLayout.updateListAdd(result, adapter);
                }
            }

            @Override
            public void finish() {
                super.finish();
                xcListRefreshLayout.completeRefresh(result_boolean);
            }
        });
    }

    @Override
    public void listeners() {
        xcListRefreshLayout.setHandler(new XCIRefreshHandler() {
            @Override
            public boolean canRefresh() {
                return true;
            }

            @Override
            public boolean canLoad() {
                return true;
            }

            @Override
            public void refresh(View view, int request_page) {
                reqeust();
            }

            @Override
            public void load(View view, int request_page) {
                reqeust();
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
}
