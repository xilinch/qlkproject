/**
 *
 */
package com.xiaocoder.android.fw.general.adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.imageloader.XCImageLoaderHelper;

import java.util.List;

public abstract class XLBaseAdapterExpand<E, T> extends BaseExpandableListAdapter {
    /**
     * 子item
     */
    public List<List<T>> listChild;
    /**
     * 父item
     */
    public List<E> listParaent;
    public Context context;
    public ImageLoader imageloader;
    public DisplayImageOptions options;

    /**
     * @param imageloader 如果传null 则用默认的imageloader， 该默认的为universal imageloader框架
     */
    public XLBaseAdapterExpand(Context context, List<List<T>> listChild, List<E> listParaent, ImageLoader imageloader) {
        this.listChild = listChild;
        this.listParaent = listParaent;
        this.context = context;
        this.options = XCImageLoaderHelper.getDisplayImageOptions();
        if (imageloader == null) {
            this.imageloader = XCApplication.getBase_imageloader();
        } else {
            this.imageloader = imageloader;
        }
    }

    public XLBaseAdapterExpand(Context context, List<List<T>> listChild, List<E> listParaent) {
        this(context, listChild, listParaent, null);
    }


    public void update(List<List<T>> listChild, List<E> listParaent) {
        this.listChild = listChild;
        this.listParaent = listParaent;
    }

    @Override
    public int getGroupCount() {
        if (listParaent != null) {
            return listParaent.size();
        }
        return 0;
    }

    @Override
    public E getGroup(int i) {
        if (listParaent != null) {
            return listParaent.get(i);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {

        return i;

    }

    @Override
    public int getChildrenCount(int i) {
        if (listChild != null && listChild.get(i) != null) {
            return listChild.get(i).size();
        }
        return 0;
    }


    @Override
    public T getChild(int groupPosition, int childPosition) {

        if (listChild != null && listChild.get(groupPosition) != null) {
            return listChild.get(groupPosition).get(childPosition);
        }
        return null;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;

    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;

    }


    @Override
    public boolean hasStableIds() {

        return true;

    }

    public void setViewGone(boolean isGone, View view) {
        if (context != null) {
            ((XCBaseActivity) context).setViewGone(isGone, view);
        }
    }

    public void setViewVisible(boolean isVisible, View view) {
        if (context != null) {
            ((XCBaseActivity) context).setViewVisible(isVisible, view);
        }
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        imageloader.displayImage(uri, imageView, options);
    }

    public void displayImage(String uri, ImageView imageView) {
        imageloader.displayImage(uri, imageView);
    }

}