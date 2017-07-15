package com.example.mahendran.teacherspet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mahendran.teacherspet.ClassesAndTeachers.ClassAndTeachers;
import com.example.mahendran.teacherspet.Connectivity.ConnectivityReceiver;
import com.example.mahendran.teacherspet.Connectivity.MyApplication;
import com.example.mahendran.teacherspet.firebase.Teachervalues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    private DatabaseReference mDatabase;
    private DatabaseReference teacherCloudEndPoint;
    private DatabaseReference listCloudEndPoint;
    @BindView(R.id.et_Username)
    EditText et_Username;
    @BindView(R.id.et_Password)
    EditText et_Password;
    @BindView(R.id.teacher_name)
    EditText teacherName;
    @BindView(R.id.button1)
    Button bt_SignIn;
    @BindView(R.id.sign_up)
    Button bt_SignUp;
    @BindView(R.id.btn_reset_password)
    Button resetButton;
    SharedPreferences sharedpreferences;
    private FirebaseAuth auth;
    private RadioGroup radioRoleGroup;
    private RadioButton radioRoleButton;
    private ProgressBar progressBar;
    private ArrayList<String> teacherList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutscreen);
        final LinearLayout nlayout = (LinearLayout) findViewById(R.id.name_layout);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.setPadding(0, 20, 0, 0);
        }
        ButterKnife.bind(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        teacherCloudEndPoint = mDatabase.child(getResources().getString(R.string.teacher));
        listCloudEndPoint = mDatabase.child(getResources().getString(R.string.teacherslist));


        radioRoleGroup = (RadioGroup) findViewById(R.id.radioRole);
        int selectedId = radioRoleGroup.getCheckedRadioButtonId();
        radioRoleButton = (RadioButton) findViewById(selectedId);
        teacherlist();
        checkConnection();
        autoAuthentication();
        radioRoleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioRoleButton1;
                int selectedId1 = radioRoleGroup.getCheckedRadioButtonId();
                radioRoleButton1 = (RadioButton) findViewById(selectedId1);
                if (radioRoleButton1.getText().toString().equals(getResources().getString(R.string.student))) {

                    nlayout.setVisibility(View.VISIBLE);
                }
                if (radioRoleButton1.getText().toString().equals(getResources().getString(R.string.teacher))) {
                    nlayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        bt_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(et_Username.getText());
                String password = String.valueOf(et_Password.getText());
                int selectedId = radioRoleGroup.getCheckedRadioButtonId();
                radioRoleButton = (RadioButton) findViewById(selectedId);
                if ((email == null) || (email.equals(""))) {
                    Toast.makeText(getApplication(), R.string.email_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password == null) {
                    Toast.makeText(getApplication(), R.string.password_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplication(), R.string.password_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.account_created,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                if (radioRoleButton.getText().equals("Teacher")) {
                    Teachervalues teachValues = new Teachervalues();
                    teachValues.setTeacherId(email);
                    String key = listCloudEndPoint.push().getKey();
                    listCloudEndPoint.child(key).setValue(teachValues);
                    DatabaseReference tempCloudEndPoint;
                    tempCloudEndPoint = teacherCloudEndPoint.child(email.split("@")[0]);
                }
            }
        });

        bt_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = String.valueOf(et_Username.getText());
                final String password = String.valueOf(et_Password.getText());
                int selectedId = radioRoleGroup.getCheckedRadioButtonId();
                radioRoleButton = (RadioButton) findViewById(selectedId);
                final String teachermailID = String.valueOf(teacherName.getText());
                final CharSequence role = radioRoleButton.getText();
                if ((email == null) || (email.equals(""))) {
                    Toast.makeText(getApplication(), R.string.email_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password == null) {
                    Toast.makeText(getApplication(), R.string.password_required, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplication(), R.string.password_length, Toast.LENGTH_SHORT).show();
                    return;
                }
                authentication(email, password, role.toString(), teachermailID);

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = et_Username.getText().toString().trim();

                if ((email == null) || (email.equals(""))) {
                    Toast.makeText(getApplication(), R.string.reg_mail_id_needed, Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), R.string.instructions_sent, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.failed_to_send, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private boolean checkTeacher(String email, CharSequence role) {

        if ((teacherList.contains(email))) {
            if (email.equals(auth.getCurrentUser().getEmail()) && ((role.equals(getResources().getString(R.string.student))))) {
                Toast.makeText(getBaseContext(), R.string.login_as_teacher,
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            return true;
        }
        if (role.equals(getResources().getString(R.string.teacher))) {
            Toast.makeText(getBaseContext(), R.string.not_a_teacher,
                    Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
        return false;

    }


    public void teacherlist() {
        DatabaseReference listCloudEndPoint;
        listCloudEndPoint = mDatabase.child(getResources().getString(R.string.teacherslist));

        listCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {

                    noteSnapshot.getKey();
                    Teachervalues note = noteSnapshot.getValue(Teachervalues.class);
                    teacherList.add(note.getTeacherId());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), R.string.database_issue,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
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

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        statusDisplay(isConnected);
    }

    public void authentication(final String email, final String password, final String role, final String teachermailID) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(getBaseContext(), R.string.login_incorrect,
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            if ((role.equals(getResources().getString(R.string.teacher)) && (checkTeacher(email, role)))) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("Email", auth.getCurrentUser().getEmail().split("@")[0]);
                                editor.putString("User", email);
                                editor.putString("Password", password);
                                editor.putString("Role", role);
                                editor.putString("TmailID", "none");
                                editor.commit();
                                Intent intent = new Intent(getBaseContext(), ClassAndTeachers.class);
                                intent.putExtra("String", radioRoleButton.getText());
                                startActivity(intent);
                                finish();
                            } else if (role.equals(getResources().getString(R.string.student)) && !(checkTeacher(email, role))) {
                                if (!(teachermailID.equals("")) && (!(teachermailID == null)) && (checkTeacher(teachermailID, role))) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("Email", teachermailID.split("@")[0]);
                                    editor.putString("User", email);
                                    editor.putString("Password", password);
                                    editor.putString("Role", role);
                                    editor.putString("TmailID", teachermailID);
                                    editor.commit();
                                    Intent intent = new Intent(getBaseContext(), ClassAndTeachers.class);
                                    intent.putExtra("String", radioRoleButton.getText());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getBaseContext(), R.string.no_such_teacher,
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                            }

                        }

                    }
                });
    }

    public void autoAuthentication() {

        final String email = sharedpreferences.getString("User", null);
        if (email != null) {
            String password = sharedpreferences.getString("Password", null);
            final String teachermail = sharedpreferences.getString("TmailID", null);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.VISIBLE);

                            if (teachermail.equals("none")) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("Email", email.split("@")[0]);
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("Email", teachermail.split("@")[0]);
                                editor.commit();
                            }

                            Intent intent = new Intent(getBaseContext(), ClassAndTeachers.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }


}
