package com.xiaocoder.android.fw.general.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

public class XCSearchRecordModelDb extends SQLiteOpenHelper {

    public String mDbName;
    public int mVersion;
    public String mOperatorTableName;

    /**
     * 排序常量
     */
    public static String SORT_DESC = " DESC";
    public static String SORT_ASC = " ASC";

    /**
     * 以下是表字段
     */
    public static final String _ID = "_id";
    public static final String KEY_WORD = "key_word";
    public static final String TIME = "time";

    public static String TABLE_1 = "SEARCH_1";
    public static String TABLE_2 = "SEARCH_2";
    public static String TABLE_3 = "SEARCH_3";
    public static String TABLE_4 = "SEARCH_4";
    public static String TABLE_5 = "SEARCH_5";
    public static String TABLE_6 = "SEARCH_6";

    public XCSearchRecordModelDb(Context context, String dbName, int version, String operatorTableName) {
        super(context, dbName, null, version);
        if (UtilString.isBlank(dbName)) {
            throw new RuntimeException("数据库名不能为空");
        }

        if (operatorTableName == null || operatorTableName.length() < 1) {
            throw new RuntimeException("操作的表名不能为空");
        }
        mDbName = dbName;
        mVersion = version;
        mOperatorTableName = operatorTableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_1
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
        db.execSQL("CREATE TABLE " + TABLE_2
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
        db.execSQL("CREATE TABLE " + TABLE_3
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
        db.execSQL("CREATE TABLE " + TABLE_4
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
        db.execSQL("CREATE TABLE " + TABLE_5
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
        db.execSQL("CREATE TABLE " + TABLE_6
                + "(" + _ID + " integer primary key autoincrement,"
                + KEY_WORD + " text, "
                + TIME + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public ContentValues createContentValue(XCSearchRecordModel model) {
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, model.getKey_word());
        values.put(TIME, model.getTime());
        return values;
    }

    public XCSearchRecordModel createModel(Cursor c) {
        XCSearchRecordModel model = new XCSearchRecordModel();
        model.setKey_word(c.getString(c.getColumnIndex(KEY_WORD)));
        model.setTime(c.getString(c.getColumnIndex(TIME)));
        return model;
    }

    /**
     * 插入一条记录
     */
    public long insert(XCSearchRecordModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = createContentValue(model);
        long id = db.insert(mOperatorTableName, _ID, values);
        //XCLog.i(XCConfig.TAG_DB, "插入的记录的id是: " + id);
        db.close();
        return id;
    }

    /**
     * 删除所有记录
     */
    public int deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        int raw = db.delete(mOperatorTableName, null, null);
        db.close();
        return raw;
    }

    /**
     * 根据时间删除记录
     */
    //TODO 指定你的查找的字段，替换KEY_FIELD
    public int deleteByTime(String value) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mOperatorTableName, TIME + "=?", new String[]{value + ""});
        //XCLog.i(XCConfig.TAG_DB, "delete-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 根据关键字，删除记录
     */
    //TODO 指定你的查找的字段，替换KEY_FIELD
    public int deleteByKeyword(String value) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mOperatorTableName, KEY_WORD + "=?", new String[]{value + ""});
        //XCLog.i(XCConfig.TAG_DB, "delete-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 查询共有多少条记录
     */
    public int queryCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, new String[]{"COUNT(*)"}, null, null, null, null, null);
        c.moveToNext();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    /**
     * 查询所有
     */
    public List<XCSearchRecordModel> queryAllByDESC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, _ID + SORT_DESC); // 条件为null可以查询所有
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询所有
     */
    public List<XCSearchRecordModel> queryAllByASC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, _ID + SORT_ASC); // 条件为null可以查询所有
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 根据关键字查询
     */
    //TODO 指定你的查找的字段，替换KEY_FIELD
    public List<XCSearchRecordModel> query(String value) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, KEY_WORD + "=?", new String[]{value}, null, null, null);
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 分页查找
     */
    public List<XCSearchRecordModel> queryPage(int pageNum, int capacity) {
        String offset = (pageNum - 1) * capacity + ""; // 偏移量
        String len = capacity + ""; // 个数
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, null, offset + "," + len);
        List<XCSearchRecordModel> beans = new ArrayList<XCSearchRecordModel>();
        while (c.moveToNext()) {
            XCSearchRecordModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    //TODO 指定你的修改的字段，替换KEY_FIELD
    public int update(XCSearchRecordModel model, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = createContentValue(model);
        int rows = db.update(mOperatorTableName, values, TIME + "=?", new String[]{value + ""});
        //XCLog.i(XCConfig.TAG_DB, "更新了" + rows + "行");
        db.close();
        return rows;
    }
}