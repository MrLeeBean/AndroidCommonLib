package com.shuai.android.common_lib.library_common.utils.collect;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;


import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;
import com.shuai.android.common_lib.library_common.utils.LangUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取联系人
 */

public class AppContactsReader {
    private static final AppContactsReader ourInstance = new AppContactsReader();

    public static AppContactsReader getInstance() {
        return ourInstance;
    }

    private AppContactsReader() {
    }
    public List<ContactBean> getContactList(){

        try {
            //生成ContentResolver对象
            ContentResolver contentResolver = BaseApplication.getInstance().getContentResolver();

            // 获得所有的联系人
            Cursor cursor = contentResolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            //这段代码和上面代码是等价的，使用两种方式获得联系人的Uri
            //   Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);
            if (cursor == null) {  //针对vivo手机 获取不到数据库
                return null;
            }
            ArrayList<ContactBean> list = new ArrayList<ContactBean>();
            // 循环遍历
            if (cursor.moveToFirst()) {

                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int lastUpdateColumn = cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP);

                do {

                    // 获得联系人的ID
                    String contactId = cursor.getString(idColumn);
                    // 获得联系人姓名
                    String displayName = cursor.getString(displayNameColumn);
                    // 获得联系人最新修改时间
                    String lastUpdateTime;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){//18-4.3以上才有效。
                        try {
                            lastUpdateTime = cursor.getLong(lastUpdateColumn) + "";
                        }catch (Exception e){
                            lastUpdateTime = "-1";
                            e.printStackTrace();
                            AppExceptionHandler.doHandle(e,"获取联系人最新更新时间出错");
                        }

                    }else{

                        lastUpdateTime = "-1";

                    }


                    //联系人
                    ContactBean user = new ContactBean();
                    user.setName(displayName);//姓名
                    user.setLastUpdate(lastUpdateTime);//最后修改时间。

                    // 查看联系人有多少个号码，如果没有号码，返回0
                    int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (phoneCount > 0) {
                        // 获得联系人的电话号码列表
                        Cursor phoneCursor = BaseApplication.getInstance().getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + "=" + contactId, null, null);
                        if (phoneCursor.moveToFirst()) {
                            List phoneNumbers = new ArrayList<String>();
                            ArrayList<String> phones = new ArrayList<String>();
                            do {
                                //遍历所有的联系人下面所有的电话号码
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                phones.add(phoneNumber);

                            } while (phoneCursor.moveToNext());
                            user.setPhone(phones);
                        }
                        phoneCursor.close();

                    } else {

                    }


                    if (!LangUtils.isStrNullOrEmpty(user.getName()) && (user.getPhone() != null && user.getPhone().size() > 0)) {//姓名不为空 且 电话号码个数大于0 -->才予以添加到联系人列表中
                        // 将每组数据加入链表
                        list.add(user);
                    }


                } while (cursor.moveToNext());

            }
            cursor.close();
            return list;
        } catch (Exception e) {
            AppExceptionHandler.doHandle(e, "获取联系人：发生了异常");

            return null;
        }
    }

    public class ContactBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;//姓名
        private ArrayList<String> phone;//电话号码list
        private String lastUpdate;//最后一次的更新时间

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getPhone() {
            return phone;
        }

        public void setPhone(ArrayList<String> phone) {
            this.phone = phone;
        }

        public String getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
}
