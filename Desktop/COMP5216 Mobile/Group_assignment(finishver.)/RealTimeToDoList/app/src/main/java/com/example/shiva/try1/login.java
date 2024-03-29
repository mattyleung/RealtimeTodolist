package com.example.shiva.try1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * The type Login.
 */
public class login extends AppCompatActivity {
    /**
     * The Email.
     */
    EditText Email, /**
     * The Password.
     */
    Password;
    /**
     * The Log in button.
     */
    Button LogInButton, /**
     * The Register button.
     */
    RegisterButton;
    /**
     * The M auth.
     */
    FirebaseAuth mAuth;
    /**
     * The M auth listner.
     */
    FirebaseAuth.AuthStateListener mAuthListner;
    /**
     * The M user.
     */
    FirebaseUser mUser;
    /**
     * The Email.
     */
    String email, /**
     * The Password.
     */
    password;
    /**
     * The Dialog.
     */
    ProgressDialog dialog;
    /**
     * The constant userEmail.
     */
    public static final String userEmail = "";

    /**
     * The constant TAG.
     */
    public static final String TAG = "LOGIN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.login);
        LogInButton = (Button) findViewById(R.id.buttonLogin);

        RegisterButton = (Button) findViewById(R.id.buttonRegister);

        Email = (EditText) findViewById(R.id.editEmail);
        Password = (EditText) findViewById(R.id.editPassword);
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(login.this, dashboardDummy.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "AuthStateChanged:Logout");
                }

            }
        };
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSign();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(login.this, Register.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    public void onBackPressed() {
        login.super.finish();
    }


    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(login.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();

                    Toast.makeText(login.this, "Login not successfull", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();

                    checkIfEmailVerified();

                }
            }
        });

    }

    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        } else {
            Email.getText().clear();

            Password.getText().clear();
            Intent intent = new Intent(login.this, DashboardPage.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail, email);

            startActivity(intent);

        }
    }

}

