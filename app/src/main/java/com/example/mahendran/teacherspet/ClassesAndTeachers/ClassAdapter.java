package com.example.mahendran.teacherspet.ClassesAndTeachers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.example.mahendran.teacherspet.ActionsScreen;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Mahendran on 19-06-2017.
 */

public class ClassAdapter extends FirebaseRecyclerAdapter<ClassValues, ClassValueAdapterHolder> {
    private static final String TAG = ClassAdapter.class.getSimpleName();
    private Context context;

    public ClassAdapter(Class<ClassValues> modelClass, int modelLayout, Class<ClassValueAdapterHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(final ClassValueAdapterHolder viewHolder, ClassValues model, int position) {

        viewHolder.className.setText(model.getClassName());
        final String className = model.ClassId;
        viewHolder.className.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences;
                sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("class", className);
                editor.commit();
                Intent intent = new Intent(context, ActionsScreen.class);
                intent.putExtra("String", "Teacher");
                intent.putExtra("Class", className);
                context.startActivity(intent);

            }
        });

    }
}

