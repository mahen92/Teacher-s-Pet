package com.example.mahendran.teacherspet.Test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mahendran.teacherspet.R;
import com.example.mahendran.teacherspet.StudentDatabase.FireBaseAdapterHolder;

/**
 * Created by Mahendran on 25-06-2017.
 */

public class GetTestAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FireBaseAdapterHolder.class.getSimpleName();
    public TextView name;
    public TextView marks;

    public GetTestAdapterHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.stud_name);
        marks = (TextView) itemView.findViewById(R.id.marks);
    }
}
