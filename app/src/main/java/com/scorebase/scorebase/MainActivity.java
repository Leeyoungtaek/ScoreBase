package com.scorebase.scorebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager mFragmentManager;
    private ViewPagerAdapter adapter;

    private Bitmap image_bitmap;

    private FirebaseAuth auth;
    private StorageReference storageReference;

    private int[] tabIcons ={
            R.drawable.ic_view_module_black_24dp,
            R.drawable.ic_forum_black_24dp,
            R.drawable.ic_face_black_24dp
    };

    private ImageLoadThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + auth.getCurrentUser().getEmail() + ".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final Uri newUri = uri;
                mThread = new ImageLoadThread(mHandler, newUri);
                mThread.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image_bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_profile);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg){
            if(msg.what == 0){
                image_bitmap = (Bitmap) msg.obj;
                Fragment frg = null;
                frg = getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + 2);
                if(frg != null){
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                }
            }
        }
    };

    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setImage_bitmap(Bitmap image_bitmap){
        this.image_bitmap = image_bitmap;
    }
    public Bitmap getImage_bitmap(){
        return image_bitmap;
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFragmentManager);
        adapter.addFragment(new GroupFragment());
        adapter.addFragment(new NewsFeedFragment());
        adapter.addFragment(new MyInformationFragment());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
