package com.example.mahendran.teacherspet.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.mahendran.teacherspet.DiscussionRoom.DiscussionboardValues;
import com.example.mahendran.teacherspet.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String INTENT_ACTION = "com.example.mahendran.teacherspet.Widget.WidgetProvider.INTENT_ACTION";


    /**
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     **/

    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int[] appWidgetIds) {

/*int[] appWidgetIds holds ids of multiple instance
 * of your widget
 * meaning you are placing more than one widgets on
 * your homescreen*/
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.discussion_room_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //svcIntent.putExtra("ArrayList", a);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
        AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
        AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
        remoteViews.setRemoteAdapter(R.id.listViewWidget,
                svcIntent);
        Intent i = new Intent(context, WidgetProvider.class);
        i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        PendingIntent pi = PendingIntent.getBroadcast(context,0, i,0);
        remoteViews.setOnClickPendingIntent(R.id.refresh,pi);

        AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);


        //setting an empty view in case of no data
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if (intent.getAction()==null) {
            context.startService(new Intent(context, WidgetService.class));
        }
        else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Bundle extras = intent.getExtras();

            int[] appid = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            //a = (ArrayList<DiscussionboardValues>) extras.getSerializable(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
            onUpdate(context, AppWidgetManager.getInstance(context), appid);

        }
        else {
            super.onReceive(context, intent);
        }
    }


}