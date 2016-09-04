package com.scorebase.scorebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    // Views
    private EditText inputEmail, inputPassword, inputName, inputIntroduction;
    private Button btnSignUp, btnSignIn, btnResetPassword, btnUploadImage;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    // firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // View Reference
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        inputEmail = (EditText) findViewById(R.id.edit_text_email);
        inputPassword = (EditText) findViewById(R.id.edit_text_password);
        inputName = (EditText) findViewById(R.id.edit_text_name);
        inputIntroduction = (EditText) findViewById(R.id.edit_text_introduction);
        btnUploadImage = (Button) findViewById(R.id.button_sign_up_upload_image);
        btnSignUp = (Button) findViewById(R.id.button_sign_up);
        btnResetPassword = (Button) findViewById(R.id.button_password);
        btnSignIn = (Button) findViewById(R.id.button_sign_in);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        // Firebase Reference
        auth = FirebaseAuth.getInstance();

        // Go to ForgotPasswordActivity
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Go to SignInActivity
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // SignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get information
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String introduction = inputIntroduction.getText().toString().trim();
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String gender = radioButton.getText().toString();

                // Check Empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "이메일를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check Error
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "패스워드가 너무 짧습니다. 다시 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Loading ...
                progressBar.setVisibility(View.VISIBLE);

                // SingUp
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT);
                                } else {
                                    finish();
                                }
                            }
                        });
            }
        });

    }
}
