package com.example.birdfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BirdDataActivity extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mapIcon;
    private ImageView searchIcon;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_data);
        ArrayList<BirdDataModel> birdDataList = this.getIntent().getExtras().getParcelableArrayList("Birds");
        mapIcon = findViewById(R.id.mapIcon);
        searchIcon = findViewById(R.id.searchIcon);
        mapIcon.setDefaultFocusHighlightEnabled(false);
        searchIcon.setDefaultFocusHighlightEnabled(false);
        BirdDataModel myBird = birdDataList.get(0);
        Log.d("BOK", "My Bird is in new activity " + myBird.getName());
        Log.w("BirdDataActivity", "on " + myBird.getDate() + " there were " + myBird.getAmount() + " " + myBird.getName() + "('s) at " + myBird.getLocation());
        mTextView = findViewById(R.id.birdText);
        if(!myBird.getAmount().equalsIgnoreCase("1")){
            mTextView.setText("on " + myBird.getDate() + " there were  " + myBird.getAmount() + " " + myBird.getName() + "s at " + myBird.getLocation());
        }
        else if(myBird.getAmount().equalsIgnoreCase("1")){
            mTextView.setText("on " + myBird.getDate() + " there was " + myBird.getAmount() + " " + myBird.getName() + " at " + myBird.getLocation());
        }

        for(int i = 1; i < birdDataList.size(); i++){
            BirdDataModel nextBird = birdDataList.get(i);
            if(!nextBird.getAmount().equalsIgnoreCase("1")){

                mTextView.append("\n\non " + nextBird.getDate() + " there were " + nextBird.getAmount() + " " + nextBird.getName() + "s at " + nextBird.getLocation());
            }
            else if(nextBird.getAmount().equalsIgnoreCase("1")){

                mTextView.append("\n\non " + nextBird.getDate() + " there was " + nextBird.getAmount() + " " + nextBird.getName() + " at " + nextBird.getLocation());
            }
        }

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapClick(birdDataList);
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClick();
            }
        });
        searchIcon.clearFocus();

        mapIcon.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pressDownColor(v, event);
                return false;
            }
        });

        searchIcon.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pressDownColor(v, event);
                return false;
            }
        });
    }

    private void mapClick(ArrayList<BirdDataModel> birdDataList) {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Birds", birdDataList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void searchClick() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
    public static void pressDownColor(View v, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                int pressedColor = Color.parseColor("#68B0AB");
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(pressedColor,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
    }

}