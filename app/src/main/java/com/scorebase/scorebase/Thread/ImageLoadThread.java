package com.scorebase.scorebase.Thread;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoadThread extends Thread {

    private Handler handler;
    private Uri uri;

    public ImageLoadThread(Handler handler, Uri uri) {
        this.handler = handler;
        this.uri = uri;
    }

    @Override
    public void run() {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = getImageFromFirebase(uri);

        handler.sendMessage(msg);
    }

    private Bitmap getImageFromFirebase(Uri uri) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
