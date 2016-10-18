package com.scorebase.scorebase.Sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scorebase.scorebase.DataFormat.Account;
import com.scorebase.scorebase.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpDetailActivity extends AppCompatActivity {

    // Const
    public final static int PICK_FROM_GALLERY = 1001;

    // View
    private EditText inputName, inputIntroduction;
    private Button btnSignUp, btnBack, btnUploadImage;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CircleImageView imageView;

    // FireBase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageReference profileImageRef;

    // Data
    private String email;
    private String password;
    private String fileName;
    private Account userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);

        // Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        // View Reference
        inputName = (EditText) findViewById(R.id.edit_text_name);
        inputIntroduction = (EditText) findViewById(R.id.edit_text_introduction);
        btnUploadImage = (Button) findViewById(R.id.button_sign_up_upload_image);
        btnSignUp = (Button) findViewById(R.id.button_sign_up_detail);
        btnBack = (Button) findViewById(R.id.button_back);
        radioButton = (RadioButton) findViewById(R.id.radio_button_man);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group_sign_up);
        imageView = (CircleImageView) findViewById(R.id.sing_up_image);

        // View Set
        radioButton.setChecked(true);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
        imageView.setImageBitmap(image);

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View Event
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String createdAt = String.valueOf(System.currentTimeMillis());
                final String name = inputName.getText().toString().trim();
                final String introduction = inputIntroduction.getText().toString().trim();

                int Id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(Id);
                final String gender = radioButton.getText().toString();

                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data = baos.toByteArray();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(introduction)) {
                    Toast.makeText(getApplicationContext(), "소개를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = auth.getCurrentUser();

                        fileName = auth.getCurrentUser().getUid() + ".jpg";
                        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://scorebase-6b4ac.appspot.com/");
                        profileImageRef = storageReference.child("accounts/images/" + fileName);

                        UploadTask uploadTask = profileImageRef.putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "이미지 업로드에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getApplicationContext(), "이미지 다운로드에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                        userData = new Account(createdAt, name, user.getEmail(), gender, introduction, uri, "password", user.getUid());
                                        databaseReference.child("accounts").child(user.getUid()).setValue(userData);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "이미지 다운로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "이미지 업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();

                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, height / (width / 640), true);

                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
