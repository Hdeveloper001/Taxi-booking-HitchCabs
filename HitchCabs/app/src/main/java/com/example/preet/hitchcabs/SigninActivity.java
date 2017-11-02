package com.example.preet.hitchcabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    EditText txt_Email, txt_password;
    private FirebaseAuth auth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();

        txt_Email = (EditText)findViewById(R.id.txt_email);
        txt_password = (EditText)findViewById(R.id.txt_password);
        Button btn_forgetPass = (Button)findViewById(R.id.btn_forgetPass);
        Button btn_signin = (Button)findViewById(R.id.btn_signin);
        Button btn_goSignUp = (Button)findViewById(R.id.btn_go_signup);

        btn_forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ForgotPasswordActivity.class));
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_Email.getText().length() == 0){
                    Toast.makeText(SigninActivity.this, "Enter your Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_password.getText().length() == 0){
                    Toast.makeText(SigninActivity.this, "Enter your Password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = ProgressDialog.show(SigninActivity.this,"","Ligin ...");

                auth.signInWithEmailAndPassword(txt_Email.getText().toString(), txt_password.getText().toString())
                        .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
// If sign in fails, display a message to the user. If sign in succeeds
// the auth state listener will be notified and logic to handle the
// signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SigninActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                pd.dismiss();
                            }
                        });

            }
        });

        btn_goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SignupActivity.class));
            }
        });
    }
}
