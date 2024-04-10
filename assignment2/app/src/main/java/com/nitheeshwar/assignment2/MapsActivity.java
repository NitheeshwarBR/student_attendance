package com.nitheeshwar.assignment2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.location.Address;
import android.location.Geocoder;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private EditText eventName, eventFromDate, eventToDate, searchEvent, eventLocation;
    private Button addEventButton, searchButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        db = new DatabaseHelper(this);

        eventName = findViewById(R.id.eventName);
        eventFromDate = findViewById(R.id.eventFromDate);
        eventToDate = findViewById(R.id.eventToDate);
        searchEvent = findViewById(R.id.searchEvent);
        eventLocation = findViewById(R.id.eventLocation); // Assuming you've added this field to your layout
        addEventButton = findViewById(R.id.addEventButton);
        searchButton = findViewById(R.id.searchButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        addEventButton.setOnClickListener(view -> addEvent());

        searchButton.setOnClickListener(view -> searchForEvent());
    }

    private void addEvent() {
        String name = eventName.getText().toString();
        String startDate = eventFromDate.getText().toString();
        String endDate = eventToDate.getText().toString();
        String locationName = eventLocation.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                Event event = new Event(name, startDate, endDate, latitude, longitude);
                db.addEvent(event);

                LatLng latLng = new LatLng(latitude, longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                        .snippet("From: " + startDate + " To: " + endDate)
                        .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor())));
                if (marker != null) {
                    marker.setTag(event); // Save event object in marker for retrieval in onMarkerClick
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        loadEventsAndMarkers();
    }

    private void loadEventsAndMarkers() {
        List<Event> events = db.getAllEvents();
        for (Event event : events) {
            LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(event.getName())
                    .snippet("From: " + event.getStartDate() + " To: " + event.getEndDate())
                    .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor())));
            if (marker != null) {
                marker.setTag(event);
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Event event = (Event) marker.getTag();
        if (event != null) {
            showBottomSheetDialog(event);
        }
        return true;
    }

    private void showBottomSheetDialog(Event event) {
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        @SuppressLint("MissingInflatedId") TextView eventName = view.findViewById(R.id.eventDetailName);
        @SuppressLint("MissingInflatedId") TextView eventDates = view.findViewById(R.id.eventDetailDates);

        eventName.setText(event.getName());
        eventDates.setText(String.format(Locale.getDefault(), "From: %s To: %s", event.getStartDate(), event.getEndDate()));

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    private float getMarkerColor() {
        // This method can be enhanced to cycle through a set of colors
        // For simplicity, this example uses a fixed color
        return BitmapDescriptorFactory.HUE_ROSE;
    }

    private void searchForEvent() {
        String searchText = searchEvent.getText().toString();
        if (!TextUtils.isEmpty(searchText)) {
            // Assuming you have implemented searchEventsByNameOrLocation method in DatabaseHelper
            List<Event> events = db.searchEventsByNameOrLocation(searchText);
            mMap.clear(); // Clear previous markers
            for (Event event : events) {
                LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(event.getName())
                        .snippet("From: " + event.getStartDate() + " To: " + event.getEndDate())
                        .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor())));
                if (marker != null) {
                    marker.setTag(event);
                }
            }
        }
    }
}
