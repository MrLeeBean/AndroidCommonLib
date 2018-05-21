package com.shuai.android.common_lib.library_update.notification;

import com.shuai.android.common_lib.R;

/**
 * Created by 一米阳光 on 2018/3/8.
 */

public class NotificationBuilder {
    private int icon;
    private String title;
    private String ticker;
    private String contentText;
    private boolean isRingtone;
    public static  NotificationBuilder create(){
        return new NotificationBuilder();
    }
    private NotificationBuilder()
    {
        icon = R.mipmap.ic_launcher;
        isRingtone = true;
    }

    public int getIcon() {
        return icon;
    }

    public NotificationBuilder setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NotificationBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTicker() {
        return ticker;
    }

    public NotificationBuilder setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public String getContentText() {
        return contentText;
    }

    public NotificationBuilder setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public boolean isRingtone() {
        return isRingtone;
    }

    public NotificationBuilder setRingtone(boolean ringtone) {
        isRingtone = ringtone;
        return this;
    }
}
