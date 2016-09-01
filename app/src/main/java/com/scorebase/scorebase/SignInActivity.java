package com.scorebase.scorebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    // Views
    private Button btnSignIn, btnResetPassword, btnSignUp;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // View Reference
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        inputEmail = (EditText)findViewById(R.id.edit_text_email);
        inputPassword = (EditText)findViewById(R.id.edit_text_password);
        btnSignIn = (Button)findViewById(R.id.button_sign_in);
        btnResetPassword = (Button)findViewById(R.id.button_password);
        btnSignUp = (Button)findViewById(R.id.button_sign_up);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();

        // Go to SingUpActivity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Go to ForgotPasswordActivity
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // SignIn
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email, password
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                // Check Empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Loading ...
                progressBar.setVisibility(View.VISIBLE);

                // SignIn
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // Error Check
                                if(!task.isSuccessful()){
                                    if(password.length()<6){
                                        inputPassword.setError("패스워드가 너무 짧습니다. 다시 입력해주세요!");
                                    }else{
                                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
