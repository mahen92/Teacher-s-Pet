package com.example.mahendran.teacherspet.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViewsService;

import com.example.mahendran.teacherspet.DiscussionRoom.DiscussionboardValues;

import java.util.ArrayList;

/**
 * Created by Mahendran on 01-07-2017.
 */

public class WidgetService extends RemoteViewsService {
/*
* So pretty simple just defining the Adapter of the listview
* here Adapter is ListProvider
* */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Bundle extras = intent.getExtras();
        ArrayList<DiscussionboardValues> a = (ArrayList<DiscussionboardValues>) extras.getSerializable("ArrayList");

        return (new ListProvider(this.getApplicationContext(), intent, a));
    }

}



