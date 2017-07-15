package com.example.mahendran.teacherspet.ClassesAndTeachers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahendran.teacherspet.Connectivity.ConnectivityReceiver;
import com.example.mahendran.teacherspet.Connectivity.MyApplication;
import com.example.mahendran.teacherspet.R;
import com.example.mahendran.teacherspet.StudentDatabase.StudentValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassAndTeachers extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public DatabaseReference mDatabase;
    public DatabaseReference teacherCloudEndPoint;
    public DatabaseReference classCloudEndPoint;
    private FirebaseAuth auth;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedpreferences;
    EditText getClass;
    TextView className;
    EditText teacherName;
    Button classAdd;
    Button logout;
    ClassAdapter ca;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_and_teachers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        linearLayout = (LinearLayout) findViewById(R.id.add_class_layout);
        getClass = (EditText) findViewById(R.id.get_class);
        className = (TextView) findViewById(R.id.class_name);
        teacherName = (EditText) findViewById(R.id.teacher_name);
        classAdd = (Button) findViewById(R.id.add_class_button);
        logout = (Button) findViewById(R.id.log_out);
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedpreferences.getString("Email", null);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        teacherCloudEndPoint = mDatabase.child("Teachers");
        classCloudEndPoint = teacherCloudEndPoint.child(auth.getCurrentUser().getEmail().split("@")[0]);
        if (((auth.getCurrentUser().getEmail().split("@")[0]).equals(email))) {

            linearLayout.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                auth.signOut();
                Toast.makeText(getBaseContext(), R.string.log_out,
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });


        classAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassValues cv = new ClassValues();
                String classname = (String.valueOf(getClass.getText()));
                cv.setClassName(classname);
                DatabaseReference tempCloudEndPoint;
                tempCloudEndPoint = classCloudEndPoint.child(classname);
                String key = tempCloudEndPoint.push().getKey();
                tempCloudEndPoint.child(key).setValue(cv);
                DatabaseReference tempClassCloudEndPoint;
                DatabaseReference tempClassCloudEndPoint1;
                tempClassCloudEndPoint = teacherCloudEndPoint.child(auth.getCurrentUser().getEmail().split("@")[0]);
                tempClassCloudEndPoint1 = tempClassCloudEndPoint.child("Classes");
                String key1 = tempClassCloudEndPoint1.push().getKey();
                tempClassCloudEndPoint1.child(key1).setValue(cv);
                Snackbar.make(view, getString(R.string.class_add) + classname, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView gridView = (RecyclerView) findViewById(R.id.class_list_view);

        DatabaseReference tempClassCloudEndPoint;
        DatabaseReference tempClassCloudEndPoint1;

        tempClassCloudEndPoint = teacherCloudEndPoint.child(email);
        tempClassCloudEndPoint1 = tempClassCloudEndPoint.child("Classes");
        ca = new ClassAdapter(ClassValues.class, R.layout.teachers_and_classes, ClassValueAdapterHolder.class, tempClassCloudEndPoint1, this);
        gridView.setLayoutManager(linearLayoutManager);
        gridView.setAdapter(ca);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver receiver = new ConnectivityReceiver();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        statusDisplay(isConnected);
    }

    private void statusDisplay(boolean isConnected) {
        if (!(isConnected)) {
            Toast.makeText(getApplication(), R.string.connectrivity_issue, Toast.LENGTH_SHORT).show();
        }
    }
}

