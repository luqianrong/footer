package com.example.footer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by 乃军 on 2018/1/11.
 */

public class DownImage extends AsyncTask {

    private ImageView imageView;

    public DownImage(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    protected Bitmap doInBackground(Object[] objects) {
        String url = (String)objects[0];
        Bitmap bitmap = null;
        try {
            //加载一个网络图片
           /* InputStream is = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(is);*/

            Bitmap bitmap1=ImageLoader.getInstance().loadImageSync(url);
            bitmap=BitmapCut.ImageCrop(bitmap1,2,3,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Bitmap bitmap=(Bitmap)o;
        imageView.setImageBitmap(bitmap);
    }
}
