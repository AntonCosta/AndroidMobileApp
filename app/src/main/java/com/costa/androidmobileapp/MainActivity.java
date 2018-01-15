package com.costa.androidmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = "LOGIN_SIGNUP";
    private Button loginButton,registerButton;
    private EditText emailEdit,passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        loginButton = (Button) findViewById(R.id.button);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickRegister(emailEdit.getText().toString(),passwordEdit.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void btnClick(String email,String password) {
        Log.d(TAG,"log in: "+email);

        //TODO validate
        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(MainActivity.this,"Authentication field empty!",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //log in successful,updated UI with the user information
                        FirebaseUser user = mAuth.getCurrentUser();


                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        rootRef.child("admins").child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String email = dataSnapshot.getValue(String.class);
                                if (email.equals(mAuth.getCurrentUser().getEmail())) {
                                    loggedIn(true);
                                } else {
                                    loggedIn(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        //log in fails
                        Toast.makeText(MainActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void btnClickRegister(String email,String password){
        //TODO validate email and password


        if (email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(MainActivity.this,"Authentication field empty!",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //register successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        loggedIn(false);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void mailBtnClick(View view)
    {
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //   final Intent intent = new Intent(this, ContactUsActivity.class);

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailEditText.getText().toString() });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Credentials");
        intent.putExtra(Intent.EXTRA_TEXT, passwordEditText.getText().toString());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    public void loggedIn(boolean isAdmin){
        Intent intent;
        if (isAdmin==true){
            intent =new Intent(MainActivity.this,AdminActivity.class);
        }
        else{
            intent=new Intent(MainActivity.this,Navigation.class);
        }
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
