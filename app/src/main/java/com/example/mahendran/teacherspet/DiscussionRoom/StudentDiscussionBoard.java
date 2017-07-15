package com.example.mahendran.teacherspet.DiscussionRoom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mahendran.teacherspet.Connectivity.ConnectivityReceiver;
import com.example.mahendran.teacherspet.Connectivity.MyApplication;
import com.example.mahendran.teacherspet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentDiscussionBoard extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private DatabaseReference mDatabase;
    private DatabaseReference DiscussionboardCloudEndPoint;
    private DatabaseReference teacherCloudEndPoint;
    private DatabaseReference teacherClassCloudEndPoint;
    private DatabaseReference referenceClassCloudEndPoint;
    private EditText question;
    private EditText answer;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_discussion_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = pref.getString("Email", null);
        String className = pref.getString("class", null);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        referenceClassCloudEndPoint = mDatabase.child("Teachers");
        teacherCloudEndPoint = referenceClassCloudEndPoint.child(email);
        teacherClassCloudEndPoint = teacherCloudEndPoint.child(className);
        DiscussionboardCloudEndPoint = teacherClassCloudEndPoint.child("DiscussionBoard");
        from = getIntent().getStringExtra("from");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete_button);
        answer = (EditText) findViewById(R.id.answer_by_teacher);
        question = (EditText) findViewById(R.id.Question_from_student);
        String teacherAnswer;
        String studentQuestion;

        teacherAnswer = getIntent().getStringExtra("answer");
        studentQuestion = getIntent().getStringExtra("question");
        final String id = getIntent().getStringExtra("id");
        answer.setText(teacherAnswer);
        question.setText(studentQuestion);

        if ((from == null)) {
            answer.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DiscussionboardValues dv = new DiscussionboardValues();
                dv.setAnswer(String.valueOf(answer.getText()));
                dv.setQuestion(String.valueOf(question.getText()));
                String key;
                if (id == null) {
                    key = DiscussionboardCloudEndPoint.push().getKey();
                } else {
                    key = id;
                }
                dv.setId(key);
                DiscussionboardCloudEndPoint.child(key).setValue(dv);
                Toast.makeText(getApplication(), R.string.updated, Toast.LENGTH_SHORT).show();


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiscussionboardCloudEndPoint.child(id).removeValue();
                Toast.makeText(getApplication(), R.string.deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    protected void onResume() {
        super.onResume();
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
