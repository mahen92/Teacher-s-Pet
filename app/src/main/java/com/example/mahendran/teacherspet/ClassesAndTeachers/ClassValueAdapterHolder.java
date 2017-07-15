package com.example.mahendran.teacherspet.ClassesAndTeachers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mahendran.teacherspet.R;

/**
 * Created by Mahendran on 19-06-2017.
 */

public class ClassValueAdapterHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ClassValueAdapterHolder.class.getSimpleName();
    public TextView className;

    public ClassValueAdapterHolder(View itemView) {
        super(itemView);
        className = (TextView) itemView.findViewById(R.id.class_name);
    }
}
