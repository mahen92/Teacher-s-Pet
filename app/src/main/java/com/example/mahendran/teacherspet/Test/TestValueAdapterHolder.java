package com.example.mahendran.teacherspet.Test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mahendran.teacherspet.R;
import com.example.mahendran.teacherspet.StudentDatabase.FireBaseAdapterHolder;

/**
 * Created by Mahendran on 05-06-2017.
 */

public class TestValueAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = TestValueAdapterHolder.class.getSimpleName();
    public TextView className;

    public TestValueAdapterHolder(View itemView) {
        super(itemView);
        className = (TextView) itemView.findViewById(R.id.name);
    }
}