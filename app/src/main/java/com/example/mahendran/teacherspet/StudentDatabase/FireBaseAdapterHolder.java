package com.example.mahendran.teacherspet.StudentDatabase;

/**
 * Created by Mahendran on 04-06-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mahendran.teacherspet.R;

public class FireBaseAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FireBaseAdapterHolder.class.getSimpleName();
    public TextView name;

    public FireBaseAdapterHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.student);
    }
}
