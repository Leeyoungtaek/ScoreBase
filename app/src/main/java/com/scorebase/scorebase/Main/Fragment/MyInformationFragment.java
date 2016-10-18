package com.scorebase.scorebase.Main.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scorebase.scorebase.DataFormat.Account;
import com.scorebase.scorebase.Main.MainActivity;
import com.scorebase.scorebase.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lee young teak on 2016-08-25.
 */
public class MyInformationFragment extends Fragment {

    // Const
    public final static int PICK_FROM_GALLERY = 1001;

    // View
    private TextView nameText;
    private Button btnUploadImage;
    private CircleImageView profileImage;

    // FireBase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference storageReference;
    private UploadTask uploadTask;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    // Data
    private final String[] name = new String[1];

    public MyInformationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_information, container, false);

        // View Reference
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        nameText = (TextView) view.findViewById(R.id.text_view_name);
        btnUploadImage = (Button) view.findViewById(R.id.button_upload_image);

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // View Set
        if(((MainActivity)getActivity()).getImageBitmap()==null){
            profileImage.setImageResource(R.drawable.ic_clear_black_48dp);
        }else{
            profileImage.setImageBitmap(((MainActivity)getActivity()).getImageBitmap());
        }
        databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Account account = child.getValue(Account.class);
                    if(account.getUid().equals(user.getUid())){
                        name[0] = account.getDisplayName();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        nameText.setText(name[0]);

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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();

                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, height / (width / 640), true);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    storageReference = FirebaseStorage.getInstance().getReference().child("accounts/images/" + user.getUid() + ".jpg");
                    uploadTask = storageReference.putBytes(byteArray);
                    final Bitmap finalBitmap = bitmap;
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ((MainActivity)getActivity()).setImageBitmap(finalBitmap);
                            profileImage.setImageBitmap(((MainActivity)getActivity()).getImageBitmap());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "다시 이미지를 업로드해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

