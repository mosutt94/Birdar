package com.example.birdfinder;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<BirdDataModel> birdDataList = this.getIntent().getExtras().getParcelableArrayList("Birds");
        ImageView scrollIcon = findViewById(R.id.scrollIcon);
        ImageView searchIcon = findViewById(R.id.searchIcon);


        searchIcon.setDefaultFocusHighlightEnabled(false);
        scrollIcon.setDefaultFocusHighlightEnabled(false);
        scrollIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollClick(birdDataList);
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClick();
            }
        });

        scrollIcon.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BirdDataActivity.pressDownColor(v, event);
                return false;
            }
        });

        searchIcon.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BirdDataActivity.pressDownColor(v, event);
                return false;
            }
        });


        //Bundle bundle = intent.getBundleExtra("bundle");
        //ArrayList<BirdDataModel> birdDataList = (ArrayList<BirdDataModel>) getIntent().getSerializableExtra("BIRDDATALIST");
        for(int i = 0; i < birdDataList.size(); i++) {
            BirdDataModel myBird = birdDataList.get(i);
            Log.d("BOK", "My Bird is in new activity " + myBird.getName());
            Log.w("BirdDataActivity", "on " + myBird.getMonth() + "/" + myBird.getDay() + "/" + myBird.getYear() + " at " + myBird.getTime() + " there were " + myBird.getAmount() + " " + myBird.getName() + "('s) at " + myBird.getLocation());

            // Add a marker in Sydney and move the camera
            double lat = Double.parseDouble(myBird.getLat());
            double lon = Double.parseDouble(myBird.getLon());
            LatLng birdSpot = new LatLng(lat, lon);
            int height = 140;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pin);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap bigMarker = Bitmap.createScaledBitmap(b, width, height, false);
            //Marker marker = new Marker
            mMap.addMarker(new MarkerOptions()
                    .position(birdSpot)
                    .title(myBird.getLocation())
                    .snippet(myBird.getMonth() + "/" + myBird.getDay() + "/" + myBird.getYear() + " " + (myBird.getTime() != null? myBird.getTime(): "") + " " + myBird.getAmount() + " seen")
                    .icon(BitmapDescriptorFactory.fromBitmap(bigMarker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(birdSpot, 9f));



        }
    }
    private void scrollClick(ArrayList<BirdDataModel> birdDataList) {
        Intent intent = new Intent(getBaseContext(), BirdDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Birds", birdDataList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void searchClick() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}

