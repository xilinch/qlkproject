package com.xiaocoder.android.fw.general.exception;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiaocoder.android.fw.general.util.UtilString;

import java.util.ArrayList;
import java.util.List;

public class XCExceptionModelDb extends SQLiteOpenHelper {

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
    public static final String INFO = "info";
    public static final String EXCEPTIONTIME = "exceptionTime";
    public static final String UPLOADSUCCESS = "uploadSuccess";
    public static final String USERID = "userId";
    public static final String UNIQUEID = "uniqueId";

    public XCExceptionModelDb(Context context, String dbName, int version, String operatorTableName) {
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

        db.execSQL("CREATE TABLE " + mOperatorTableName
                + "(" + _ID + " integer primary key autoincrement,"
                + INFO + " text, "
                + EXCEPTIONTIME + " text, "
                + UPLOADSUCCESS + " text, "
                + USERID + " text, "
                + UNIQUEID + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public ContentValues createContentValue(XCExceptionModel model) {
        ContentValues values = new ContentValues();
        values.put(INFO, model.getInfo());
        values.put(EXCEPTIONTIME, model.getExceptionTime());
        values.put(UPLOADSUCCESS, model.getUploadSuccess());
        values.put(USERID, model.getUserId());
        values.put(UNIQUEID, model.getUniqueId());
        return values;
    }

    public XCExceptionModel createModel(Cursor c) {
        XCExceptionModel model = new XCExceptionModel();
        model.setInfo(c.getString(c.getColumnIndex(INFO)));
        model.setExceptionTime(c.getString(c.getColumnIndex(EXCEPTIONTIME)));
        model.setUploadSuccess(c.getString(c.getColumnIndex(UPLOADSUCCESS)));
        model.setUserId(c.getString(c.getColumnIndex(USERID)));
        model.setUniqueId(c.getString(c.getColumnIndex(UNIQUEID)));
        return model;
    }

    /**
     * 插入一条记录
     */
    public long insert(XCExceptionModel model) {
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

    //TODO 指定你的查找的字段，替换KEY_FIELD
    public int delete(String value) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mOperatorTableName, UNIQUEID + "=?", new String[]{value + ""});
        //XCLog.i(XCConfig.TAG_DB, "delete-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 删除所有上传成功了的异常信息，“1”为上传成功，“0”为未上传或失败
     */
    public int deleteUploadSuccess() {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mOperatorTableName, UPLOADSUCCESS + "=?", new String[]{XCExceptionModel.UPLOAD_YES});
        //XCLog.i(XCConfig.TAG_DB, "delete_uploadSuccess-->" + rows + "行");
        db.close();
        return rows;
    }

    /**
     * 删除与该用户有关的所有异常
     */
    public int deleteUserid(String userId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(mOperatorTableName, USERID + "=?", new String[]{userId + ""});
        // XCLog.i(XCConfig.TAG_DB, "delete_userid-->" + rows + "行");
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
    public List<XCExceptionModel> queryAllByDESC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, _ID + SORT_DESC); // 条件为null可以查询所有
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询所有
     */
    public List<XCExceptionModel> queryAllByASC() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, _ID + SORT_ASC); // 条件为null可以查询所有
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    //TODO 指定你的查找的字段，替换KEY_FIELD
    public List<XCExceptionModel> queryUnique(String value) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, UNIQUEID + "=?", new String[]{value}, null, null, null);
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询上传成功的记录
     */
    public List<XCExceptionModel> queryUploadSuccess(String sort) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, UPLOADSUCCESS + "=?",
                new String[]{XCExceptionModel.UPLOAD_YES}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 查询上传失败的记录
     */
    public List<XCExceptionModel> queryUploadFail(String sort) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, UPLOADSUCCESS + "=?",
                new String[]{XCExceptionModel.UPLOAD_NO}, null, null, _ID + sort); // 条件为null可以查询所有,见api;ORDER
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    /**
     * 分页查找
     */
    public List<XCExceptionModel> queryPage(int pageNum, int capacity) {
        String offset = (pageNum - 1) * capacity + ""; // 偏移量
        String len = capacity + ""; // 个数
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(mOperatorTableName, null, null, null, null, null, null, offset + "," + len);
        List<XCExceptionModel> beans = new ArrayList<XCExceptionModel>();
        while (c.moveToNext()) {
            XCExceptionModel bean = createModel(c);
            beans.add(bean);
        }
        c.close();
        db.close();
        return beans;
    }

    //TODO 指定你的修改的字段，替换KEY_FIELD
    public int update(XCExceptionModel model, String value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = createContentValue(model);
        int rows = db.update(mOperatorTableName, values, UNIQUEID + "=?", new String[]{value + ""});
        //XCLog.i(XCConfig.TAG_DB, "更新了" + rows + "行");
        db.close();
        return rows;
    }

}