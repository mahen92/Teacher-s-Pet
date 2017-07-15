package com.example.mahendran.teacherspet.Test;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.mahendran.teacherspet.StudentDatabase.StudentValues;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Mahendran on 07-06-2017.
 */

public class GetTestAdapter extends FirebaseRecyclerAdapter<StudentValues, GetTestAdapterHolder> {
    private static final String TAG = com.example.mahendran.teacherspet.StudentDatabase.FireBaseAdapter.class.getSimpleName();
    private Context context;
    public String testName;

    public GetTestAdapter(Class<StudentValues> modelClass, int modelLayout, Class<GetTestAdapterHolder> viewHolderClass, DatabaseReference ref, Context context, String className) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        testName = className;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(GetTestAdapterHolder viewHolder, final StudentValues model, int position) {
        viewHolder.name.setText(model.getEmailID());
        final String stname = model.getEmailID();
        if (model.map.containsKey(testName)) {
            viewHolder.marks.setText(model.map.get(testName));
        }
        final String key = model.getId();

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddTest.class);
                intent.putExtra("TestName", testName);
                intent.putExtra("StudentName", stname);
                intent.putExtra("Key", key);
                context.startActivity(intent);

            }
        });
    }
}