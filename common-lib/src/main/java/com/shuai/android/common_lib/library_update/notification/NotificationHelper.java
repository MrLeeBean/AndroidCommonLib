package com.shuai.android.common_lib.library_update.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.shuai.android.common_lib.R;
import com.shuai.android.common_lib.library_update.UIData;
import com.shuai.android.common_lib.library_update.download.VersionFacade;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;
/**
 * Created by 一米阳光 on 2018/3/8.
 */

public class NotificationHelper {
    private VersionFacade versionFacade;
    private UIData uiData;
    NotificationCompat.Builder notificationBuilder = null;
    NotificationManager manager = null;
    private boolean isDownloadSuccess=false,isFailed=false;
    private int currentProgress = 0;
    private  final int NOTIFICATION_ID=0;
    private String contentText;
    public NotificationHelper(VersionFacade versionFacade, UIData uiData) {
        this.versionFacade = versionFacade;
        this.uiData = uiData;
    }
    public void showNotification()
    {
        isDownloadSuccess=false;
        isFailed=false;
        if(versionFacade.isShowNotification())
        {
            manager= (NotificationManager) uiData.getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationBuilder = createNotification();
            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
    /**
     * update notification progress
     *
     * @param progress the progress of notification
     */
    public void updateNotification(int progress) {
        if (versionFacade.isShowNotification()) {
            if ((progress - currentProgress) > 5&&!isDownloadSuccess&&!isFailed) {
                notificationBuilder.setContentIntent(null);
                notificationBuilder.setContentText(String.format(contentText, progress));
                notificationBuilder.setProgress(100, progress, false);
                manager.notify(NOTIFICATION_ID, notificationBuilder.build());
                currentProgress = progress;
            }
        }
    }
    private NotificationCompat.Builder createNotification(){
        final String CHANNEL_ID = "0", CHANNEL_NAME = "ALLEN_NOTIFICATION";
        NotificationCompat.Builder builder = null;
        NotificationBuilder notificationBuilder=versionFacade.getNotificationBuilder();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            NotificationManager manager = (NotificationManager) uiData.getContext().getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        builder=new NotificationCompat.Builder(uiData.getContext(),CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setSmallIcon(notificationBuilder.getIcon());
        String contentTitle = uiData.getContext().getString(R.string.app_name);
        if(TextUtils.isEmpty(notificationBuilder.getTitle()))
        {
            contentTitle=notificationBuilder.getTitle();
        }
        builder.setContentTitle(contentTitle);
        //set ticker
        String ticker = "正在下载中...";
        if (notificationBuilder.getTicker() != null)
            ticker = notificationBuilder.getTicker();
        builder.setTicker(ticker);
        //set content text
        contentText = "下载进度:%d%%/100%%";
        if (notificationBuilder.getContentText() != null)
            contentText = notificationBuilder.getContentText();
        builder.setContentText(String.format(contentText, 0));

        if (notificationBuilder.isRingtone()) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(uiData.getContext(), notification);
            r.play();
        }
        return  builder;
    }
    public void showDownloadCompleteNotifcation(File file) {
        isDownloadSuccess=true;
        if (!versionFacade.isShowNotification())
            return;
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri;
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            ALog.e(context.getPackageName() + "");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }*/
        //设置intent的类型
      /*  i.setDataAndType(uri,
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentText(context.getString(R.string.versionchecklib_download_finish));
        notificationBuilder.setProgress(100, 100, false);*/
        manager.cancelAll();
        manager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void showDownloadFailedNotification() {
        isDownloadSuccess=false;
        isFailed=true;
        if (versionFacade.isShowNotification()) {
           /* Intent intent = new Intent(context, PermissionDialogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setContentText(context.getString(R.string.versionchecklib_download_fail));
            notificationBuilder.setProgress(100, 0, false);
            manager.notify(NOTIFICATION_ID, notificationBuilder.build());*/
        }
    }
    public void onDestroy() {
        if(manager!=null)
            manager.cancel(NOTIFICATION_ID);
    }
}
