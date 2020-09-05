package com.example.ma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignUpActivity extends AppCompatActivity {
    EditText sEmail, sPass, sConfirmPass;
    Button SignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        sEmail = findViewById(R.id.sgemail);
        sPass = findViewById(R.id.sgpass);
        sConfirmPass = findViewById(R.id.sgconfirmpass);
        SignUp  = findViewById(R.id.SignUp);

            mAuth = FirebaseAuth.getInstance();

            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        Intent intent = new Intent(SignUpActivity.this, PhotoActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            };



            SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = sEmail.getText().toString();
                    final String password = sPass.getText().toString();
                    final String cpassword = sConfirmPass.getText().toString();
                    if(password.equals(cpassword)) {

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                    current_user_db.setValue(true);
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }


        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(firebaseAuthListener);
        }
        @Override
        protected void onStop() {
            super.onStop();
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


