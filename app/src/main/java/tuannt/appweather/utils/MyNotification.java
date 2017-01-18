package tuannt.appweather.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import tuannt.appweather.R;
import tuannt.appweather.activities.SlideActivity;
import tuannt.appweather.activities.SplashActivity;
import tuannt.appweather.models.User;
import tuannt.appweather.tasks.GetCityWeather;

/**
 * Created by tuan on 22/02/2016.
 */
public class MyNotification {

    private Activity context;

    private static AlertDialog.Builder builder = null;

    public MyNotification(Activity context) {
        this.context = context;
    }

    public void showStatus(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tuannt", context.MODE_PRIVATE);
        boolean canSendStatus = sharedPreferences.getBoolean("canSendStatus", true);
        if(canSendStatus==true) {
            // TODO Auto-generated method stub
            //Creating Notification Builder
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            //Title for Notification
            User user = User.getInstance();
            notification.setContentTitle(user.getWeather().getCity_name());
            //Message in the Notification
            notification.setContentText(MyMethods.getCTemp(user.getWeather().getTemp()) + ", " + MyMethods.capitalize(user.getWeather().getDescription()));
            //Alert shown when Notification is received
//        notification.setTicker("New Message Alert!");
            //Icon to be set on Notification
            notification.setSmallIcon(Variables.WEATHER_ICON.get(user.getWeather().getIcon()));
//        switchSmallIcon(notification, user.getWeather().getDescription());
            //Creating new Stack Builder
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(SplashActivity.class);
            //Intent which is opened when notification is clicked
            Intent resultIntent = new Intent(context, SplashActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pIntent);

//        Intent notificationIntent = new Intent(context, SplashActivity.class);
//
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent intent = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);

//        notification.setContentIntent(intent);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification.build());
        }
    }


    public static void showDialogNotification(final Activity context){
        if(builder==null){
            builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Lỗi kết nối");
            builder.setMessage("Vui lòng kiểm tra kết nối mạng để tiếp tục sử dụng ứng dụng!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder = null;
//                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    Log.i("CLASS", "CLASS: " + context.getLocalClassName());
                    if (context.getLocalClassName().equals("activities.SplashActivity")) {
                        context.finish();
                    }
                }
            });
            builder.show();
        }

    }
}
