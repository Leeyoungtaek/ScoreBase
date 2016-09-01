package com.scorebase.scorebase;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lee young teak on 2016-08-25.
 */
public class MyInformationFragment extends Fragment {

    // Views
    private TextView nameText;
    private Button uploadImage;
    private CircleImageView profileImage;

    // Firebase
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private UploadTask uploadTask;

    // Gallery Request Code
    public final static int PICK_FROM_GALLERY = 1001;

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
        uploadImage = (Button) view.findViewById(R.id.button_upload_image);

        // Set Profile Image
        if(((MainActivity)getActivity()).getImage_bitmap()==null){
            profileImage.setImageResource(R.drawable.ic_clear_black_48dp);
        }else{
            profileImage.setImageBitmap(((MainActivity)getActivity()).getImage_bitmap());
        }

        // Go to Gallery
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
            }
        });

        // Set View
        nameText.setText(auth.getCurrentUser().getEmail());

        return view;
    }

    private byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Request to gallery
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();

                    // Compress Bitmap
                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, height/(width/640), true);
                    final Bitmap finalBitmap = bitmap;

                    storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + auth.getCurrentUser().getEmail() + ".jpg");
                    uploadTask = storageReference.putBytes(bitmapToByteArray(bitmap));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ((MainActivity)getActivity()).setImage_bitmap(finalBitmap);
                            profileImage.setImageBitmap(((MainActivity)getActivity()).getImage_bitmap());
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

