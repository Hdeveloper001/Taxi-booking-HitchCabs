package com.example.preet.hitchcabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class SignupActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText txt_firstname, txt_lastname, txt_phonenumber, txt_email, txt_password;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        txt_firstname = (EditText)findViewById(R.id.txt_firstname);
        txt_lastname = (EditText)findViewById(R.id.txt_lastname);
        txt_phonenumber = (EditText)findViewById(R.id.txt_phonenumber);
        txt_email = (EditText)findViewById(R.id.txt_email);
        txt_password = (EditText)findViewById(R.id.txt_password);

        Button btn_signup = (Button)findViewById(R.id.btn_signup);
        Button btn_goSignIn = (Button)findViewById(R.id.btn_go_signin);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_email.getText().length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter your Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_password.getText().length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter your Password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_firstname.getText().length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter your First Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_lastname.getText().length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter your Last Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_phonenumber.getText().length() == 0){
                    Toast.makeText(SignupActivity.this, "Enter your Phone Number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = ProgressDialog.show(SignupActivity.this,"","Registering ...");
                mFirebaseAuth.createUserWithEmailAndPassword(txt_email.getText().toString(), txt_password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                                pd.dismiss();
                            }
                        });
            }
        });

        btn_goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SigninActivity.class));
            }
        });
    }
}
