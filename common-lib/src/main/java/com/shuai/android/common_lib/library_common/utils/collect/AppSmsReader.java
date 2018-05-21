package com.shuai.android.common_lib.library_common.utils.collect;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 获取短信列表
 */

public class AppSmsReader {

    private static final AppSmsReader ourInstance = new AppSmsReader();

    public static AppSmsReader getInstance() {
        return ourInstance;
    }

    private AppSmsReader() {
    }

    public List<SmsBean> getSmsList() {
        final String SMS_URI_ALL = "content://sms/";
        List<SmsBean> smsList = new ArrayList<>();
        try {
            ContentResolver cr = BaseApplication.getInstance().getContentResolver();
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            Cursor cur = cr.query(uri, projection, null, null, "date desc");

            if (cur.moveToFirst()) {
                String phoneNumber;
                String smsbody;
                String date;
                String type;

                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");

                do {
                    phoneNumber = cur.getString(phoneNumberColumn);
                    smsbody = cur.getString(smsbodyColumn);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                    date = dateFormat.format(d);

                    int typeId = cur.getInt(typeColumn);
                    if (typeId == 1) {
                        type = "接收";
                    } else if (typeId == 2) {
                        type = "发送";
                    } else {
                        type = "";
                    }
                    SmsBean sms = new SmsBean();
                    sms.setPhone(phoneNumber);
                    sms.setContent(smsbody);
                    sms.setDate(date);
                    sms.setType(type);

                    smsList.add(sms);
                } while (cur.moveToNext());
            }
        } catch (Exception ex) {
            AppExceptionHandler.doHandle(ex,"获取短信内容发生异常");
        }
        return smsList;
    }
    public class SmsBean {
        private String phone;
        private String content;
        private String date;
        private String type;

        public SmsBean() {
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
