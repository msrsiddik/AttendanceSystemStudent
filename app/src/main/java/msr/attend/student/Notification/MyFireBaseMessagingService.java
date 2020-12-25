package msr.attend.student.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import msr.attend.student.R;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "999";
    String title,message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            title=remoteMessage.getData().get("Title");
            message=remoteMessage.getData().get("Message");

        for (Map.Entry<String, String> map : remoteMessage.getData().entrySet()){
            if (map.getKey().equals("Title")) {
                Log.e("Title", map.getValue());
            }else if (map.getKey().equals("Message")) {
                Log.e("Message", map.getValue());
            }
        }

        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setChannelId(CHANNEL_ID);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }

    private void sendNotification(RemoteMessage remoteMessage) {

//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("Title");
        String body = remoteMessage.getData().get("Message");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(this, SendNotif.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound);
//                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
//        if (j > 0){
//            i = j;
//        }

        noti.notify(i, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
