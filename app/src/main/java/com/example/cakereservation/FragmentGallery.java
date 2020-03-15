package com.example.cakereservation;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class FragmentGallery extends AppCompatActivity {
    CarouselView my_CAROU;
    private int[] mImages = new int[]{
            R.drawable.img_my_cake_1,R.drawable.img_my_cake_2,R.drawable.img_my_cake_3,R.drawable.img_my_cake_4,R.drawable.img_my_cake_5
    };

    private String[] mImagesTitle = new String[]{
        "1","2","3","4","5"    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_my_cake);


        my_CAROU = findViewById(R.id.my_CAROU);
        my_CAROU.setPageCount(mImages.length);
        my_CAROU.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
                }
        });


    }

}
