package com.xiaocoder.test;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.io.XCIO;
import com.xiaocoder.android.fw.general.io.XCIOAndroid;
import com.xiaocoder.android.fw.general.io.XCLog;
import com.xiaocoder.android.fw.general.io.XCSP;
import com.xiaocoder.android.fw.general.js_xl_encryption.aes.AesEncryptAndDecrypt;
import com.xiaocoder.android.fw.general.js_xl_encryption.des.DesEncryptAndDecrypt;
import com.xiaocoder.android.fw.general.js_xl_encryption.md5.UtilMd5;
import com.xiaocoder.android.fw.general.json.XCJsonBean;
import com.xiaocoder.android.fw.general.json.XCJsonParse;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.android.fw.general.util.UtilSystem;
import com.xiaocoder.middle.MActivity;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;


public class TextActivity extends MActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置布局
        setContentView(R.layout.activity_text);
        super.onCreate(savedInstanceState);
    }

    // 初始化控件
    @Override
    public void initWidgets() {
        test1();
        test2();
        test3();
        test4();
    }

    private void test4() {
        XCSP.spPut("1", "123 ");
        XCSP.spPut("2", 456);
        XCSP.spPut("3", 789.1F);
        XCSP.spPut("4", false);
        XCSP.spPut("5", " abc ");

//        String result = XCSP.spGet("1", "abc") + XCSP.spGet("2", 0) + XCSP.spGet("3", 0.1f)
//                + XCSP.spGet("4", true) + XCSP.spGet("5", "abc") + XCSP.spGet("6", null) + XCSP.spGet("7", "    jkl");
//        XCLog.shortToast(result);

        XCSP.spPut("1", 0.1f);

        String result2 = XCSP.spGet("5", "java") + XCSP.spGet("1", 0.0f) + XCSP.spGet("2", 0) + XCSP.spGet("3", 0.1f)
                + XCSP.spGet("4", true) + XCSP.spGet("6", null) + XCSP.spGet("7", "    jkl");
        XCLog.shortToast(result2);
    }


    private void test3() {
        XCLog.i("UUID----" + UUID.randomUUID() + "----" + UUID.randomUUID().toString().length());

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("a");
        queue.add("b");
        queue.add("c");
        queue.add("d");

//        try {
//            XCLog.i(queue.take()); // a  //take 类似 poll remove
//            XCLog.i(queue.take()); // b
//            XCLog.i(queue.take()); // c
//            XCLog.i(queue.take()); // d
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        XCLog.i(queue.peek()); //a  // peek 类似 element
        XCLog.i(queue.peek()); //a
        XCLog.i(queue.peek()); //a
        XCLog.i(queue.peek()); //a
        queue.remove("a");
        XCLog.i(queue.peek()); //b
        XCLog.i(queue.peek()); //b
        XCLog.i(queue.peek()); //b


    }

    private void test2() {
        String one = UtilMd5.MD5Encode("123456abc");
        String two = UtilMd5.MD5Encode2("123456abc");
        XCLog.i(one);
        XCLog.i(two);
        XCLog.i(UtilString.equalsStr(one, two)); // true

        String des_close = DesEncryptAndDecrypt.encodeRequestStr("today is haha 123");
        String des_open = DesEncryptAndDecrypt.decodeResponseStr(des_close);
        XCLog.i(des_close);
        XCLog.i(des_open);

        String aes_close = AesEncryptAndDecrypt.encodeRequestStr("computer 123 macpro");
        String aes_open = AesEncryptAndDecrypt.decodeResponseStr(aes_close);
        XCLog.i(aes_close);
        XCLog.i(aes_open);
    }

    private void test1() {
        String str = "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": [\n" +
                "\"/cr/100/200/aaa.mp4\"\n" +
                "    ]\n" +
                "}\n";

        String str2 = " {\"code\":0,\"msg\":\"成功\",\"data\":[[\"板蓝根\",\"白云山\"]]}";

        XCJsonBean bean = XCJsonParse.getJsonParseData(str2, XCJsonBean.class);
        List beans = bean.getListList("data", new ArrayList<ArrayList>());

        try {
            XCLog.i(beans.toString());
            XCLog.i(beans.get(0).toString());
            if (beans.get(0) instanceof List) {
                XCLog.i("List");
            } else if (beans.get(0) instanceof String[]) {
                XCLog.i("string[]");
            } else {
                XCLog.i(beans.get(0).getClass().toString());

                JSONArray array = (JSONArray) beans.get(0);
                int count = array.length();
                for (int i = 0; i < count; i++) {
                    XCLog.i((String) array.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            XCLog.e(this.toString() + "---exception");
        }

        XCLog.i(UtilSystem.getDeviceId(this) + "--------------deviceId");
    }

    // 设置监听
    @Override
    public void listeners() {

    }


    public void test() {
        final File card = XCIOAndroid.createFileInAndroid(getApplicationContext(), "card", "card.txt");

        new Thread(new Runnable() {

            HashSet<String> set = new HashSet<String>();
            Random rand = new Random();
            int i = 0;

            char[] chars_num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            char[] chars_letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };

            @Override
            public void run() {
                while (set.size() < 500000) {
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 10; i++) {
                        sb.append(chars_num[rand.nextInt(10)]);
                    }

                    char[] result = sb.toString().toCharArray();

                    int letterPosition_1 = getPosition(0, 0);
                    int letterPosition_2 = getPosition(letterPosition_1, 2);
                    int letterPosition_3 = getPosition(letterPosition_2, 4);
                    int letterPosition_4 = getPosition(letterPosition_3, 6);

                    result[letterPosition_1] = chars_letter[rand.nextInt(26)];
                    result[letterPosition_2] = chars_letter[rand.nextInt(26)];
                    result[letterPosition_3] = chars_letter[rand.nextInt(26)];
                    result[letterPosition_4] = chars_letter[rand.nextInt(26)];

                    if (set.add(new String(result) + XCIO.LINE_SEPARATOR)) {
                        System.out.println(i++ + "---" + new String(result));
                    } else {
                        System.out.println("重复了");
                    }

                    if (letterPosition_2 - letterPosition_1 < 2 || letterPosition_3 - letterPosition_2 < 2 || letterPosition_4 - letterPosition_3 < 2) {
                        System.out.println("不连续");
                    }
                }

                System.out.println("size" + set.size());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(card);
                    for (String str : set) {
                        fos.write(str.getBytes());
                    }
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    public int getPosition(int before, int index) {

        Random rand = new Random();

        int last = rand.nextInt(4) + index;

        if (before == 0) {
            return last;
        }

        if (last - before == -1) {
            last = last + 3; // 加3
        } else if (last - before == 0) {
            // 加2或加3
            if (before % 2 == 0) {
                last = last + rand.nextInt(2) + 2; // 加2或者加3
            } else {
                last = last + 2; // 只能加2
            }
        } else if (last - before == 1) {
            if (last % index == 0) {
                if (before == index) {
                    last = last + rand.nextInt(3) + 1; // 加1 2 3 都行
                } else {
                    last = last + 1;
                }
            } else if (last % index == 1) {
                last = last + rand.nextInt(2) + 1; // 加1 或 加2
            } else if (last % index == 2) {
                last = last + 1; // 加1
            }
        }
        return last;
    }
}