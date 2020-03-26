package app.gify.co.id.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.gify.co.id.R;
import app.gify.co.id.activity.MainActivity;

/**
 * Created by SUPRIYANTO on 3/26/2020.
 */

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        Intent intent = new Intent(this, MainActivity.class);
        intent.getIntExtra(title, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification);
        remoteViews.setTextViewText(R.id.text_View_title_notif_item, title);

        final NotificationCompat.Builder compat = new NotificationCompat.Builder(this);
        compat.setContentTitle(title);
        compat.setCustomContentView(remoteViews);
        compat.setSmallIcon(R.mipmap.ic_launcher_round);
        compat.setAutoCancel(true);
        compat.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, compat.build());

    }
}
