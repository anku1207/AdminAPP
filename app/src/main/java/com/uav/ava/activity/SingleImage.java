package com.uav.ava.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.uav.ava.R;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.util.ExceptionHandler;
import com.uav.ava.volley.volley.VolleySingleton;
import com.uav.ava.volley.volley.VolleyUtils;

public class SingleImage extends AppCompatActivity {

    ImageView image;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysingle_image);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

       // getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        imageButton=(ImageButton)findViewById(R.id.imageButton);
        image = (ImageView) findViewById(R.id.image);

        Intent extera = getIntent();

        if(extera.getAction().equals(ApplicationConstant.SINGLE_IMAGE_ACTION_USER)){
            Uri myUri = Uri.parse(extera.getStringExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER));
            if(myUri!=null){
             /* byte[] byteArray = extras.getByteArray("picture");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/
                Bitmap bmp= VolleyUtils.grabImage(myUri,SingleImage.this);

                if(bmp.getWidth()>bmp.getHeight()){
                    Matrix matrix =new Matrix();
                    matrix.postRotate(90);
                    bmp= Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);

                }
                image.setImageBitmap(bmp);
            }
        }
        if(extera.getAction().equals(ApplicationConstant.SINGLE_IMAGE_ACTION_SERVER)){
            String s=extera.getStringExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_SERVER);


            if(s != null){
                ImageRequest imageRequest=  new ImageRequest(s,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
//                                            ImageView imageView=(ImageView)findViewById(R.id.imageview);
//                                            imageView.setImageBitmap(bitmap);

                                image.setImageBitmap(bitmap);
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                //mImageView.setImageResource(R.drawable.image_load_error);
                                Toast.makeText(SingleImage.this, "sdfsf", Toast.LENGTH_SHORT).show();
                            }
                        });
                // Access the RequestQueue through your singleton class.
                VolleySingleton.getInstance(SingleImage.this).addTorequestque(imageRequest);

            }

        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
