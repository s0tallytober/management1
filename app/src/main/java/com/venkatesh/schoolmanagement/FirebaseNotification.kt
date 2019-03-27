package com.venkatesh.schoolmanagement

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.venkatesh.schoolmanagement.activity.SplashActivity

/**
 * Helper class for showing and canceling firebase
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object FirebaseNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private val NOTIFICATION_TAG = "Firebase"

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     *
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     *
     *
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of firebase notifications. Make
     * sure to follow the
     * [
 * Notification design guidelines](https://developer.android.com/design/patterns/notifications.html) when doing so.
     *
     * @see .cancel
     */

    /*fun sendNotification(context: Context,title: String, messageBody: String) {
        val mBuilder =
            NotificationCompat.Builder(context, "channelid")
        val intent = Intent(context, SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(title)
        bigText.setBigContentTitle(messageBody)

        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(messageBody)
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setAutoCancel(true)
        mBuilder.setStyle(bigText)

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelid",
                "channelname",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager!!.createNotificationChannel(channel)
        }

        mNotificationManager!!.notify(0, mBuilder.build())
    }*/
    fun notify(
        context: Context,
        exampleString: String, number: Int
    ) {
        val res = context.resources

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
      //  val picture = BitmapFactory.decodeResource(res, R.drawable.example_picture)


        val title = exampleString

        /*val text = res.getString(
            R.string.firebase_notification_placeholder_text_template, exampleString
        )*/
        //val text = exampleString

        val builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))

            // Set appropriate defaults for the notification light, sound,
            // and vibration.
            .setDefaults(Notification.DEFAULT_ALL)

            // Set required fields, including the small icon, the
            // notification title, and text.
            .setSmallIcon(R.drawable.ic_stat_firebase)
            .setContentTitle(title)
          //  .setContentText(text)

            // All fields below this line are optional.

            // Use a default priority (recognized on devices running Android
            // 4.1 or later)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Provide a large icon, shown with the notification in the
            // notification drawer on devices running Android 3.0 or later.
            //.setLargeIcon(picture)

            // Set ticker text (preview) information for this notification.
            .setTicker(exampleString)

            // Show a number. This is useful when stacking notifications of
            // a single type.
            .setNumber(number)

            // If this notification relates to a past or upcoming event, you
            // should set the relevant time information using the setWhen
            // method below. If this call is omitted, the notification's
            // timestamp will by set to the time at which it was shown.
            // TODO: Call setWhen if this notification relates to a past or
            // upcoming event. The sole argument to this method should be
            // the notification timestamp in milliseconds.
            //.setWhen(...)

            // Set the pending intent to be initiated when the user touches
            // the notification.
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, SplashActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                    },
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )

            // Show expanded text content on devices running Android 4.1 or
            // later.
            .setStyle(
                NotificationCompat.BigTextStyle()
                    //.bigText(text)
                    .setBigContentTitle(title)
                    .setSummaryText(context.getString(R.string.notification))
            )

            // Example additional actions for this notification. These will
            // only show on devices running Android 4.1 or later, so you
            // should ensure that the activity in this notification's
            // content intent provides access to the same actions in
            // another way.
            /*.addAction(
                R.drawable.ic_action_stat_share,
                res.getString(R.string.action_share),
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addAction(
                R.drawable.ic_action_stat_reply,
                res.getString(R.string.action_reply),
                null
            )*/

            // Automatically dismiss the notification when it is touched.
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.default_notification_channel_id),
                context.getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            mNotificationManager!!.createNotificationChannel(channel)
        }
        notify(context, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * [.notify].
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }
}
