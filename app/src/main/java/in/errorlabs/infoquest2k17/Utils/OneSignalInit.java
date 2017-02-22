package in.errorlabs.infoquest2k17.Utils;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by root on 1/22/17.
 */

public class OneSignalInit extends Application {
    private static OneSignalInit myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler()).init();
        myApplication=this;
    }

    public static synchronized Context getAppContext(){
        return myApplication.getApplicationContext();
    }
    class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String customKey;
            String id =notification.payload.notificationID;
            String title=notification.payload.title;
            String small_icon=notification.payload.smallIcon;
            String large_icon=notification.payload.largeIcon;
            String launch_url=notification.payload.launchURL;
            String description = notification.payload.body  ;
            String image = notification.payload.bigPicture;
            Toast.makeText(getApplicationContext(),id+title+small_icon+large_icon+launch_url+description+image,Toast.LENGTH_LONG).show();
            Log.v("","in receiver");

            if (data != null) {

                customKey = data.optString("customkey", null);
                if (customKey == null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }
    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;
            String id =result.notification.payload.notificationID;
            String title=result.notification.payload.title;
            String small_icon=result.notification.payload.smallIcon;
            String large_icon=result.notification.payload.largeIcon;
            String launch_url=result.notification.payload.launchURL;
            String description = result.notification.payload.body  ;
            String image = result.notification.payload.bigPicture;
            Toast.makeText(getApplicationContext(),id+title+small_icon+large_icon+launch_url+description+image,Toast.LENGTH_LONG).show();
            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            Intent intent = new Intent(getApplicationContext(), Notification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
            /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
        }
    }
}