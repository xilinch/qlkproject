package com.xiaocoder.android.fw.general.db.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.db.helper.XCDbHelper;
import com.xiaocoder.android.fw.general.model.XCSearchRecordModel;
import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

public class XCSearchDao {

    private XCDbHelper mHelper;
    private String mTabName;

    public static String KEY_WORD = "keyword";
    public static String TIME = "time";
    public static String _ID = "_id";

    public static String SORT_DESC = " DESC";
    public static String SORT_ASC = " ASC";


    public XCSearchDao(Context context, XCDbHelper helper, String tabName) {

        mHelper = helper;

        if (UtilString.isBlank(tabName)) {
            throw new RuntimeException("new dao时，数据库表名不能为空");
        } else {
            mTabName = tabName;
        }

    }

    public void insert(XCSearchRecordModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, bean.getKey_word());
        values.put(TIME, bean.getTime());
        long id = db.insert(mTabName, _ID, values);
        XCApplication.printi("插入的记录的id是: " + id);
        db.close();
    }

    public int delete_unique(String time) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, TIME + "=?",
                new String[]{time + ""});
        XCApplication.printi("delete_unique-->" + rows + "行");
        db.close();
        return rows;
    }

    public int delete(String keyword) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.delete(mTabName, KEY_WORD + "=?",
                new String[]{keyword + ""});
        XCApplication.printi("delete-->" + rows + "行");
        db.close();
        return rows;
    }

    public int deleteAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int raw = db.delete(mTabName, null, null);
        db.close();
        return raw;
    }


    public int update(XCSearchRecordModel bean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, bean.getKey_word());
        values.put(TIME, bean.getTime());
        int rows = db.update(mTabName, values, TIME + "=?",
                new String[]{bean.getTime() + ""});
        XCApplication.printi("更新了" + rows + "行");
        db.close();
        return rows;
    }

    public List<XCSearchRecordModel> queryAll(String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, null, null, null, null,
                _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
                    .getColumnIndex(KEY_WORD)), c.getString(c
                    .getColumnIndex(TIME)));
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    public List<XCSearchRecordModel> query(String keyword, String sort) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, KEY_WORD + "=?",
                new String[]{keyword + ""}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
                    .getColumnIndex(KEY_WORD)), c.getString(c
                    .getColumnIndex(TIME)));
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    public int queryCount() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, new String[]{"COUNT(*)"},
                null, null, null, null, null);
        c.moveToNext();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    public List<XCSearchRecordModel> queryPage(int pageNum, int capacity) {
        String offset = (pageNum - 1) * capacity + ""; // 偏移量
        String len = capacity + ""; // 个数
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.query(mTabName, null, null, null, null, null,
                null, offset + "," + len);
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = new XCSearchRecordModel(c.getString(c
                    .getColumnIndex(KEY_WORD)), c.getString(c
                    .getColumnIndex(TIME)));
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    //
    // // 事务这里还是用execSQL方便点
    // public void remit(int from, int to, int amount) {
    // SQLiteDatabase qlk_db = helper.getWritableDatabase();
    // try {
    // qlk_db.beginTransaction(); // 开始事务
    // qlk_db.execSQL("UPDATE person SET balance=balance-? WHERE id=?", new
    // Object[] { amount, from });
    // qlk_db.execSQL("UPDATE person SET balance=balance+? WHERE id=?", new
    // Object[] { amount, to });
    // qlk_db.setTransactionSuccessful(); // 设置成功点, 在事务结束时, 成功点之前的操作会被提交
    // } finally {
    // qlk_db.endTransaction(); // 结束事务, 将成功点之前的操作提交
    // qlk_db.close();
    // }
    // }


}