package com.scorebase.scorebase.Main;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scorebase.scorebase.Main.Fragment.GroupListFragment;
import com.scorebase.scorebase.Thread.ImageLoadThread;
import com.scorebase.scorebase.Main.Fragment.MyInformationFragment;
import com.scorebase.scorebase.Main.Fragment.NewsFeedFragment;
import com.scorebase.scorebase.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // View
    private TabLayout tabLayout;
    public ViewPager viewPager;

    // View:Fragment
    private FragmentManager mFragmentManager;
    private ViewPagerAdapter adapter;

    // FireBase
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseUser user;

    // Data
    private Bitmap imageBitmap;
    private int[] tabIcons = {
            R.drawable.ic_view_module_black_24dp,
            R.drawable.ic_forum_black_24dp,
            R.drawable.ic_face_black_24dp
    };

    // Thread
    private ImageLoadThread mThread;

    // Handler
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                imageBitmap = (Bitmap) msg.obj;

                Fragment frg = getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + 2);

                if (viewPager.getCurrentItem() != 0) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Reference & Set
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        // FireBase Reference
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("accounts/images/" + user.getUid() + ".jpg");

        // FireBase Event
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
                imageBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_clear_black_48dp);
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(mFragmentManager);
        adapter.addFragment(new GroupListFragment());
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
