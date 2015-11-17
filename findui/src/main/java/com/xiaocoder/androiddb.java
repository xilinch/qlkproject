package com.xiaocoder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class androiddb {

	/**
	 * 类名
	 */
	public static String createFileName = "";
	/**
	 * model名
	 */
	public static String createModelName = "";

	public static File file;

	public static LinkedHashMap<String, String> mapFileds = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> getMethods = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> setMethods = new LinkedHashMap<String, String>();

	public static JFrame frame;
	public static JTextField textfield;
	public static JButton button;
	public static JTextArea area;
	public static JScrollPane scrollPane;
	public static String ENCODING = "utf-8";

	public static void main(String[] args) {

		frame = new JFrame("生成android sql文件");
		textfield = new JTextField();
		button = new JButton("开始");
		area = new JTextArea(30, 100);
		scrollPane = new JScrollPane(area);

		frame.setBounds(200, 100, 1000, 600);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(button, BorderLayout.EAST);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area.setText("请输入Model的绝对路径,该model必须有一个无参的构造器 , androidStudio为ctrl+shift+c");

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String content = getModelFileString(area.getText().toString()
						.trim());

				getClassName();

				getClassField(content);

				String result = getDBFile();
				area.setText("");
				area.setText(result);
			}
		});
	}

	private static String getDBFile() {

		String temp = " class "
				+ createFileName
				+ " extends SQLiteOpenHelper {\n"
				+ "\n"
				+ "    public String mDbName;\n"
				+ "    public int mVersion;\n"
				+ "    public String mOperatorTableName;\n"
				+ "\n"
				+ "	   /** 排序常量 */ \n"
				+ "	   public static String SORT_DESC = \" DESC\";\n"// 有个空格
				+ "	   public static String SORT_ASC = \" ASC\";"// 有个空格
				+ getFieldsString()
				+ "\n"
				+ "    public "
				+ createFileName
				+ " (Context context, String dbName, int version, String operatorTableName) {\n"
				+ "        super(context, dbName, null, version);\n"
				+ "        if (UtilString.isBlank(dbName)) {\n"
				+ "            throw new RuntimeException(\"数据库名不能为空\");\n"
				+ "        }\n"
				+ "\n"
				+ "        if (operatorTableName == null || operatorTableName.length() < 1) {\n"
				+ "            throw new RuntimeException(\"操作的表名不能为空\");\n"
				+ "        }\n"
				+ "        mDbName = dbName;\n"
				+ "        mVersion = version;\n"
				+ "        mOperatorTableName = operatorTableName;\n"
				+ "    }\n"
				+ "\n"
				+ "    @Override\n"
				+ "    public void onCreate(SQLiteDatabase db) {\n"
				+ "\n"
				+ getSql()
				+ "\n"
				+ "    }\n"
				+ "\n"
				+ "    @Override\n"
				+ "    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {\n\n"
				+ "    }\n \n"
				// + "\n"
				// + "    public static "
				// + createFileName
				// +
				// " instanceHelper(Context context, Class<? extends SQLiteOpenHelper> dbHelper, String dbName,\n"
				// +
				// "                                            int version, String operatorTableName ) {\n"
				// + "        try {\n"
				// +
				// "            // XCLog.i(XCConfig.TAG_DB, \"dbHelper----instanceHelper()\");\n"
				// +
				// "            Constructor constructor = dbHelper.getConstructor(Context.class, String.class, int.class, String.class);\n"
				// +
				// "            Object o = constructor.newInstance(context, dbName, version, operatorTableName);\n"
				// + "            return (" + createFileName + ") o;\n"
				// + "        } catch (Exception e) {\n"
				// + "            e.printStackTrace();\n"
				// + "            // XCLog.e(context, \"\", e);\n"
				// + "            return null;\n" + "        }\n" + "    }\n"
				+ getContentValue() + "\n" + createModel() + "\n" + insert()
				+ deleteAll() + delete() + queryCount() + queryAllByDESC()
				+ queryAllByASC() + queryUnique() + queryPage() + update()
				+ "}";

		return temp;

	}

	private static String update() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("//TODO 指定你的修改的字段，替换KEY_FIELD \n");
		sb.append("public int update(" + createModelName
				+ " model,String value) {\n");
		sb.append("SQLiteDatabase db = getWritableDatabase();\n");
		sb.append("ContentValues values = createContentValue(model);\n");
		sb.append("int rows = db.update(mOperatorTableName, values, KEY_FIELD + \"=?\",new String[]{value + \"\"});\n");
		sb.append("	//XCLog.i(XCConfig.TAG_DB, \"更新了\" + rows + \"行\");\n");
		sb.append("db.close();\n");
		sb.append("return rows;\n}\n");
		return sb.toString();

	}

	private static String queryPage() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 分页查找 */ \n");
		sb.append(" public List<" + createModelName
				+ "> queryPage(int pageNum, int capacity){\n");
		sb.append("String offset = (pageNum - 1) * capacity + \"\"; // 偏移量\n");
		sb.append(" String len = capacity + \"\"; // 个数\n");
		sb.append("SQLiteDatabase db = getReadableDatabase();\n");
		sb.append("Cursor c = db.query(mOperatorTableName, null, null, null, null, null,null, offset + \",\" + len);\n");
		sb.append("List<" + createModelName + "> beans = new ArrayList<"
				+ createModelName + ">();\n");
		sb.append("while (c.moveToNext()) {\n");
		sb.append(createModelName + " bean = createModel(c);\n");
		sb.append("beans.add(bean);\n");
		sb.append("}\n");
		sb.append("c.close();\n");
		sb.append("db.close();\n");
		sb.append("return beans;\n}\n");
		return sb.toString();

	}

	private static String queryUnique() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append("//TODO 指定你的查找的字段，替换KEY_FIELD \n");
		sb.append(" public List<" + createModelName
				+ "> queryUnique(String value) {\n");
		sb.append("SQLiteDatabase db = getReadableDatabase();\n");
		sb.append("Cursor c = db.query(mOperatorTableName, null, KEY_FIELD + \"=?\", new String[]{value}, null, null, null);\n");
		sb.append("List<" + createModelName + "> beans = new ArrayList<"
				+ createModelName + ">();\n");
		sb.append("while (c.moveToNext()) {\n");
		sb.append(createModelName + " bean = createModel(c);\n");
		sb.append("beans.add(bean);\n");
		sb.append("}\n");
		sb.append("c.close();\n");
		sb.append("db.close();\n");
		sb.append("return beans;\n}\n");
		return sb.toString();

	}

	private static String queryAllByDESC() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 查询所有*/ \n");
		sb.append(" public List<" + createModelName + "> queryAllByDESC() {\n");
		sb.append("SQLiteDatabase db = getReadableDatabase();\n");
		sb.append("Cursor c = db.query(mOperatorTableName, null, null, null, null, null,_ID + SORT_DESC); // 条件为null可以查询所有\n");
		sb.append("List<" + createModelName + "> beans = new ArrayList<"
				+ createModelName + ">();\n");

		sb.append("while (c.moveToNext()) {\n");
		sb.append(createModelName + " bean = createModel(c);\n");
		sb.append("beans.add(bean);\n");
		sb.append("}\n");
		sb.append("c.close();\n");
		sb.append("db.close();\n");
		sb.append("return beans;\n}\n");
		return sb.toString();
	}

	private static String queryAllByASC() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 查询所有*/ \n");
		sb.append(" public List<" + createModelName + "> queryAllByASC() {\n");
		sb.append("SQLiteDatabase db = getReadableDatabase();\n");
		sb.append("Cursor c = db.query(mOperatorTableName, null, null, null, null, null,_ID + SORT_ASC); // 条件为null可以查询所有\n");
		sb.append("List<" + createModelName + "> beans = new ArrayList<"
				+ createModelName + ">();\n");

		sb.append("while (c.moveToNext()) {\n");
		sb.append(createModelName + " bean = createModel(c);\n");
		sb.append("beans.add(bean);\n");
		sb.append("}\n");
		sb.append("c.close();\n");
		sb.append("db.close();\n");
		sb.append("return beans;\n}\n");
		return sb.toString();
	}

	private static String queryCount() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 查询共有多少条记录 */ \n");
		sb.append("public int queryCount() {\n");
		sb.append("SQLiteDatabase db = getReadableDatabase();\n");
		sb.append("Cursor c = db.query(mOperatorTableName, new String[]{\"COUNT(*)\"},null, null, null, null, null);\n");
		sb.append("c.moveToNext();\n");
		sb.append("int count = c.getInt(0);\n");
		sb.append("c.close();\n");
		sb.append("db.close();\n");
		sb.append("return count;\n}\n");
		return sb.toString();

	}

	private static String deleteAll() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 删除所有记录 */ \n");
		sb.append("public int deleteAll() {\n");
		sb.append("SQLiteDatabase db = getWritableDatabase();\n");
		sb.append("int raw = db.delete(mOperatorTableName, null, null);\n");
		sb.append("db.close();\n");
		sb.append("return raw;\n}\n");
		return sb.toString();
	}

	public static String delete() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("//TODO 指定你的查找的字段，替换KEY_FIELD \n");
		sb.append("public int delete(String value) {\n");
		sb.append("SQLiteDatabase db = getWritableDatabase();\n");
		sb.append("int rows = db.delete(mOperatorTableName, KEY_FIELD + \"=?\",new String[]{value + \"\"});\n");
		sb.append("	//XCLog.i(XCConfig.TAG_DB, \"delete-->\" + rows + \"行\");\n");
		sb.append("db.close();\n");
		sb.append("return rows;\n}\n");
		return sb.toString();
	}

	private static String insert() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append("/** 插入一条记录 */ \n");
		sb.append("public long insert(" + createModelName + " model) {\n");
		sb.append("SQLiteDatabase db = getWritableDatabase();\n");
		sb.append("ContentValues values = createContentValue(model);\n");
		sb.append("long id = db.insert(mOperatorTableName, _ID, values);\n");
		sb.append("	//XCLog.i(XCConfig.TAG_DB, \"插入的记录的id是: \" + id);\n");
		sb.append("db.close();\n");
		sb.append("return id;\n}\n");
		return sb.toString();
	}

	private static String createModel() {

		StringBuilder sb = new StringBuilder("\n");
		sb.append("	public "+createModelName+" createModel(Cursor c){\n");
		sb.append("	" + createModelName + " model = new " + createModelName
				+ "();\n");
		for (Entry<String, String> entry : mapFileds.entrySet()) {
			sb.append("	model.set" + setFirstLetterBig(entry.getKey())
					+ "(c.getString(c.getColumnIndex("
					+ entry.getKey().toUpperCase() + ")));\n");
		}
		sb.append("	return model;\n	}");

		return sb.toString();
	}

	private static String getContentValue() {

		ArrayList<String> list = getPutContentValues();
		StringBuilder sb = new StringBuilder("\n");
		sb.append("	public ContentValues createContentValue(" + createModelName
				+ " model) {\n");
		sb.append("	ContentValues values = new ContentValues();\n");

		for (String str : list) {

			sb.append("	values.put(" + str + ");\n");
		}
		return sb.append("	return values;\n	}").toString();

	}

	private static ArrayList<String> getPutContentValues() {
		ArrayList<String> list = new ArrayList<>();
		for (Entry<String, String> entry : mapFileds.entrySet()) {
			list.add(entry.getValue().toUpperCase() + ", model." + "get"
					+ setFirstLetterBig(entry.getValue()) + "()");
		}
		return list;
	}

	private static String getSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("db.execSQL(");
		sb.append("\"" + "CREATE TABLE " + "\" + " + " mOperatorTableName "
				+ "\n+ \"" + "(" + "\"" + "+" + "_ID " + "+" + "\""
				+ " integer primary key autoincrement," + "\"\n");
		for (Entry<String, String> entry : mapFileds.entrySet()) {
			sb.append(" + ").append(entry.getValue().toUpperCase())
					.append(" + ").append("\" text, \"\n");
		}

		String result = getWithOutLast(sb.toString(), ",");
		result = result + ")\"";

		return result + ");";
	}

	private static String getFieldsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("\n").append("	").append("/").append("**").append("以下是表字段")
				.append("*").append("/").append("\n");
		sb.append("public static final String _ID = \"_id\";\n");
		for (Entry<String, String> entry : mapFileds.entrySet()) {
			sb.append("	public static final String ")
					.append(entry.getValue().toString().toUpperCase())
					.append(" = ").append("\"")
					.append(entry.getValue().toString()).append("\"")
					.append(";").append("\n");
		}
		return sb.toString();
	}

	public static void getClassField(String content) {
		int startIndex = content.indexOf("{");
		int endIndex = content.lastIndexOf("}");
		String body = content.substring(startIndex + 1, endIndex);
		String[] split = body.split(";");
		for (String statement : split) {

			// 排除静态的字段
			if (statement.contains(" static ")) {
				continue;
			}

			if (statement.contains("{") || statement.contains("}")) {
				// 是方法语句
				statement = getWithOutLast(statement, "(");
				String method = getStringFromLastIndex(statement, " ");
				if (method.contains("get")) {
					getMethods.put(method, method);
				} else if (method.contains("set")) {
					setMethods.put(method, method);
				}
			} else {
				// 是字段声明
				if (statement.contains("=")) {
					statement = getWithOutLast(statement, "=").trim();
				}

				String field = getStringFromLastIndex(statement, " ");
				if (field != null && !"".equals(field)) {
					mapFileds.put(field, field);
				}
			}
		}
		// System.out.println(mapFileds);
		// System.out.println(getMethods);
		// System.out.println(setMethods);
	}

	/**
	 * 获取model的类名+“Db”，用以创建该自动生成的类名
	 */
	public static String getClassName() {
		createModelName = getWithOutLast(file.getName(), ".");
		return createFileName = createModelName + "Db";
	}

	/**
	 * 获取整个model的内容
	 */
	public static String getModelFileString(String path) {
		try {
			file = new File(path);
			return getStringFromFile(file)
					.replace(System.getProperty("line.separator"), "").replace("/", "")
					.replace("*", "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getWithOutLast(String origin, String simbol) {
		int index = origin.lastIndexOf(simbol);
		if (index > 0) {
			return origin.substring(0, index).trim();
		}
		return "";
	}

	public static String getStringFromLastIndex(String origin, String symbol) {
		int index = origin.lastIndexOf(symbol);
		if (index > 0) {
			return origin.substring(index + 1, origin.length());
		}
		return "";
	}

	/**
	 * 设置第一个字母为大写
	 */
	public static String setFirstLetterBig(String origin) {

		char[] chars = origin.toCharArray();

		if (chars[0] >= 97) {
			chars[0] = (char) (chars[0] - 32);
		}

		return new String(chars);

	}

	public static String getStringFromFile(File file) {
		if (!file.exists()) {
			return null;
		}
		BufferedReader br = null;
		try {
			FileReader reader = new FileReader(file);
			br = new BufferedReader(reader);
			StringBuilder buffer = new StringBuilder();
			String s;
			while ((s = br.readLine()) != null) {
				buffer.append(s);
			}
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}



// public void remit(int from, int to, int amount) {
// SQLiteDatabase qlk_db = helper.getWritableDatabase();
// try {
// qlk_db.beginTransaction(); // 开始事务
// qlk_db.execSQL("UPDATE person SET balance=balance-? WHERE id=?", new
// Object[] { amount, from });
// qlk_db.execSQL("UPDATE person SET balance=balance+? WHERE id=?", new
// Object[] { amount, to });
// qlk_db.setTransactionSuccessful(); // 事务结束时, 成功点之前的操作会被提交
// } finally {
// qlk_db.endTransaction(); // 结束事务, 将成功点之前的操作提交
// qlk_db.close();
// }
// }
